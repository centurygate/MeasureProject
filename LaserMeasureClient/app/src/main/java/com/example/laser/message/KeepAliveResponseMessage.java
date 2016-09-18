package com.example.laser.message;



import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by free on 2016/9/2.
 */
public class KeepAliveResponseMessage
{
	@Protobuf(fieldType = FieldType.UINT64,order = 1,required = true)
	private long servertime;
	
	public long getServertime()
	{
		return servertime;
	}
	
	public void setServertime(long servertime)
	{
		this.servertime = servertime;
	}
	
	@Override
	public String toString()
	{
		return "KeepAliveResponseMessage{" +
				"servertime=" + servertime +
				'}';
	}
}
