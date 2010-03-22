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
		if(image != null) {
			image.update(tex);
		}
	}
	/**
	 * Added this method for debugging openGL error
	 * @return int textureID.
	 */
	public int getTexID(){
		return tex.getTextureId();
	}
	
	public QtNyARRaster_RGB getRaster() {
		return image.getRaster();
	}

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
