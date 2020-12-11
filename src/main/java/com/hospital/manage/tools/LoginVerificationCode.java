package com.hospital.manage.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class LoginVerificationCode {
    public Map<String, Object> sendLoginVerificationCode(String url, String phone) throws Exception {
        Map<String, Object> res = new HashMap<>(16);
        JSONObject jsonObject = new JSONObject();
        Map<String, String> params = new TreeMap<>();

        params.put("sysCode", "100106");
        params.put("phone", phone);

        String funcode = "500001";

        jsonObject.put("funcode", funcode);
        jsonObject.put("token", "token");
        jsonObject.put("args", DesUtil.encrypt(JSON.toJSONString(params)));

        String str = null;

        try {
            str = HttpClientSupport.doPost(url, jsonObject.toString(), "UTF-8");
            JSONObject resJson = JSON.parseObject(str);

            res.put("code", resJson.getString("code"));
            res.put("message", resJson.getString("message"));
        } catch (Exception e) {
            res.put("code", "1");
            res.put("message", "发送验证码失败");
//            logger.info("发送验证码失败："+e.getMessage());
            e.printStackTrace();
        }

        return res;
    }

    public Map<String, String> validateVerificationCode(String url, String phone, String vcode) throws Exception {
        Map<String, String> res = new HashMap<>(16);
        JSONObject jsonObject = new JSONObject();
        Map<String, String> params = new TreeMap<>();

        params.put("sysCode", "100106");
        params.put("vcode", vcode);
        params.put("phone", phone);

        jsonObject.put("funcode", "500101");
        jsonObject.put("token", "token");
        jsonObject.put("args", DesUtil.encrypt(JSON.toJSONString(params)));

        String str = HttpClientSupport.doPost(url, jsonObject.toString(), "UTF-8");
        JSONObject resJson = JSON.parseObject(str);

        res.put("code", resJson.getString("code"));
        res.put("message", resJson.getString("message"));

        return res;
    }
}
