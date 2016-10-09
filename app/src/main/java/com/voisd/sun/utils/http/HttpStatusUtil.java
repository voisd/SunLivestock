package com.voisd.sun.utils.http;



import com.voisd.sun.utils.StringHelper;

import org.json.JSONObject;

/**
 * Created by voisd on 16/5/23.
 */
public class HttpStatusUtil {

    // 得到状态码
    public static boolean getStatus(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            boolean statusCode = jsonObject.getInt("code")==200;

            if (statusCode) {
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }

    /**
     * 得到状态提示
     *
     * @param json
     * @return
     */
    public static String getStatusMsg(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
//            String message = "状态码："+jsonObject.getInt("code") +"\n状态描述："+ jsonObject.getString("msg");

            String message = jsonObject.getString("msg");
            if (!StringHelper.isEmpty(message)) {
                return message;
            }
        } catch (Exception ex) {

            return ex.getMessage();
        }
        return "服务器异常，返回格式不对";
    }

    /**
     * 得到状态提示
     *
     * @param json
     * @return
     */
    public static String getStatusDate(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
//            String message = "状态码："+jsonObject.getInt("code") +"\n状态描述："+ jsonObject.getString("msg");

            String message = jsonObject.getString("data");
            if (!StringHelper.isEmpty(message)) {
                return message;
            }
        } catch (Exception ex) {

            return ex.getMessage();
        }
        return "服务器异常，返回格式不对";
    }



    /**
     * 得到状态异常码
     *
     * @param json
     * @return
     */
    public static int getStatusError(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            int error = jsonObject.getInt("code");

            return error;
        } catch (Exception ex) {

            return 0;
        }
    }

    /**
     * 判断状态是否需要重新登录
     * @param json
     * @return
     */
    public static boolean isRelogin(String json) {

//        int error = getStatusError(json);
//
//        if (error == 100 || error == 103 || error == 104) {
//
//            return true;
//        }

        return false;
    }
}
