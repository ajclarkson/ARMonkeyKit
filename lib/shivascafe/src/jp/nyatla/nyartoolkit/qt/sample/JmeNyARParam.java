/* 
 * PROJECT: NyARToolkit Java3D utilities.
 * --------------------------------------------------------------------------------
 * The MIT License
 * Copyright (c) 2008 nyatla
 * airmail(at)ebony.plala.or.jp
 * http://nyatla.jp/nyartoolkit/
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */
package jp.nyatla.nyartoolkit.qt.sample;

import jp.nyatla.nyartoolkit.core.param.NyARParam;
import jp.nyatla.nyartoolkit.jogl.utils.NyARGLUtil;

/**
 * NyARParam for jMonkeyEngine
 */
public class JmeNyARParam extends NyARParam {
	private float distMin = 10.0f;// 1cm～10.0m

	private float distMax = 10000.0f;

	private float[] projection = null;

	public void setViewDistanceMin(float val) {
		projection = null;// キャッシュ済変数初期化
		distMin = val;
	}

	public void setViewDistanceMax(float val) {
		projection = null;// キャッシュ済変数初期化
		distMax = val;
	}

	/**
	 * getCameraFrustum matrix
	 */
	public float[] getCameraFrustum() {
		// 既に値がキャッシュされていたらそれを使う
		if (projection != null) {
			return projection;
		}
		NyARGLUtil glu = new NyARGLUtil(null);
		double[] ad = new double[16];
		glu.toCameraFrustumRH(this, ad);

		float near = distMin;
		float far = distMax;
		float left = (float) (near * (ad[8] + 1) / ad[0]);
		float right = (float) (near * (ad[8] - 1) / ad[0]);
		float top = (float) (near * (ad[9] + 1) / ad[5]);
		float bottom = (float) (near * (ad[9] - 1) / ad[5]);

		projection = new float[] { near, far, left, right, top, bottom };
		return projection;
	}
}
