package ekaiser.nzlov.plugin.image;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ekaiser.nzlov.methodmap.EMethodMapManage;
import ekaiser.nzlov.methodmap.EMethodMessage;
import ekaiser.nzlov.plugins.EPluginData;
import ekaiser.nzlov.plugins.IEPlugin;

public class ImageSavePlugin extends IEPlugin{
	private static Logger logger = LogManager.getLogger("ImageSavePlugin");
	

	private final static String v="1.1";
	
	@Override
	public Object start() {
		// TODO Auto-generated method stub
    	logger.entry();    	    	
		EMethodMapManage.addMethodMap("ImageSave", this);
    	logger.exit();
		return true;
	}

	@Override
	public Object stop() {
		// TODO Auto-generated method stub
    	logger.entry();
		EMethodMapManage.removeMethodMap("ImageSave:save");
    	logger.exit();
		return null;
	}

	@Override
	public Object start(HashMap<String, Object> pa) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean save(EMethodMessage msg) throws SQLException{
    	logger.entry();
		String name = (String)msg.getParameters()[0];
		byte[] data = (byte[])msg.getParameters()[1];
		FileOutputStream out = null ;
		try {
			out = new FileOutputStream(name);
			out.write(data);
			out.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			if(out!=null){
				try {
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				out = null;
			}
		}
    	logger.exit();
    	return true;
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
