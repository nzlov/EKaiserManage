package ekaiser.nzlov.methodmap;
/**
 * 消息映射传递消息类
 * @author nzlov
 *
 */
public class EMethodMessage {
	private Object[] parameter;
	private Object obj;
	private String name;
	/**
	 * 构造方法
	 * @param obj {@link Object} 消息发送对象
	 * @param p {@link Object[]} 参数数组
	 */
	public EMethodMessage(String name,Object obj,Object[] p){
		this.obj = obj;
		this.name = name;
		this.parameter = p;
	}
	/**
	 * 构造方法
	 * @param obj {@link Object} 消息发送对象
	 * @param p {@link Object} 参数
	 */
	public EMethodMessage(String name,Object obj,Object p){
		this.obj = obj;
		this.name =name;
		Object[] ps = new Object[1];
		ps[0] = p;
		this.parameter = ps;
	}
	/**
	 * 获得参数数组
	 * @return {@link Object[]} 参数数组
	 */
	public Object[] getParameters(){
		return parameter;
	}
	/**
	 * 获得参数
	 * @return {@link Object} 参数
	 */
	public Object getParameter(){
		return parameter[0];
	}
	/**
	 * 获得消息发送对象
	 * @return
	 */
	public Object getObject(){
		return obj;
	}
	public String getObjClassName(){
		return obj.getClass().getName();
	}
	public String getName(){
		return name;
	}
	private String getParameterString(){
		String str = "";
		for(Object o: parameter){
			if(o!=null)
				str = str+"\r\n\t\t"+o.getClass().getCanonicalName()+" = "+o.toString();
		}
		return str;
	}
	public String toString(){
		return "\r\nEMessage:"+
				"\r\n\tname      \t=\t" + this.name+
				"\r\n\tobj       \t=\t"+this.obj.getClass().getCanonicalName()+
				"\r\n\tparameter:"+getParameterString()+"\r\n";
	}
}
