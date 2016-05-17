package net.linvx.java.libs.enhance;

import net.linvx.java.libs.utils.MyReflectUtils;
import net.sf.json.JSONObject;

public class BaseBean {
	
	public String toJsonString() {
		return this.toJson().toString();
	}
	
	public JSONObject toJson() {
		return MyReflectUtils.toJson(this, false, false);
	}
	
}
