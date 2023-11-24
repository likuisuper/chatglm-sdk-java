package com.cxylk.chatglm.session.defaults;

import com.cxylk.chatglm.IOpenApi;
import com.cxylk.chatglm.interceptor.OpenAiHTTPInterceptor;
import com.cxylk.chatglm.session.Configuration;
import com.cxylk.chatglm.session.OpenAiSession;
import com.cxylk.chatglm.session.OpenAiSessionFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author likui
 * @description 会话工厂
 * @date 2023/11/22 20:35
 **/
public class DefaultOpenAiSessionFactory implements OpenAiSessionFactory {
    private final Configuration configuration;

    public DefaultOpenAiSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }
    @Override
    public OpenAiSession openSession() {
        // 1. 日志配置
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(configuration.getLevel());

        // 2. 开启 Http 客户端
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new OpenAiHTTPInterceptor(configuration))
                .connectTimeout(configuration.getConnectTimeout(), TimeUnit.SECONDS)
                .writeTimeout(configuration.getWriteTimeout(), TimeUnit.SECONDS)
                .readTimeout(configuration.getReadTimeout(), TimeUnit.SECONDS)
                .build();

        configuration.setOkHttpClient(okHttpClient);

        // 3. 创建 API 服务
        IOpenApi openAiApi = new Retrofit.Builder()
                .baseUrl(configuration.getApiHost())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(IOpenApi.class);

        configuration.setOpenAiApi(openAiApi);

        return new DefaultOpenAiSession(configuration);
    }
}
