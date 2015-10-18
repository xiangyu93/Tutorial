package util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerUtil {
	JSONArray jsonArray = new JSONArray();
	public static final ServerUtil serverUtil = new ServerUtil();

	public static ServerUtil getServerUtil() {
		return serverUtil;
	}

	public void setJson(JSONObject json) {
		jsonArray.put(json);
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void removeJson() {
		try {
			jsonArray = new JSONArray("[]");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}