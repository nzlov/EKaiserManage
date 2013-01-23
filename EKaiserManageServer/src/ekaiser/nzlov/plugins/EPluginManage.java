package ekaiser.nzlov.plugins;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 插件管理器
 * @author nzlov
 *
 */
public class EPluginManage {
	private static EPluginManage def = null;
	/**
	 * 默认的配置文件路径
	 */
	private static final String CONFIGF = "plugins/config.xml";
	private static Logger logger = LogManager.getLogger("EPluginManage"); 
	
	/**
	 * 插件列表
	 */
	private HashMap<String, IEPlugin> pluginsMap = null;
	
	/**
	 * 获得插件管理器
	 * @return
	 */
	public static EPluginManage createPluginManage(String cfgPath){
		if(def == null){
			def = new EPluginManage(cfgPath);
		}
		return def;
	}
	
	
	/**
	 * 获得插件管理器
	 * @return
	 */
	public static EPluginManage createPluginManage(){
		if(def == null){
			def = new EPluginManage();
		}
		return def;
	}
	
	private EPluginManage(){
		this(CONFIGF);
	}
	/**
	 * 根据配置文件生成插件管理器
	 * @param cfgPath XML插件配置文件
	 */
	private EPluginManage(String cfgPath){
		
		
		pluginsMap = new HashMap<String,IEPlugin>();
		
		init(cfgPath);
	}
	
	private void init(String cfgPath){
		logger.entry();
		try {
			SAXReader saxReader = new SAXReader();
			Document xmlDocument = saxReader.read(new File(cfgPath));
			Element root = xmlDocument.getRootElement();
			
			
			List<Element> plugins = root.elements("Plugin"); //解析Plugin标签
			
			String pName = "";
//			String pVersion = "0";
			String pJarName = "";
						
			for(Element p:plugins){
				
				pName = p.elementText("Name");
//				pVersion = p.elementText("Version");
				pJarName = p.elementText("Jar");
				
				loadPlugin(pName, pJarName);
			}
			
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			logger.catching(e);
		}
		logger.exit();
	}
	/**
	 * 获得所加载的插件名
	 * @return
	 */
	public String[] getPlugins(){
		return (String[])pluginsMap.keySet().toArray();
	}
	/**
	 * 获得某个插件的版本
	 * @param n
	 * @return
	 */
	public String getPluginVersion(String n){
		if(n!=null && !n.trim().equals("")){
			IEPlugin p = pluginsMap.get(n);
			return p.getVersion();
		}
		return null;
	}
	/**
	 * 移除某个插件
	 * @param n
	 * @return
	 */
	public boolean stopPlugin(String n){
		logger.entry();
		if(n!=null && !n.trim().equals("")){
			IEPlugin p = pluginsMap.get(n);
			p.stop();
			pluginsMap.remove(n);
			p = null;
			logger.exit();
			return true;
		}
		logger.exit();
		return false;
	}
	/**
	 * 加载插件
	 * @param n
	 * @return
	 */
	public boolean loadPlugin(String n,String jar){
		logger.entry();
		if(n!=null && !n.trim().equals("") && jar!=null && !jar.trim().equals("") ){
			try {
				IEPlugin plugin = EPluginLoader.loadPlugin(n, jar);
				plugin.setManage(def);
				pluginsMap.put(n, plugin);
				plugin.start();
				logger.exit();
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.catching(e);
				logger.exit();
				return false;
			}
		}
		logger.exit();
		return false;
	}
	
}
