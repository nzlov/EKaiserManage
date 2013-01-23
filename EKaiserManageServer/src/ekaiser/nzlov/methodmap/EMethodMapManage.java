package ekaiser.nzlov.methodmap;

import java.lang.reflect.Method;
import java.util.HashMap;


/**
 * 消息映射
 * @author nzlov
 *
 */
public class EMethodMapManage {
	private static EMethodMapManage def = new EMethodMapManage();
	/**
	 * 是否输出消息信息
	 */
	private static boolean isMessageInfo = false;
	private HashMap<String, EMethodMap> _eventMap = null;
	/**
	 * 发送消息
	 * @param name 消息触发名
	 * @param obj 消息触发对象
	 * @param parameter 参数
	 * @return
	 */
	public static Object sendMethodMessage(String name,Object obj,Object[] parameter){
		return sendMethodMessage(new EMethodMessage(name, obj, parameter));
	}
	/**
	 * 发送消息
	 * @param name 消息触发名
	 * @param obj 消息触发对象
	 * @param parameter 参数
	 * @return
	 */
	public static Object sendMethodMessage(String name,Object obj,Object parameter){
		return sendMethodMessage(new EMethodMessage(name, obj, parameter));
	}
	/**
	 * 发送消息
	 * @param msg 消息映射消息
	 * @return
	 */
	public static Object sendMethodMessage(EMethodMessage msg){
		return def.sendMethodMessages(msg);
	}
	/**
	 * 将 obj 对象的实现消息机制的方法全部注册为消息映射
	 * @param name 前缀名
	 * @param obj 对象
	 * @return
	 */
	public static boolean addMethodMap(String name,Object obj){
		Method[] ms = obj.getClass().getMethods();
		for(Method m:ms){
			Class[] cs = m.getParameterTypes();
			if(cs.length == 1 && cs[0].equals(EMethodMessage.class)){
				addMethodMap(name+":"+m.getName(), obj, m.getName());
			}
		}
		return true;
	}
	/**
	 * 添加消息映射
	 * @param name 触发名
	 * @param obj 触发目标对象
	 * @param methodName 触发目标方法名
	 * @return
	 */
	public static boolean addMethodMap(String name,Object obj,String methodName){
		return def.addMethodMaps(name, obj, methodName);
	}
	/**
	 * 删除所有的消息映射
	 * @return
	 */
	public static boolean removeAllMethodMap(){
		return def.removeAlls();
	}
	/**
	 * 获得消息映射列表
	 * @return
	 */
	public static String getMethodMapList(){
		return def.getMethodMapLists();
	}
	/**
	 * 删除name对应的消息映射
	 * @param name
	 * @return
	 */
	public static boolean removeMethodMap(String name){
		return def.removes(name);
	}
	/**
	 * 获得消息映射管理对象
	 * @return
	 */
	public static EMethodMapManage createEMethodMapManage(){
		if(def==null){
			def = new EMethodMapManage();
		}
		return def;
	}
	/**
	 * 是否输出消息信息
	 * @return
	 */
	public static boolean isMessageInfo(){
		return isMessageInfo;
	}
	/**
	 * 设置是否输出消息信息
	 * @param b
	 */
	public static void setMessageInfo(boolean b){
		isMessageInfo = b;
	}
	
	private EMethodMapManage(){
		_eventMap = new HashMap<String,EMethodMap>();
	}
	/**
	 * 传递消息
	 * @param name {@link String} 映射名
	 * @param msg  {@link EMethodMessage} 消息对象
	 * @return
	 */
	private Object sendMethodMessages(EMethodMessage msg){
		if(msg.getName() == null || msg.getName().equals(""))
			return false;
		EMethodMap m = _eventMap.get(msg.getName());
		if(m==null) return false;
		try {
			return m.invoke(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 添加一个消息映射
	 * @param name {@link String} 映射名
	 * @param obj {@link Object} 方法所在对象
	 * @param methodName {@link String} 方法名
	 * @return
	 */
	private boolean addMethodMaps(String name,Object obj,String methodName){
		if(obj==null || name == null || methodName == null || methodName.trim().equals("") || name.trim().equals(""))
			return false;
		Method method;
		try {
			method = obj.getClass().getMethod(methodName, EMethodMessage.class);
			_eventMap.put(name, new EMethodMap(name,obj, method));
			return true;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 删除所有消息映射
	 * @return
	 */
	private boolean removeAlls(){
		_eventMap.clear();
		return true;
	}
	/**
	 * 删除一个消息映射
	 * @param name {@link String} 映射名
	 * @return
	 */
	private boolean removes(String name){
		_eventMap.remove(name);
		return true;
	}

	private String getMethodMapLists(){
		String str = "\r\nMethodMapList:";
		for(String s:_eventMap.keySet()){
			EMethodMap m = _eventMap.get(s);
			str = str+"\r\n\t"+s+"\t=>\t"+m.getObj().getClass().getCanonicalName()+"."+m.getMethod().getName();
		}
		return str;
	}
}
