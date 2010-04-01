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
