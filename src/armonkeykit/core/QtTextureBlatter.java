package armonkeykit.core;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

import com.jme.image.Texture;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.types.NyARIntSize;
import jp.nyatla.nyartoolkit.qt.utils.QtCameraCapture;
import jp.nyatla.nyartoolkit.qt.utils.QtCaptureListener;
import jp.nyatla.nyartoolkit.qt.utils.QtNyARRaster_RGB;

public class QtTextureBlatter implements QtCaptureListener {
	private QtCameraCapture qtCameraCapture;
	private QtNyARRaster_RGB raster;
	private BufferedImage bufferedVideoImage;
	private Texture backgroundTexture;
	private TextureState backgroundTextureState;

	public QtTextureBlatter(int cameraWidth, int cameraHeight, float fps, TextureState textureState) throws NyARException {
		qtCameraCapture = new QtCameraCapture(cameraWidth, cameraHeight, fps);
		qtCameraCapture.setCaptureListener(this);
		this.raster = new QtNyARRaster_RGB(cameraWidth, cameraHeight);
		this.backgroundTextureState = textureState;
	}

	@Override
	/**
	 * We know this to be slow. Still need to apply comments from
	 * http://www.jmonkeyengine.com/forum/index.php?topic=12220.0
	 * 
	 */
	public void onUpdateBuffer(byte[] pixels)
	{
		try {
			getRaster().wrapBuffer(pixels);
		} catch (NyARException e) {
			e.printStackTrace();
		}

		NyARIntSize s=getRaster().getSize();
		WritableRaster wr = WritableRaster.createInterleavedRaster(DataBuffer.TYPE_BYTE, s.w,s.h, s.w * 3, 3, new int[] { 0, 1, 2 }, null);
		bufferedVideoImage = new BufferedImage(s.w, s.h, BufferedImage.TYPE_3BYTE_BGR);

		wr.setDataElements(0, 0, s.w, s.h,getRaster().getBuffer());
		bufferedVideoImage.setData(wr);

		if (backgroundTexture != null) {
			TextureManager.releaseTexture(backgroundTexture);
			TextureManager.deleteTextureFromCard(backgroundTexture);
		}
		synchronized (bufferedVideoImage) {

			backgroundTexture = TextureManager.loadTexture(bufferedVideoImage,
					Texture.MinificationFilter.BilinearNearestMipMap,
					Texture.MagnificationFilter.Bilinear, true);
			backgroundTextureState.setTexture(backgroundTexture);
		}
	}
	
	public void start() throws NyARException {
		qtCameraCapture.start();
	}

	public QtNyARRaster_RGB getRaster() {
		return raster;
	}

	public boolean hasData() {
		return bufferedVideoImage != null;
	}
}
