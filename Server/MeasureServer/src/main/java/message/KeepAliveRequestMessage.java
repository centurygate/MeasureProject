package message;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by free on 2016/9/2.
 */
public class KeepAliveRequestMessage
{
	public RequestCmdHeader getCmdHeader()
	{
		return cmdHeader;
	}
	
	public void setCmdHeader(RequestCmdHeader cmdHeader)
	{
		this.cmdHeader = cmdHeader;
	}
	
	public long getClienttime()
	{
		return clienttime;
	}
	
	public void setClienttime(long clienttime)
	{
		this.clienttime = clienttime;
	}
	
	@Override
	public String toString()
	{
		return "KeepAliveRequestMessage{" +
				"cmdHeader=" + cmdHeader +
				", clienttime=" + clienttime +
				'}';
	}
	
	@Protobuf(fieldType = FieldType.OBJECT,order = 1,required = true)
	private RequestCmdHeader cmdHeader;
	@Protobuf(fieldType = FieldType.UINT64,order = 2,required = true)
	private long clienttime;
}
