package com.style.newwork.core;


/**
 * Created by xiajun on 2016/1/15.
 */
public class OkHttpUtil {
    protected static String TAG = "OkHttpUtil";

    static {
    }

    //同步请求
    public static void postAsyn(String url, String params) {
        NetJsonResult result = null;
      /*  Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                String body = "{code:0,body: {name: \"夏军\",phone: \"18202823096\"},msg: \"请求成功\"}";
                result = JSON.parseObject(body, NetJsonResult.class);
            }
            response.body().close();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            result = new NetJsonResult();
            result.setCode(1);
        }*/
    }
}
