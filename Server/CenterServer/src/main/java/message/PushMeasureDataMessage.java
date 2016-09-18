package message;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by free on 2016/9/2.
 */
public class PushMeasureDataMessage
{
	@Protobuf(fieldType = FieldType.OBJECT,order = 1,required = true)
	private RequestCmdHeader cmdHeader;
	@Protobuf(fieldType = FieldType.OBJECT,order = 2,required = true)
	MeasureRecord singleMeasureRecord;
	
	@Override
	public String toString()
	{
		return "PushMeasureDataMessage{" +
				"cmdHeader=" + cmdHeader +
				", singleMeasureRecord=" + singleMeasureRecord +
				'}';
	}
	
	public RequestCmdHeader getCmdHeader()
	{
		return cmdHeader;
	}
	
	public void setCmdHeader(RequestCmdHeader cmdHeader)
	{
		this.cmdHeader = cmdHeader;
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
