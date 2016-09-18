package com.example.laser.message;



import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by free on 2016/9/1.
 */
public class ResponseCmdHeader
{
	public ResponseCmdHeader()
	{
	}
	
	public int getResultcode()
	{
		return resultcode;
	}
	
	public void setResultcode(int resultcode)
	{
		this.resultcode = resultcode;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	@Override
	public String toString()
	{
		return "ResponseCmdHeader{" +
				"resultcode=" + resultcode +
				", desc='" + desc + '\'' +
				'}';
	}
	
	@Protobuf(fieldType = FieldType.INT32,order=1,required = true)
	private int resultcode;
	
	
	@Protobuf(fieldType = FieldType.STRING,order=2,required = true)
	private String desc;
}
