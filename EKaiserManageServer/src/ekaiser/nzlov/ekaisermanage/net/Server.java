package ekaiser.nzlov.ekaisermanage.net;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class Server {

	private static Logger logger = LogManager.getLogger("Server"); 
	
	private SocketAcceptor acceptor;
	
	public Server(){
		acceptor = new NioSocketAcceptor();
	}
    public boolean start(int port) {
    	logger.entry();
        DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();
        // 添加编码过滤器 处理乱码、编码问题
        filterChain.addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        
        /*LoggingFilter loggingFilter = new LoggingFilter();
        loggingFilter.setMessageReceivedLogLevel(LogLevel.INFO);
        loggingFilter.setMessageSentLogLevel(LogLevel.INFO);
        // 添加日志过滤器
        filterChain.addLast("loger", loggingFilter);*/
        
        // 设置核心消息业务处理器
        acceptor.setHandler(new ServerHandler());
        // 设置session配置，30秒内无操作进入空闲状态
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
        
        try {
            // 绑定端口3456
            acceptor.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            logger.catching(e);
            logger.exit();
            return false;
        }
        logger.exit();
        return true;
    }
}
