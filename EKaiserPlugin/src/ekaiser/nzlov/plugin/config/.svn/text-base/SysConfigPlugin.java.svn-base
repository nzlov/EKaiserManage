package ekaiser.nzlov.plugin.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ekaiser.nzlov.methodmap.EMethodMapManage;
import ekaiser.nzlov.methodmap.EMethodMessage;
import ekaiser.nzlov.plugins.EPluginData;
import ekaiser.nzlov.plugins.IEPlugin;

public class SysConfigPlugin extends IEPlugin{
	private static Logger logger = LogManager.getLogger("SysConfigPlugin");
	
	private HashMap<String, String> configMap = null;


	private final static String v="1.0";
	
	@Override
	public Object start() {
		// TODO Auto-generated method stub
    	logger.entry();
    	configMap = new HashMap<String,String>();
    	    	
		EMethodMapManage.addMethodMap("SysConfig", this);
    	logger.exit();
		return true;
	}

	@Override
	public Object stop() {
		// TODO Auto-generated method stub
    	logger.entry();
		EMethodMapManage.removeMethodMap("SysConfig:getConfig");
    	logger.exit();
		return null;
	}

	@Override
	public Object start(HashMap<String, Object> pa) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getConfig(EMethodMessage msg) throws SQLException{
    	logger.entry();
		String key = msg.getParameter().toString();
		String value = configMap.get(key);
		if(value==null){
			String sql = "select * from tb_config;";
			Object[] objs = (Object[])EMethodMapManage.sendMethodMessage("Database:query", this, sql);
			ResultSet rs = (ResultSet) objs[0];
			while (rs.next()) {
				value = rs.getString(key);
			}
			EMethodMapManage.sendMethodMessage("Database:closeJDBC", this, objs);
			configMap.put(key, value);
		}
    	logger.exit();
		return value;
	}

	@Override
	public EPluginData createReplaceData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void replaceData(EPluginData arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
