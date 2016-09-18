package com.example.laser.message;



import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by free on 2016/9/2.
 */
public class MeasureRecord
{
	public String getDeviceAddr()
	{
		return deviceAddr;
	}
	
	public void setDeviceAddr(String deviceAddr)
	{
		this.deviceAddr = deviceAddr;
	}
	
	public long getTime()
	{
		return time;
	}
	
	public void setTime(long time)
	{
		this.time = time;
	}
	
	public double getDistance()
	{
		return distance;
	}
	
	public void setDistance(double distance)
	{
		this.distance = distance;
	}
	
	@Override
	public String toString()
	{
		return "MeasureRecord{" +
				"deviceAddr='" + deviceAddr + '\'' +
				", time=" + time +
				", distance=" + distance +
				'}';
	}
	
	@Protobuf(fieldType = FieldType.STRING,order=1,required = true)
	private String deviceAddr;
	@Protobuf(fieldType = FieldType.UINT64,order=2,required = true)
	private long time;
	@Protobuf(fieldType = FieldType.DOUBLE,order=3,required = true)
	private double distance;
}
