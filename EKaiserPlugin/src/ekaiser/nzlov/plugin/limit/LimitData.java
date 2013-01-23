package ekaiser.nzlov.plugin.limit;

import java.util.Vector;

public class LimitData {
	private int limit = 0;
	private Vector<String> limits = null;
	
	LimitData(int i){
		this.limit = i;
		limits = new Vector<String>();
	}
	
	void addLimits(String str){
		if(str != null && !str.trim().equals("")){
			limits.add(str);
		}
	}
	
	boolean isLimits(String str){
		if(str != null && !str.trim().equals("")){
			return (limits.indexOf(str)>-1);
		}
		return false;
	}
	
	int getLimit(){
		return limit;
	}
}
