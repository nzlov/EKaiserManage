package ekaiser.nzlov.plugin.user;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import ekaiser.nzlov.methodmap.EMethodMapManage;
import ekaiser.nzlov.methodmap.EMethodMessage;
import ekaiser.nzlov.notepad.data.NotepadData;
import ekaiser.nzlov.plugins.IEPlugin;
/**
 * 6、	修改密码：User:pwd
 * 0：用户名 2：新密码（必须md5加密）
 * Re：0|1
 *
 */
public class UserPwdPlugin extends IEPlugin{
	private static Logger logger = LogManager.getLogger("UserPwdPlugin");

	private final static String v="1.0";
	@Override
	public Object start() {
		// TODO Auto-generated method stub
    	logger.entry();
    	
    	setVersion(v);
    	
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
		EMethodMapManage.removeMethodMap("User:pwd");
    	logger.exit();
		return null;
	}
	
	
	public void pwd(EMethodMessage msg) throws UnsupportedEncodingException{
    	logger.entry();
		IoSession session = (IoSession)msg.getObject();
		NotepadData data = (NotepadData)msg.getParameter();
		String sname = (String)session.getAttribute("name");
		String login = data.getDataBlock(0, "123").getDataToString();
		int limit = (int)EMethodMapManage.sendMethodMessage("Limit:isLimit", session, data);
		switch(limit){
		case 1:break;
		case 2:
			if(sname.equals(login)){
				break;
			}
		default:
			data.clean();
			data.setName("Error","123");
			data.putString("3", "123");
			session.write(data);
			logger.exit();
			return;
		}
		String pwd = data.getDataBlock(1, "123").getDataToString();
		
		String sql = "update user_table u set u.user_password='"+pwd+"' where u.loginname='"+login+"';";
		int i = (int)EMethodMapManage.sendMethodMessage("Database:update", this, sql);
		
		data.clean();
		data.putString(i+"", "123");
		
		session.write(data);
    	logger.exit();
	}
	
}
