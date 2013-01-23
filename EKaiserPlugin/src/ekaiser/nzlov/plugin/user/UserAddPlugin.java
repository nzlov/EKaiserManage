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
import ekaiser.nzlov.util.GuidCreator;

/**
 * 		添加用户信息：User:modify
 * 				0：用户名 1：密码 2：姓名 3：身份证 4：性别 5：年龄 6：家电 7：移动电话
 * 				8：E-mail 9：住址 10：照片(文件数据)
 * 			Re：0|1     10：用户名已存在
 *
 * @author nzlov
 *
 */
public class UserAddPlugin extends IEPlugin{
	private static Logger logger = LogManager.getLogger("UserAddPlugin");

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
		EMethodMapManage.removeMethodMap("User:add");
    	logger.exit();
		return null;
	}
	
	
	public void add(EMethodMessage msg) throws UnsupportedEncodingException, SQLException{
    	logger.entry();
		IoSession session = (IoSession)msg.getObject();
		
		NotepadData data = (NotepadData)msg.getParameter();
		String login = data.getDataBlock(0, "123").getDataToString();
		String pwd = data.getDataBlock(1, "123").getDataToString();
		String name = data.getDataBlock(2, "123").getDataToString();
		String id = data.getDataBlock(3, "123").getDataToString();
		int sex = Integer.parseInt(data.getDataBlock(4, "123").getDataToString());
		int age = Integer.parseInt(data.getDataBlock(5, "123").getDataToString());
		String phone = data.getDataBlock(6, "123").getDataToString();
		String moblie = data.getDataBlock(7, "123").getDataToString();
		String email = data.getDataBlock(8, "123").getDataToString();
		String addr  = data.getDataBlock(9, "123").getDataToString();
		String photo = data.getDataBlock(10, "123").getDataToString();
		
		GuidCreator g = new GuidCreator();
		String guid = g.createNewGuid(GuidCreator.BeforeMD5);
		
		
		data.clean();
		
		//检测是否已经存在用户
		String sql = "update user_table u set u.user_loginname='"+login+"' where u.user_loginname='"+login+"';"; 
		
		int i = (int)EMethodMapManage.sendMethodMessage("Database:update", this, sql);
		if(i<0){
			//创建用户
			sql = "update user_info_table ui,user_table u set ui.user_info_name='"+name+"' , ui.user_info_id='"+id+"' , ui.user_info_sex="+sex + " , ui.user_info_age=" + age +
								" , ui.user_info_phone='" + phone+"' ui.user_info_moblie='" + moblie +"' , ui.email='" + email+"' , ui.addr='" + addr +"' ui.photo='" + photo+"' where ui.user_id=u.id and " +
								"u.user_loginname='" + login+"';";
			i = (int)EMethodMapManage.sendMethodMessage("Database:update", this, sql);
			
			//创建用户资料
			sql = "update user_info_table ui,user_table u set ui.user_info_name='"+name+"' , ui.user_info_id='"+id+"' , ui.user_info_sex="+sex + " , ui.user_info_age=" + age +
								" , ui.user_info_phone='" + phone+"' ui.user_info_moblie='" + moblie +"' , ui.email='" + email+"' , ui.addr='" + addr +"' ui.photo='" + photo+"' where ui.user_id=u.id and " +
								"u.user_loginname='" + login+"';";
			i = (int)EMethodMapManage.sendMethodMessage("Database:update", this, sql);
			
			
		}else{
			data.putString("10", "123");
		}
		
		session.write(data);
		
    	logger.exit();
	}
	
}
