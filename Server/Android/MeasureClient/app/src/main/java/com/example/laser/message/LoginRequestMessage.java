package com.example.laser.message;


import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by free on 2016/9/1.
 */
public class LoginRequestMessage
{
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public int getClientType()
	{
		return clientType;
	}
	
	public void setClientType(int clientType)
	{
		this.clientType = clientType;
	}
	
	@Protobuf(fieldType = FieldType.STRING, order = 1, required = true)
	private String username;
	@Protobuf(fieldType = FieldType.STRING, order = 2, required = true)
	private String password;
	@Protobuf(fieldType = FieldType.INT32, order = 3, required = true)
	private int clientType;
}
