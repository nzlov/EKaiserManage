package ekaiser.nzlov.plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class EPluginLoader {
	
	public static IEPlugin loadPlugin(String className,String jarName) throws Exception{
		URLClassLoader classLoader =  new URLClassLoader(new URL[]{ new File("plugins/"+jarName).toURI().toURL()});
		IEPlugin plugin = (IEPlugin)classLoader.loadClass(className).newInstance();	
		return plugin;
	}
}
