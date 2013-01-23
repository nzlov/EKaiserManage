package ekaiser.nzlov.methodmap;

import java.lang.reflect.Method;

/**
 * 消息映射
 * @author nzlov
 *
 */
public class EMethodMap {
	private String name;
	private Object obj;
	private Method method;
	
	public EMethodMap(String n,Object o,Method m){
		obj = o;
		name = n;
		method = m;
	}

	public Object getObj() {
		return obj;
	}

	public String getName() {
		return name;
	}

	public Method getMethod() {
		return method;
	}
	public Object invoke(EMethodMessage msg) throws Exception{
		return method.invoke(obj, msg);
	}
}
