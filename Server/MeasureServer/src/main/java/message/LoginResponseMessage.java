package message;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by free on 2016/9/1.
 */
public class LoginResponseMessage
{
	
	@Protobuf(fieldType = FieldType.OBJECT,order = 1,required = true)
	private ResponseCmdHeader respCmdHeader;
	@Protobuf(fieldType = FieldType.STRING,order=2,required = true)
	private String clientID;
	
	
	public String getClientID()
	{
		return clientID;
	}
	
	public void setClientID(String clientID)
	{
		this.clientID = clientID;
	}
	public ResponseCmdHeader getRespCmdHeader()
	{
		return respCmdHeader;
	}
	
	public void setRespCmdHeader(ResponseCmdHeader respCmdHeader)
	{
		this.respCmdHeader = respCmdHeader;
	}
	
}
