package ekaiser.nzlov.plugin.talk;

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
 * 即时聊天 离线信息
 * @author nzlov
 *
 */
public class TalkPlugin extends IEPlugin{
	private static Logger logger = LogManager.getLogger("TalkPlugin");

	@Override
	public Object start() {
		// TODO Auto-generated method stub
    	logger.entry();
		EMethodMapManage.addMethodMap("Talk", this);

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
		EMethodMapManage.removeMethodMap("Talk:sendMessage");
    	logger.exit();
		return null;
	}
	
	
	public void sendMessage(EMethodMessage msg) throws UnsupportedEncodingException{
    	logger.entry();
		IoSession session = (IoSession)msg.getObject();
		NotepadData data = (NotepadData)msg.getParameter();

		try{
			String re = data.getDataBlock(1, "123").getDataToString();
			Long reid = (Long)EMethodMapManage.sendMethodMessage("Login:getUserSessionLong", session, re);
		
			IoSession resession = session.getService().getManagedSessions().get(reid);
		
			resession.write(data);
		}catch(Exception e){
			data = new NotepadData("Error");
			data.putString("2", "123");
			session.write(data);
		}
    	logger.exit();
	}
	
}
