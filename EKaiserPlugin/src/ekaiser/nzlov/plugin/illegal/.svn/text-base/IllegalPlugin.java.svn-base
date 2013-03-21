package ekaiser.nzlov.plugin.illegal;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import ekaiser.nzlov.methodmap.EMethodMapManage;
import ekaiser.nzlov.methodmap.EMethodMessage;
import ekaiser.nzlov.notepad.data.NotepadData;
import ekaiser.nzlov.plugins.EPluginData;
import ekaiser.nzlov.plugins.IEPlugin;
import ekaiser.nzlov.util.GuidCreator;

public class IllegalPlugin extends IEPlugin{
	private static Logger logger = LogManager.getLogger("IllegalPlugin");
	

	private final static String v="1.0";
	
	@Override
	public Object start() {
		// TODO Auto-generated method stub
    	logger.entry();    	    	
		EMethodMapManage.addMethodMap("Illegal", this);
    	logger.exit();
		return true;
	}

	@Override
	public Object stop() {
		// TODO Auto-generated method stub
    	logger.entry();
		EMethodMapManage.removeMethodMap("Illegal:upLoad");
    	logger.exit();
		return null;
	}

	@Override
	public Object start(HashMap<String, Object> pa) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 
	 * NotepadData
	 * 0    info
	 * 1    data
	 * @param msg
	 * @throws SQLException
	 * @throws UnsupportedEncodingException 
	 */
	public void upLoad(EMethodMessage msg) throws SQLException, UnsupportedEncodingException{
    	logger.entry();
    	IoSession session = (IoSession)msg.getObject();
		NotepadData data = (NotepadData)msg.getParameter();

		String info = data.getDataBlock(0, "123").getDataToString();
		byte[] imgdata = data.getDataBlock(1, "123").getData();
		
		GuidCreator g = new GuidCreator();
		
		String imgguid = g.createNewGuid(GuidCreator.AfterMD5);
		
		String imgpath = (String)EMethodMapManage.sendMethodMessage("SysConfig:getConfig", this, "imagepath");
		
		imgpath = imgpath + "/"+imgguid+".png";
		
		boolean isSave = (boolean)EMethodMapManage.sendMethodMessage("ImageSave:save", this, new Object[]{imgpath,imgdata});
		
		if(isSave){
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			
			String sql = "insert into tb_illegal(id,user_id,info,date) " +
											"values('"+imgguid+"','"+session.getAttribute("guid")+"','"+info+"','"+date+"');";
			int  i = (int)EMethodMapManage.sendMethodMessage("Database:update", this, sql);
			
			
			
			
			
			data.clean();
			data.putString( i+"", "123");
			data.putString(info+"", "123");
			session.write(data);
		}else{
			data.clean();
			data.putString("0", "123");
			session.write(data);
		}
		
		
    	logger.exit();
	}

	@Override
	public EPluginData createReplaceData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void replaceData(EPluginData arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
