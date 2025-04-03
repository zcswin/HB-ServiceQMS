package com.ww.boengongye.utils;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

public class HttpUtil {
    public static String get(String url, Map<String, String> urlParam, Map<String, String> header, boolean ssl) {
        return get(url, urlParam, header, CharEncoding.UTF_8, ssl);
    }

    public static String get(String url, Map<String, String> urlParams, Map<String, String> headers, String charSet, boolean ssl) {
        HttpGet httpGet = new HttpGet(charSet == null ? addParams(url, urlParams) : addParamsWithCharSet(url, urlParams, charSet));
        return getResponse(httpGet, charSet, headers, ssl);
    }

    // 以请求体JSON发送数据
    public static String postJson(String url, Map<String, String> urlParams, Map<String, String> headers, String data, boolean ssl) {
        HttpPost httpPost = new HttpPost(addParams(url, urlParams));
        httpPost.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
        return getResponse(httpPost, CharEncoding.UTF_8, headers, ssl);
    }

    // 以表单的形式发送数据
    public static String postForm(String url, Map<String, String> urlParams, Map<String, String> headers, Map<String, String> data, boolean ssl) {
        HttpPost httpPost = new HttpPost(addParams(url, urlParams));
        ContentType contentType = ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8);
        if (Objects.isNull(headers)) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", contentType.toString());
        List<NameValuePair> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        if (list.size() > 0) {
            UrlEncodedFormEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(list, CharEncoding.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            httpPost.setEntity(entity);
        }
        return getResponse(httpPost, CharEncoding.UTF_8, headers, ssl);
    }

    // 获取响应数据
    private static String getResponse(HttpRequestBase httpRequestBase, String charSet, Map<String, String> headers, boolean ssl) {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = ssl ? getHttpClient() : HttpClients.createDefault();
            httpRequestBase.setConfig(getRequestConfig());
            if (headers.size() > 0) {
                httpRequestBase.setHeaders(getHeaders(headers));
            }
            CloseableHttpResponse response = httpClient.execute(httpRequestBase);
            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                HttpEntity entity = response.getEntity();
                String res = EntityUtils.toString(entity, charSet);
                EntityUtils.consume(entity);
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (Objects.nonNull(httpClient)) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("调用失败");
    }

    private static RequestConfig getRequestConfig() {
        return RequestConfig.custom().setConnectTimeout(6000 * 2).setConnectionRequestTimeout(6000 * 2).setSocketTimeout(6000 * 2).build();
    }

    private static String addParams(String url, Map<String, String> params) {
        return addParamsWithCharSet(url, params, CharEncoding.UTF_8);
    }

    private static String addParamsWithCharSet(String url, Map<String, String> params, String charSet) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append("&").append(entry.getKey()).append("=");
                sb.append(charSet == null ? entry.getValue() : URLEncoder.encode(entry.getValue(), charSet));
            }
            if (!url.contains("?")) {
                sb.deleteCharAt(0).insert(0, "?");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url + sb;
    }

    // HTTPS client 创建
    public static CloseableHttpClient getHttpClient() {
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[]{trustManager}, null);
            return HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(context)).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    // 设置请求头
    private static Header[] getHeaders(Map<String, String> header) {
        if (header.size() == 0) {
            return new Header[]{};
        }
        List<Header> headers = new ArrayList<>();
        for (String key : header.keySet()) {
            headers.add(new BasicHeader(key, header.get(key)));
        }
        return headers.toArray(new Header[]{});
    }

}
