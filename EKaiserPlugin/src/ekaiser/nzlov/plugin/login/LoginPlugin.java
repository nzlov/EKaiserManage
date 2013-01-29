package ekaiser.nzlov.plugin.login;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
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

	private final static String v="1.1";
	
	private HashMap<String, Long> sessionMap = null;

	@Override
	public Object start() {
		// TODO Auto-generated method stub
    	logger.entry();
    	
    	setVersion(v);
    	
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
		EMethodMapManage.removeMethodMap("Login:getOnlineUser");
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
			
			if(sessionMap.containsKey(user)){
				logout(user,session);
			}
			session.setAttribute("name", user);
			data.clean();
			data.putString("login OK!", "123");
			sessionMap.put(user, session.getId());
			session.write(data);
			
			data.clean();
			data.setName("Login:enter", "123");
			data.putString(user, "123");
			
			EMethodMapManage.sendMethodMessage("ServerMsg:sendMessage", session, data);
			
		}else{
			data.clean();
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
    	logger.entry();
		String password = "";
		String id = "";
		int state = 0;
		String sql = "select * from user_table where user_loginname='" + user + "';";
		Object[] objs = (Object[])EMethodMapManage.sendMethodMessage("Database:query", this, sql);
		ResultSet rs = (ResultSet) objs[0];
		while (rs.next()) {
			id = rs.getString("id");
			password = rs.getString("user_password");
			state = rs.getInt("user_state");
		}
		EMethodMapManage.sendMethodMessage("Database:closeJDBC", this, objs);
		if (pwd.equals(password) && state == 1) {
			session.setAttribute("guid", id);
	    	logger.exit();
			return true;
		} else{
	    	logger.exit();
			return false;
		}
	}
	
	private void logout(String name,IoSession session){
    	logger.entry();
    	Long reid = sessionMap.get(name);
		NotepadData data = new NotepadData("Login:exit");
		data.putString(name, "123");
		EMethodMapManage.sendMethodMessage("ServerMsg:sendMessage", session, data);
    	IoSession resession = session.getService().getManagedSessions().get(reid);
        CloseFuture closeFuture = resession.close(true);
        closeFuture.addListener(new IoFutureListener<IoFuture>(){
            public void operationComplete(IoFuture future) {
                if (future instanceof CloseFuture) {
                    ((CloseFuture) future).setClosed();
                    logger.info("sessionClosed CloseFuture setClosed-->{},", future.getSession().getId());
                }
            }
        });
    	logger.exit();
	}
	
	public Long getUserSessionLong(EMethodMessage msg){
    	logger.entry();
		String user = msg.getParameter().toString();
    	logger.exit();
		return sessionMap.get(user);
	}

	public void closeSession(EMethodMessage msg){
    	logger.entry();
		String user = msg.getParameter().toString();
		sessionMap.remove(user);
		
		NotepadData data = new NotepadData("Login:exit");
		data.putString(user, "123");
		
		EMethodMapManage.sendMethodMessage("ServerMsg:sendMessage", msg.getObject(), data);
    	logger.exit();
	}
	
	
	public void getOnlineUser(EMethodMessage msg){
    	logger.entry();
		IoSession session = (IoSession)msg.getObject();
		NotepadData data = (NotepadData)msg.getParameter();
		
		data.clean();
		
		String[] users = (String[])sessionMap.keySet().toArray();
		
		for(String u:users){
			data.putString(u, "123");
		}
		
		session.write(data);
		
    	logger.exit();
	}
	
}
