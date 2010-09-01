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

package armonkeykit.core.app.utils;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class Rotate {
	
	public static final Quaternion ROLL045  = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(0,0,1));
    public static final Quaternion ROLL090  = new Quaternion().fromAngleAxis(FastMath.PI/2,   new Vector3f(0,0,1));
    public static final Quaternion ROLL180  = new Quaternion().fromAngleAxis(FastMath.PI  ,   new Vector3f(0,0,1));
    public static final Quaternion ROLL270  = new Quaternion().fromAngleAxis(FastMath.PI*3/2, new Vector3f(0,0,1));
    public static final Quaternion YAW045n  = new Quaternion().fromAngleAxis(- FastMath.PI/4, new Vector3f(0,1,0));
    public static final Quaternion YAW045   = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(0,1,0));
    public static final Quaternion YAW090   = new Quaternion().fromAngleAxis(FastMath.PI/2,   new Vector3f(0,1,0));
    public static final Quaternion YAW180   = new Quaternion().fromAngleAxis(FastMath.PI  ,   new Vector3f(0,1,0));
    public static final Quaternion YAW270   = new Quaternion().fromAngleAxis(FastMath.PI*3/2, new Vector3f(0,1,0));
    public static final Quaternion PITCH045 = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(1,0,0));
    public static final Quaternion PITCH090 = new Quaternion().fromAngleAxis(FastMath.PI/2,   new Vector3f(1,0,0));
    public static final Quaternion PITCH180 = new Quaternion().fromAngleAxis(FastMath.PI  ,   new Vector3f(1,0,0));
    public static final Quaternion PITCH270 = new Quaternion().fromAngleAxis(FastMath.PI*3/2, new Vector3f(1,0,0));
    public static final Quaternion UPRIGHT  = new Quaternion().fromAngleAxis(- FastMath.PI/2, new Vector3f(1,0,0));
    
	

}
