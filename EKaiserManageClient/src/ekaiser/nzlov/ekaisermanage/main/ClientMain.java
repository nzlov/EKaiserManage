package ekaiser.nzlov.ekaisermanage.main;

import java.net.InetSocketAddress;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import ekaiser.nzlov.ekaisermanage.frame.MainFrame;
import ekaiser.nzlov.ekaisermanage.net.ClientHandler;

public class ClientMain {
	private SocketConnector connector;
    private ConnectFuture future;
    private IoSession session;
	
    private MainFrame mainFrame = null;
    
    public ClientMain(){
    	mainFrame = new MainFrame(this);
    }
    
    public boolean connect() {
    	
    	
    	
    	
        // 创建一个socket连接
        connector = new NioSocketConnector();
        // 设置链接超时时间
        connector.setConnectTimeoutMillis(3000);
        // 获取过滤器链
        DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
        // 添加编码过滤器 处理乱码、编码问题
        filterChain.addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
 
        /*
        // 日志
        LoggingFilter loggingFilter = new LoggingFilter();
        loggingFilter.setMessageReceivedLogLevel(LogLevel.INFO);
        loggingFilter.setMessageSentLogLevel(LogLevel.INFO);
        filterChain.addLast("loger", loggingFilter);*/
 
        // 消息核心处理器
        connector.setHandler(new ClientHandler());
 
        // 连接服务器，知道端口、地址
        future = connector.connect(new InetSocketAddress(3421));
        // 等待连接创建完成
        future.awaitUninterruptibly();
        // 获取当前session
        session = future.getSession();
        return true;
    }
 
    public void setAttribute(Object key, Object value) {
        session.setAttribute(key, value);
    }
 
    public void send(Object message) {
        session.write(message);
    }
 
    public boolean close() {
        CloseFuture future = session.getCloseFuture();
        future.awaitUninterruptibly(1000);
        connector.dispose();
        return true;
    }
 
    public SocketConnector getConnector() {
        return connector;
    }
 
    public IoSession getSession() {
        return session;
    }
	
	public static void main(String[] args){
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
	    SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		        try {
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceAutumnLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceChallengerDeepLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceCremeCoffeeLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceDustCoffeeLookAndFeel"); 
		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceDustLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceEmeraldDuskLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGeminiLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteGlassLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceMagellanLookAndFeel"); 
//		        	2UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceMarinerLookAndFeel"); 
//		        3	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceMistAquaLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceMistSilverLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceNebulaLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceOfficeBlack2007LookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceOfficeBlue2007LookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceOfficeSilver2007LookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceSaharaLookAndFeel"); 
//		        	UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceTwilightLookAndFeel"); 

//
		        } catch (Exception e) {
		        	//JOptionPane.showMessageDialog(null, "Substance Graphite failed to initialize");
		        }
				new ClientMain().connect();;
		      }
		    });

	}
}
