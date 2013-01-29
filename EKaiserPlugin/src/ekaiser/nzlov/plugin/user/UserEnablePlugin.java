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
 * 启用/禁用用户
 * 
 * @author nzlov
 * 
 */
public class UserEnablePlugin extends IEPlugin {
	private static Logger logger = LogManager.getLogger("UserEnablePlugin");

	private final static String v = "1.0";

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
		EMethodMapManage.removeMethodMap("User:enable");
		logger.exit();
		return null;
	}

	public void enable(EMethodMessage msg) throws UnsupportedEncodingException {
		logger.entry();
		IoSession session = (IoSession) msg.getObject();
		NotepadData data = (NotepadData) msg.getParameter();
		int limit = (int) EMethodMapManage.sendMethodMessage("Limit:isLimit",
				session, data);
		switch (limit) {
		case 1:
			break;
		default:
			data.clean();
			data.setName("Error", "123");
			data.putString("3", "123");
			session.write(data);
			logger.exit();
			return;
		}
		data.clean();
		try{
			String user = data.getDataBlock(0, "123").getDataToString();
			int state = Integer.parseInt(data.getDataBlock(1, "123").getDataToString());
			
			String sql = "update user_table set user_state=" + state +" where user_loginname='" + user+"';";
			int i = (int)EMethodMapManage.sendMethodMessage("Database:update", this, sql);
			
			data.putString(i+"", "123");
		}catch(Exception e){
			logger.catching(e);
			data.putString("0", "123");
		}
		
		session.write(data);
		
		logger.exit();
	}

}
