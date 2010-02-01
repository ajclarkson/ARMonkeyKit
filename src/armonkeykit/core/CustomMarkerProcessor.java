package armonkeykit.core;

import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.param.NyARParam;
import jp.nyatla.nyartoolkit.core.squaredetect.NyARSquare;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import jp.nyatla.nyartoolkit.nyidmarker.data.INyIdMarkerData;
import jp.nyatla.nyartoolkit.nyidmarker.data.NyIdMarkerDataEncoder_RawBit;
import jp.nyatla.nyartoolkit.nyidmarker.data.NyIdMarkerData_RawBit;
import jp.nyatla.nyartoolkit.processor.SingleNyIdMarkerProcesser;

public class CustomMarkerProcessor extends SingleNyIdMarkerProcesser{

	private Object syncObject = new Object();
	private NyARTransMatResult transMatResult = null;
	private int currentID;

	public CustomMarkerProcessor(NyARParam jmeARParameters, int markerWidth, int rasterFormat) throws NyARException {
		super();
		initInstance(jmeARParameters, new NyIdMarkerDataEncoder_RawBit(), markerWidth, rasterFormat);
		return;
	}
	
	@Override
	protected void onEnterHandler(INyIdMarkerData arCode) {
		synchronized(this.syncObject){
			NyIdMarkerData_RawBit rawCode = (NyIdMarkerData_RawBit)arCode;
			//if the length of identified code is more than 4, it is outside of scope for our markers. 
			if (rawCode.length > 4) {
				this.currentID=-1; //signifies undefined ID.
				
			}else {
				this.currentID = 0; //replace null value to perform bit shifting
				for (int i=0; i<rawCode.length;i++){
					this.currentID = (this.currentID << 8 | rawCode.packet[i]);
				}
			}
			this.transMatResult = null;
		}
	}

	@Override
	protected void onLeaveHandler() {
		synchronized(this.syncObject){
			this.currentID=-1;
			this.transMatResult=null;
		}
		
	}

	@Override
	protected void onUpdateHandler(NyARSquare square, NyARTransMatResult result) {
		
		synchronized(this.syncObject){
			this.transMatResult = result;
		}
	}

	
	public NyARTransMatResult getTransMatResult(){
		return this.transMatResult;
	}
	
	public int getCurrentID(){
		return this.currentID;
	}
}
