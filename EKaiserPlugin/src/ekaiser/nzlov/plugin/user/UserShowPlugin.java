package ekaiser.nzlov.plugin.user;

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
 * 1、	查看所有用户：User:show
 *	Re：NotepadData每一块代表一个用户
 *		用户名
 *		用户名
 *		…
 *
 * @author nzlov
 *
 */
public class UserShowPlugin extends IEPlugin{
	private static Logger logger = LogManager.getLogger("UserShowPlugin");

	@Override
	public Object start() {
		// TODO Auto-generated method stub
    	logger.entry();
		EMethodMapManage.addMethodMap("User", this);

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
		EMethodMapManage.removeMethodMap("User:show");
    	logger.exit();
		return null;
	}
	
	
	public void show(EMethodMessage msg) throws UnsupportedEncodingException, SQLException{
    	logger.entry();
		IoSession session = (IoSession)msg.getObject();
		String sql = "SELECT user_loginname FROM user_table;";
		Object[] objs = (Object[])EMethodMapManage.sendMethodMessage("Database:query", this, sql);
		ResultSet rs = (ResultSet)objs[0];
		
		NotepadData data = new NotepadData("User:show");
		
		while (rs.next()) {
			data.putString(rs.getString("user_loginname"),"123");
		}
		
		EMethodMapManage.sendMethodMessage("Database:closeJDBC", this, objs);
		
		session.write(data);
		
    	logger.exit();
	}
	
}
