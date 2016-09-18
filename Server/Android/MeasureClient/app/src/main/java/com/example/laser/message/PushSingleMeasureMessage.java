package com.example.laser.message;



import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by free on 2016/9/2.
 */
public class PushSingleMeasureMessage
{
	@Protobuf(fieldType = FieldType.OBJECT,order = 1,required = true)
	MeasureRecord singleMeasureRecord;
	
	@Override
	public String toString()
	{
		return "PushSingleMeasureMessage{" +
				"singleMeasureRecord=" + singleMeasureRecord +
				'}';
	}
	
	public MeasureRecord getSingleMeasureRecord()
	{
		return singleMeasureRecord;
	}
	
	public void setSingleMeasureRecord(MeasureRecord singleMeasureRecord)
	{
		this.singleMeasureRecord = singleMeasureRecord;
	}
}
