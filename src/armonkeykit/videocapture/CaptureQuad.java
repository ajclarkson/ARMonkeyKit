/*
 * Copyright (c) 2010, ARMonkeyKit
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'ARMonkeyKit' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package armonkeykit.videocapture;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.qt.utils.QtNyARRaster_RGB;

import com.jme.image.Texture2D;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

public class CaptureQuad extends Quad {
	private static final long serialVersionUID = 2066861456419104457L;

	private QtCaptureImage image;
	private Texture2D tex;

	public CaptureQuad(String name, int camWidth, int camHeight, float frameRate) {
		super(name);
		try {
			image = new QtCaptureImage();
			image.initializeCamera(camWidth, camHeight, frameRate);
			tex = new Texture2D();
			tex.setMinificationFilter(MinificationFilter.Trilinear);
			tex.setMagnificationFilter(MagnificationFilter.Bilinear);
			tex.setImage(image);			
			tex.setApply(ApplyMode.Replace);
			TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
			ts.setEnabled(true);
			ts.setTexture(tex);
			setRenderState(ts);
			updateRenderState();
		} catch (Exception e) {
			e.printStackTrace();
		}
		start();
		// could, at this point, resize to fit height to current width?
	}

	public void update() {
		// update if we have an image with a valid texture id
		if(image != null && tex != null && tex.getTextureId() != 0) {
			image.update(tex);
		}
	}
	
	/**
	 * Get the id of the texture used in the video capture
	 * @return int textureID.
	 */
	public int getTexID(){
		return tex.getTextureId();
	}

	public QtNyARRaster_RGB getRaster() {
		return image.getRaster();
	}

	/**
	 * Start capturing from the camera.
	 */
	public void start() {
		if(image != null) {
			try {
				image.start();
			} catch (NyARException e) {
				e.printStackTrace();
			}
		}
	}

}
