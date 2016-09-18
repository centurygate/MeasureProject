package com.example.laser.message;



import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.util.List;

/**
 * Created by free on 2016/9/2.
 */
public class PullBulkMeasureMeaage
{
	public RequestCmdHeader getRequestCmdHeader()
	{
		return requestCmdHeader;
	}
	
	public void setRequestCmdHeader(RequestCmdHeader requestCmdHeader)
	{
		this.requestCmdHeader = requestCmdHeader;
	}
	
	public List<String> getDeviceAddrLst()
	{
		return deviceAddrLst;
	}
	
	public void setDeviceAddrLst(List<String> deviceAddrLst)
	{
		this.deviceAddrLst = deviceAddrLst;
	}
	
	public long getStartTime()
	{
		return startTime;
	}
	
	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}
	
	public long getEndTime()
	{
		return endTime;
	}
	
	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}
	
	@Override
	public String toString()
	{
		return "PullBulkMeasureMeaage{" +
				"requestCmdHeader=" + requestCmdHeader +
				", deviceAddrLst=" + deviceAddrLst +
				", startTime=" + startTime +
				", endTime=" + endTime +
				'}';
	}
	
	@Protobuf(fieldType = FieldType.OBJECT,order = 1,required = true)
	RequestCmdHeader requestCmdHeader;
	@Protobuf(fieldType = FieldType.STRING,order = 2,required = true)
	List<String> deviceAddrLst;
	@Protobuf(fieldType = FieldType.UINT64,order = 3,required = true)
	long startTime;
	@Protobuf(fieldType = FieldType.UINT64,order = 4,required = true)
	long endTime;
}
