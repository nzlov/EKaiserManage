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
 * 2、	查看用户信息：User:Info
 * 				0：用户名
 * 			Re：User:showInfo 姓名 身份证 
 * @author nzlov
 *
 */
public class UserInfoPlugin extends IEPlugin{
	private static Logger logger = LogManager.getLogger("UserInfoPlugin");

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
		EMethodMapManage.removeMethodMap("User:info");
    	logger.exit();
		return null;
	}
	
	
	public void info(EMethodMessage msg) throws SQLException, UnsupportedEncodingException{
    	logger.entry();
		IoSession session = (IoSession)msg.getObject();
		NotepadData data = (NotepadData)msg.getParameter();
		String name = data.getDataBlock(0, "123").getDataToString();
		String sql = "SELECT * FROM user_info_table ,user_table WHERE user_table.user_loginname ='"+name+"';";
		Object[] objs = (Object[])EMethodMapManage.sendMethodMessage("Database:query", this, sql);
		ResultSet rs = (ResultSet)objs[0];
		data.clean();
		while (rs.next()) {
			data.putString(rs.getString("user_info_name"),"123");
			data.putString(rs.getString("user_info_id"),"123");
			data.putString(rs.getInt("user_info_sex")+"","123");
			data.putString(rs.getInt("user_info_age")+"","123");
			data.putString(rs.getString("user_info_photo"),"123");
			data.putString(rs.getString("user_info_mobile"),"123");
			data.putString(rs.getString("user_info_email"),"123");
			data.putString(rs.getString("user_info_addr"),"123");
			data.putString(rs.getString("user_info_photo"),"123");
		}
		
		EMethodMapManage.sendMethodMessage("Database:closeJDBC", this, objs);
		
		session.write(data);
		
    	logger.exit();
	}
	
}
