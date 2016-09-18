package message;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by free on 2016/9/1.
 */
public class RequestCmdHeader
{
	public RequestCmdHeader()
	{
		
	}
	
	@Override
	public String toString()
	{
		return "RequestCmdHeader{" +
				"clientID='" + clientID + '\'' +
				'}';
	}
	
	@Protobuf(fieldType = FieldType.STRING,order=1,required = true)
	private String clientID;
	
	public String getClientID()
	{
		return clientID;
	}
	
	public void setClientID(String clientID)
	{
		this.clientID = clientID;
	}
}
