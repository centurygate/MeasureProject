package converter;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by free on 2016/9/1.
 */

/****
 * to convert the class type to class name  or convert the class name to class type
 */
public class NameTypeConverter
{
	private static Map<Integer,String> type2NameMap = new ConcurrentHashMap<Integer, String>(100);
	private static Map<String,Integer> name2TypeMap = new ConcurrentHashMap<String, Integer>(100);
	static void registerNameType(String className,Integer classType)
	{
		type2NameMap.put(classType,className);
		name2TypeMap.put(className,classType);
	}
	static String getClassName(Integer classType)
	{
		return type2NameMap.get(classType);
	}
	static Integer getClassType(String className)
	{
		return name2TypeMap.get(className);
	}
}
