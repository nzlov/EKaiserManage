package ekaiser.nzlov.plugin.database;

import java.beans.PropertyVetoException;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import ekaiser.nzlov.methodmap.EMethodMapManage;
import ekaiser.nzlov.methodmap.EMethodMessage;
import ekaiser.nzlov.plugins.IEPlugin;
/**
 * 提供数据库连接
 * @author nzlov
 *
 */
public class DatabasePlugin extends IEPlugin{
	private static Logger logger = LogManager.getLogger("DatabasePlugin");
	private ComboPooledDataSource cpds=new ComboPooledDataSource();
	@Override
	public Object start() {
		// TODO Auto-generated method stub
    	logger.entry();
		EMethodMapManage.addMethodMap("Database", this);

		try {
			
			SAXReader sax = new SAXReader();
			
			Document document = sax.read(new File("config/database.xml"));
			
			Element root = document.getRootElement();
			
			String driverClass = root.elementText("DriverClass");
			String jdbcUrl = root.elementText("JdbcUrl");
			String user = root.elementText("User");
			String pwd = root.elementText("Password");
			Integer initialPoolSize = Integer.parseInt(root.elementText("InitialPoolSize"));
			Integer minPoolSize = Integer.parseInt(root.elementText("MinPoolSize"));
			Integer maxPoolSize = Integer.parseInt(root.elementText("MaxPoolSize"));
			Integer maxIdleTime = Integer.parseInt(root.elementText("MaxIdleTime"));
			
			// 驱动器
			cpds.setDriverClass(driverClass);
			// 数据库url
			cpds.setJdbcUrl(jdbcUrl);
			//用户名
			cpds.setUser(user);
			//密码
			cpds.setPassword(pwd);
			//初始化连接池的大小
			cpds.setInitialPoolSize(initialPoolSize);
			//最小连接数
			cpds.setMinPoolSize(minPoolSize);
			//最大连接数
			cpds.setMaxPoolSize(maxPoolSize);
			//最大连接时间
			cpds.setMaxIdleTime(maxIdleTime);
			
		}catch (DocumentException e) {
			// TODO Auto-generated catch block
			logger.catching(e);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			logger.catching(e);
		}
		
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
		EMethodMapManage.removeMethodMap("Database:update");
		EMethodMapManage.removeMethodMap("Database:query");
		EMethodMapManage.removeMethodMap("Database:closeJDBC");
    	logger.exit();
		return null;
	}
	
	/**
	 * 增删改调用语句 
	 * @param msg
	 * @return
	 * @throws SQLException 
	 */
	public int update(EMethodMessage msg) throws SQLException{
    	logger.entry();
		String sql = (String)msg.getParameter();
		Connection conn = cpds.getConnection();
		Statement stmt =  (Statement) conn.createStatement();
		int i = stmt.executeUpdate(sql);
		closeJDBC(null, stmt, conn);
    	logger.exit();
		return i;
	}
	
	public Object[] query(EMethodMessage msg) throws SQLException{
    	logger.entry();
		String sql = (String)msg.getParameter();
		Connection conn = cpds.getConnection();
		Statement stmt =  (Statement) conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
    	logger.exit();
		return new Object[]{rs,stmt,conn};
	}
	
	public void closeJDBC(EMethodMessage msg){
    	logger.entry();
		ResultSet rs = (ResultSet) msg.getParameters()[0];
		Statement st = (Statement) msg.getParameters()[1];
		Connection con = (Connection) msg.getParameters()[2];
		
		closeJDBC(rs, st, con);
    	logger.exit();
	}
	
	/**
	 *  用于关闭数据库的关闭
	 * @param rs ResultSet对象
	 * @param st Statement对象
	 * @param con Connection对象
	 */
	private static void  closeJDBC(ResultSet rs,Statement st,Connection con){
    	logger.entry();
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.catching(e);
			}
		}
		if(st!=null){
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.catching(e);
			}
		}
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.catching(e);
			}
		}
    	logger.exit();
	}
	
}