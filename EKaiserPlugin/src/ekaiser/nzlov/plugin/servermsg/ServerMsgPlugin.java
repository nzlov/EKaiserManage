package ekaiser.nzlov.plugin.servermsg;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import ekaiser.nzlov.methodmap.EMethodMapManage;
import ekaiser.nzlov.methodmap.EMethodMessage;
import ekaiser.nzlov.notepad.data.NotepadData;
import ekaiser.nzlov.plugins.IEPlugin;
/**
 * 服务器消息
 * @author nzlov
 *
 */
public class ServerMsgPlugin extends IEPlugin{
	private static Logger logger = LogManager.getLogger("ServerMsgPlugin");

	@Override
	public Object start() {
		// TODO Auto-generated method stub
    	logger.entry();
		EMethodMapManage.addMethodMap("ServerMsg", this);

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
		EMethodMapManage.removeMethodMap("ServerMsg:sendMessage");
    	logger.exit();
		return null;
	}
	
	
	public void sendMessage(EMethodMessage msg) throws UnsupportedEncodingException{
    	logger.entry();
		IoSession session = (IoSession)msg.getObject();
		NotepadData data = (NotepadData)msg.getParameter();

	    Collection<IoSession> sessions = session.getService().getManagedSessions().values();
	    // 向所有客户端发送数据
	    for (IoSession sess : sessions) {
	    	sess.write(data);
	    }
		
    	logger.exit();
	}
	
}
