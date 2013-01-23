package ekaiser.nzlov.loadjar.main;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class LoadJar {
	public static void main(String[] args) throws MalformedURLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		String pluginPath = "plugin/test.jar";
		
		URLClassLoader classLoader = new URLClassLoader(new URL[]{ new File(pluginPath).toURI().toURL()});
				
		Plugin test = (Plugin)classLoader.loadClass("ekaiser.nzlov.loadjar.main.Test").newInstance();
		
		
		test.start();
		
	}
}
