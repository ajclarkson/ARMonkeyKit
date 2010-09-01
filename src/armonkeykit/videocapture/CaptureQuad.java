/*******************************************************************************
*    This file is part of ARMonkeyKit.
*
*    ARMonkeyKit is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    ARMonkeyKit is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with ARMonkeyKit.  If not, see <http://www.gnu.org/licenses/>.
******************************************************************************/
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
