package org.json;

/**
 * Created by xiePing on 2018/10/10 0010.
 * Description:
 */
public class JSONObject {
    public JSONObject(String string) {

    }

    public JSONObject() {

    }

    /**
     * JSONArray报文解析容错处理，获取数组中的JSONObject对象
     *
     * @param jsonArray
     *            ：需要解析的jsonArray对象、i：JSONObject在JSONArray中的索引
     * @return JSONObject：通过键名得到的JSONObject对象
     */
    public static JSONObject getJSONObject(JSONArray jsonArray, int i) {
        JSONObject value = new JSONObject();
        try {
            if (jsonArray != null) {
                value = jsonArray.getJSONObject(i);
            }
        } catch (JSONException e) {
        }
        return value;
    }
    /**
     * JSONObject报文解析容错处理，判断报文格式是否正确
     *
     * @param string
     *            ：需要转换为JSONObject的字符串
     * @return JSONObject：通过键名得到的JSONArray对象
     */
    public static JSONObject getJSONObject(String string) {
        JSONObject value = new JSONObject();
        if (string != null && string != "") {
            value = new JSONObject(string);
        }
        return value;
    }
}
