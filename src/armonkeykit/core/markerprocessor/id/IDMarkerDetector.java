package armonkeykit.core.markerprocessor.id;

import armonkeykit.videocapture.SyncObject;
import jp.nyatla.nyartoolkit.NyARException;
import jp.nyatla.nyartoolkit.core.param.NyARParam;
import jp.nyatla.nyartoolkit.core.squaredetect.NyARSquare;
import jp.nyatla.nyartoolkit.core.transmat.NyARTransMatResult;
import jp.nyatla.nyartoolkit.nyidmarker.data.INyIdMarkerData;
import jp.nyatla.nyartoolkit.nyidmarker.data.NyIdMarkerDataEncoder_RawBit;
import jp.nyatla.nyartoolkit.nyidmarker.data.NyIdMarkerData_RawBit;
import jp.nyatla.nyartoolkit.processor.SingleNyIdMarkerProcesser;

public class IDMarkerDetector extends SingleNyIdMarkerProcesser{

	
	private int currentID;
	private NyARTransMatResult transMatResult;

	public IDMarkerDetector(NyARParam jmeARParameters, int markerWidth, int rasterFormat) {
		super();
		try {
			initInstance(jmeARParameters, new NyIdMarkerDataEncoder_RawBit(), markerWidth, rasterFormat);
		} catch (NyARException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onEnterHandler(INyIdMarkerData arCode) {
		synchronized(SyncObject.getSyncObject()){
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
		synchronized(SyncObject.getSyncObject()){
			this.currentID=-1;
			this.transMatResult=null;
		}
		
	}

	@Override
	protected void onUpdateHandler(NyARSquare square, NyARTransMatResult result) {
		synchronized(SyncObject.getSyncObject()){
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
