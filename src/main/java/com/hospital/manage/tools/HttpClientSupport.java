package com.hospital.manage.tools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientSupport {
    public static String doPost(String url, String jsonStr, String charset) throws Exception {
        HttpClient httpClient;
        HttpPost httpPost;
        String result = null;

        httpClient = HttpClients.createDefault();
        httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonStr, charset);

        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost);

        if (response != null) {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, charset);
            }
        }

        return result;
    }
}
