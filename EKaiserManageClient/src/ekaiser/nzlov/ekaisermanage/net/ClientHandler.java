package ekaiser.nzlov.ekaisermanage.net;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import ekaiser.nzlov.methodmap.EMethodMapManage;
import ekaiser.nzlov.notepad.data.NotepadData;
 
public class ClientHandler extends IoHandlerAdapter {

	private static Logger logger = LogManager.getLogger("ClientHandler");
    
    public void messageReceived(IoSession session, Object message) throws Exception {
    	logger.entry();
        NotepadData data = (NotepadData)message;
        logger.info("client receive a message is : " + data.getName() + "\tdata:"+data.getDataBlock(0, "123").getDataToString());
        
        EMethodMapManage.sendMethodMessage("showMessage", session, data);
        
        
        logger.exit();
    }
    
    public void messageSent(IoSession session , Object message) throws Exception{
    	logger.entry();
    	logger.info("messageSent 客户端发送消息：" + message);
        logger.exit();
    }
    
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    	logger.entry();
    	logger.info("服务器发生异常： {}", cause.getMessage());
        logger.exit();
    }
}