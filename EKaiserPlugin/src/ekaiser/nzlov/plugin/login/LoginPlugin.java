package ekaiser.nzlov.plugin.login;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import ekaiser.nzlov.methodmap.EMethodMapManage;
import ekaiser.nzlov.methodmap.EMethodMessage;
import ekaiser.nzlov.notepad.data.NotepadData;
import ekaiser.nzlov.plugins.IEPlugin;
/**
 * 登录网关
 * @author nzlov
 *
 */
public class LoginPlugin extends IEPlugin{
	private static Logger logger = LogManager.getLogger("LoginPlugin");
	
	
	private HashMap<String, Long> sessionMap = null;

	@Override
	public Object start() {
		// TODO Auto-generated method stub
    	logger.entry();
    	
    	sessionMap = new HashMap<String,Long>();
    	
		EMethodMapManage.addMethodMap("Login", this);
    	logger.exit();
		return true;
	}

	@Override
	public Object start(HashMap<String, Object> pa) {
		// TODO Auto-generated method stub
    	logger.entry();
    	logger.exit();
		return null;
	}

	@Override
	public Object stop() {
		// TODO Auto-generated method stub
    	logger.entry();
		EMethodMapManage.removeMethodMap("Login:login");
		EMethodMapManage.removeMethodMap("Login:getUserSessionLong");
		EMethodMapManage.removeMethodMap("Login:closeSession");
    	logger.exit();
		return null;
	}
	
	
	public void login(EMethodMessage msg) throws UnsupportedEncodingException, SQLException{
    	logger.entry();
		IoSession session = (IoSession)msg.getObject();
		NotepadData data = (NotepadData)msg.getParameter();

		String user = data.getDataBlock(0, "123").getDataToString();
		String pwd = data.getDataBlock(1, "123").getDataToString();
		
		if(islogin(user, pwd,session)){
			session.setAttribute("name", user);
			data = new NotepadData("login");
			data.putString("login OK!", "123");
			sessionMap.put(user, session.getId());
			session.write(data);
		}else{
			data = new NotepadData("login");
			data.putString("login NO!", "123");
			session.write(data);
		}
		
    	logger.exit();
	}

	/**
	 * 判断是否成功登陆，并更显数据库，用户在线列表，离线列表
	 * 
	 * @param enmsg
	 * @return
	 * @throws SQLException
	 */
	private boolean islogin(String user ,String pwd,IoSession session) throws SQLException{
		String password = "";
		int state = 0;
		int limit = 0;
		String sql = "select user_password,user_state,user_limit from user_table where user_loginname='" + user + "';";
		Object[] objs = (Object[])EMethodMapManage.sendMethodMessage("Database:query", this, sql);
		ResultSet rs = (ResultSet) objs[0];
		while (rs.next()) {
			password = rs.getString("user_password");
			state = rs.getInt("user_state");
			limit = rs.getInt("user_limit");
		}
		EMethodMapManage.sendMethodMessage("Database:closeJDBC", this, objs);
		if (pwd.equals(password) && state == 1) {
			session.setAttribute("limit", limit);
			return true;
		} else
			return false;
	}
	
	
	public Long getUserSessionLong(EMethodMessage msg){
		String user = msg.getParameter().toString();
		return sessionMap.get(user);
	}

	public void closeSession(EMethodMessage msg){
		String user = msg.getParameter().toString();
		sessionMap.remove(user);
	}
}
