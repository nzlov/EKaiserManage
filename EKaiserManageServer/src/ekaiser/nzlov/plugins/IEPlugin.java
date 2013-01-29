package ekaiser.nzlov.plugins;

import java.util.HashMap;

/**
 * 插件接口类
 * @author nzlov
 *
 */
public abstract class IEPlugin {
	public EPluginManage manage = null;
	public String version="1.0";
	
		/**
		 * 开始执行
		 * @return
		 */
		public abstract Object start();
		
		/**
		 * 附带参数执行
		 * @param pa
		 * @return
		 */
		public abstract Object start(HashMap<String, Object> pa);
		
		/**
		 * 停止执行
		 * @return
		 */
		public abstract Object stop();
		
		/**
		 * 设置插件管理器
		 * @param manage
		 */
		public void setManage(EPluginManage manage){
			this.manage = manage;
		}
		
		/**
		 * 获取插件管理器
		 * @return
		 */
		public EPluginManage getManage(){
			return this.manage;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}
}
