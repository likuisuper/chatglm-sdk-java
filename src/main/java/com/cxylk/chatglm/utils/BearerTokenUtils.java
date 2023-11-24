package com.cxylk.chatglm.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author likui
 * @description
 * @date 2023/11/22 16:19
 **/
public class BearerTokenUtils {
    /**
     * token过期时间，默认30分钟
     */
    private static final long expireMillis = 30 * 60 * 1000L;

    private static Cache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(expireMillis - (60 * 1000L), TimeUnit.MILLISECONDS)
            .build();

    /**
     * 获取token <a href="https://open.bigmodel.cn/dev/api#nosdk">非sdk用户鉴权</a>
     *
     * @param apiKey    登录创建 ApiKey <a href="https://open.bigmodel.cn/usercenter/apikeys">apikeys</a>，取前半部分
     * @param apiSecret apikey后半部分
     * @return
     */
    public static String getToken(String apiKey, String apiSecret) {
        String token = cache.getIfPresent(apiKey);
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        Algorithm algorithm = Algorithm.HMAC256(apiSecret.getBytes(StandardCharsets.UTF_8));
        //header
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("sign_type", "SIGN");
        //payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("api_key", apiKey);
        payload.put("exp", System.currentTimeMillis() + expireMillis);
        payload.put("timestamp", Calendar.getInstance().getTimeInMillis());
        token = JWT.create().withHeader(header).withPayload(payload).sign(algorithm);
        cache.put(apiKey, token);
        return token;
    }
}
