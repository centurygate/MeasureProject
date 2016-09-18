package enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by free on 2016/9/1.
 */
public enum CommandEnum
{
	message$SimpleTypeTest,
	message$LoginRequestMessage,
	message$LoginResponseMessage,
	message$PushSingleMeasureMessage,
	message$PushBulkMeasureMessage,
	message$PullBulkMeasureMeaage,
	message$KeepAliveRequestMessage,
	message$KeepAliveResponseMessage,
	message$PushMeasureDataMessage;
	// Implementing a fromString method on an enum type
	private static final Map<String, Integer> name2TypeMap = new HashMap();
	private static final Map<Integer, Class> type2ClassMap = new HashMap();
	
	static
	{
		// Initialize map from constant name to enum constant
		for (CommandEnum commandEnum : values())
		{
			name2TypeMap.put(commandEnum.toString().replace('$', '.'), commandEnum.ordinal());
			try
			{
				type2ClassMap.put(commandEnum.ordinal(), Class.forName(commandEnum.toString().replace('$', '.')));
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static Integer getType(String className)
	{
		return name2TypeMap.get(className);
	}
	
	public static Class getClass(Integer type)
	{
		return type2ClassMap.get(type);
	}
	
	public static Class getClass(String name)
	{
		try
		{
			return Class.forName(name);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
