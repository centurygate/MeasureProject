package com.example.laser.message;



import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;




/**
 * Created by free on 2016/9/2.
 */
public class PushBulkMeasureMessage
{

	public List<MeasureRecord> getMeasureLst()
	{
		return measureLst;
	}

	public void setMeasureLst(List<MeasureRecord> measureLst)
	{
		this.measureLst = measureLst;
	}
	@Override
	public String toString()
	{
		return "PushBulkMeasureMessage{" +
				"measureMap=" + measureLst +
				'}';
	}
	
	@Protobuf(fieldType = FieldType.OBJECT,order = 1,required = true)
	private List<MeasureRecord> measureLst;


}
