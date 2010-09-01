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
package armonkeykit.examples.patternmarkers.basic;

import java.net.URL;

import org.llama.jmf.JMFVideoImage;

import com.jme.image.Texture2D;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

public class VideoQuad extends Quad {
	private static final long serialVersionUID = 2066861456419104457L;
	
	private JMFVideoImage image;
	private Texture2D tex;

	public VideoQuad(String name) {
		super(name);
	}
	
	public void update() {
		if(image != null) {
			if (!image.update(tex, false)) {
				image.waitSome(3);
			}
		}
	}
	
	private void loadVideo(URL videoResource) {		
		try {
			image = new JMFVideoImage(videoResource, true, JMFVideoImage.SCALE_MAXIMIZE);
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
	
	public void setVideoURL(URL url) {
		loadVideo(url);
		
	}

	public void start() {
		if(image != null) {
			image.startMovie();
		}
	}

}
