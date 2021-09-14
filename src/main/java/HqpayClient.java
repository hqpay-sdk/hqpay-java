import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HqpayClient {
    private final String appId;
    private final String secret;
    private final String pkcs8PemKey;
    private String host = "https://www.upaypal.cn";
    private ObjectMapper objectMapper = new ObjectMapper();
    private HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(new PoolingHttpClientConnectionManager()).build();

    public HqpayClient(String appId, String secret, String pkcs8PemKey) {
        this.appId = appId;
        this.secret = secret;
        this.pkcs8PemKey = pkcs8PemKey;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String sendList(List<Map<String, Object>> pMaps, String path) throws Exception {
        for (Map<String, Object> map : pMaps) {
            map.put("signature", HqpaySignature.sign(createLinkString(map), pkcs8PemKey));
        }
        return execute(path, pMaps);
    }

    public JsonNode sendListRtjson(List<Map<String, Object>> pMaps, String path) throws Exception {
        for (Map<String, Object> map : pMaps) {
            map.put("signature", HqpaySignature.sign(createLinkString(map), pkcs8PemKey));
        }
        return objectMapper.readTree(execute(path, pMaps));
    }

    public String sendOne(Map<String, Object> pMap, String path) throws Exception {
        pMap.put("signature", HqpaySignature.sign(createLinkString(pMap), pkcs8PemKey));
        return execute(path, pMap);
    }

    public JsonNode sendOneRtjson(Map<String, Object> pMap, String path) throws Exception {
        pMap.put("signature", HqpaySignature.sign(createLinkString(pMap), pkcs8PemKey));
        return objectMapper.readTree(execute(path, pMap));
    }

    private String execute(String path,  Object o) throws Exception {
        HttpUriRequest httpRequest = RequestBuilder.create("POST")
                .setUri(new URIBuilder(host).setPathSegments("business-platform.web.services.merchant-api-v3", "rest", "api", "v3", path).build())
                .setEntity(new StringEntity(objectMapper.writeValueAsString(o),Charset.forName("UTF-8")))
                .build();
        httpRequest.setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        httpRequest.setHeader(new BasicHeader(HttpHeaders.AUTHORIZATION, secret));
        httpRequest.setHeader(new BasicHeader("AppId", appId));
        return EntityUtils.toString(httpClient.execute(httpRequest).getEntity(),"UTF-8");
    }


    private static String createLinkString(Map<String, Object> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr.append(key).append("=").append(value);
            } else {
                prestr.append(key).append("=").append(value).append("&");
            }
        }
        return prestr.toString();
    }

}
