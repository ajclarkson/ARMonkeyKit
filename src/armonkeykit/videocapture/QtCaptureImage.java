/**
 * Heaviliy adapted from Tijl Houtbeckers' code from the JMF/FOBS/jME renderer,
 * see license below.
 */

/******************************************************************************
 * JMF/FOBS/jME renderer.
 * 
 * Copyright (c) 2006 Tijl Houtbeckers
 * Coded by Tijl Houtbeckers
 *
 *    This file is part of the JMF/FOBS/jME renderer.
 *
 *    The JMF/FOBS/jME renderer is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as
 *    published by the Free Software Foundation; either version 2.1 
 *    of the License, or (at your option) any later version.
 *
 *    The JMF/FOBS/jME renderer is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with FOBS; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 ******************************************************************************/

/**
 * @author Tijl Houtbeckers
 * 
 * Initial release: 23-02-2006
 * 
 * jME: jmonkeyengine.com
 * FOBS: http://fobs.sourceforge.net/
 * JMF: http://java.sun.com/products/java-media/jmf/ 
 */

package armonkeykit.videocapture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

import javax.media.format.RGBFormat;
import javax.media.opengl.GL;

import jp.nyatla.nyartoolkit.NyARException;

import jp.nyatla.nyartoolkit.qt.utils.QtCameraCapture;
import jp.nyatla.nyartoolkit.qt.utils.QtCaptureListener;
import jp.nyatla.nyartoolkit.qt.utils.QtNyARRaster_RGB;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.Util;


import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.util.geom.BufferUtils;

public class QtCaptureImage extends Image implements QtCaptureListener {

	private static final Logger log = Logger.getLogger(QtCaptureImage.class.getName());	
	private static final long serialVersionUID = -8413968528763966076L;
	public final static int SCALE_NONE = 0;
	public final static int SCALE_MAXIMIZE = 1;
	public final static int SCALE_FIT = 2;
	private int videowidth, videoheight; // frame dimensions

	public int getVideoWidth() {
		return videowidth;
	}

	public int getVideoHeight() {
		return videoheight;
	}

	long framecounter = 0;
	long lastupdated = 0;
	private ByteBuffer buffer;

	private int pixelformat, dataformat;
	private QtCameraCapture qtCameraCapture;
	private QtNyARRaster_RGB raster;
	private boolean initAndScaleTexture = false;

	public void setSize(int cameraWidth, int cameraHeight, RGBFormat format) {
		initializeCamera(cameraWidth, cameraHeight, 30f);
	}

	public void initializeCamera(int cameraWidth, int cameraHeight, float frameRate) {		
		if(qtCameraCapture == null) {
			qtCameraCapture = new QtCameraCapture(cameraWidth, cameraHeight, frameRate);
			try {
				qtCameraCapture.setCaptureListener(this);
				this.raster = new QtNyARRaster_RGB(cameraWidth, cameraHeight);
			} catch (NyARException e) {
				e.printStackTrace();
			}
		}

		pixelformat = GL.GL_RGB;
		this.setFormat(Format.RGB8);
		dataformat = GL11.GL_UNSIGNED_BYTE;

		this.videowidth = cameraWidth;
		this.videoheight = cameraHeight;

		try {
			int size = Math.max(cameraHeight, cameraWidth);

			if (!FastMath.isPowerOfTwo(size)) {
				int newsize = 2;
				do {
					newsize <<= 1;
				} while (newsize < size);
				size = newsize;
			}
			this.width = size;
			this.height = size;

			data.clear();
			data.add( ByteBuffer.allocateDirect(size*size*4).order(ByteOrder.nativeOrder()) );
			initAndScaleTexture  = true;
		} catch (Exception e) {
			e.printStackTrace();

		}
		synchronized (this) {
			this.notifyAll();
		}
	}

	public boolean update(Texture texture) {
		
		synchronized(this) {

			if(buffer == null) {
				return false;
			}
			buffer.rewind();

			if(initAndScaleTexture) {
				scaleTexture(texture);
			}
			

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, videowidth, videoheight, pixelformat, dataformat, buffer);
			


			try {
				Util.checkGLError();
			} catch (OpenGLException e) {
				log.info("Error rendering video to texture. No glTexSubImage2D/OpenGL 1.2 support?");
				System.err.println(e.getMessage());
			}

			lastupdated = framecounter;

			return true;
		}
	}



	private void scaleTexture(Texture texture) {
		texture.setScale(new Vector3f(videowidth* (1f / this.width),videoheight * (1f / this.height),1f));
	}

	public void onUpdateBuffer(byte[] pixels)
	{
		synchronized(this) {
			if(buffer == null) {
				buffer = BufferUtils.createByteBuffer(getWidth()*getHeight()*3);
			}
			try {
				buffer.clear();
				buffer.put(pixels);
				getRaster().wrapBuffer(pixels);
			} catch (NyARException e) {
				e.printStackTrace();
			}
		}
	}

	public void start() throws NyARException {
		qtCameraCapture.start();
	}

	public QtNyARRaster_RGB getRaster() {
		return raster;
	}
}
