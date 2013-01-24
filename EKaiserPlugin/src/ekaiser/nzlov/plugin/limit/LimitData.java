package ekaiser.nzlov.plugin.limit;

import java.util.HashMap;

public class LimitData {
	private String actor = "";
	private HashMap<String, Integer> limitMap = null;
	LimitData(String id){
		this.actor = id;
		limitMap = new HashMap<String,Integer>();
	}
	
	void addLimits(String str,Integer limit){
		if(str != null && !str.trim().equals("") && limit != null){
			limitMap.put(str,limit);
		}
	}
	
	Integer isLimits(String str){
		if(str != null && !str.trim().equals("")){
			return limitMap.get(str);
		}
		return 0;
	}
}
