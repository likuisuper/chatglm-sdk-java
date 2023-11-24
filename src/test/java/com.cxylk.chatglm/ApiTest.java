package com.cxylk.chatglm;

import com.alibaba.fastjson.JSON;
import com.cxylk.chatglm.model.*;
import com.cxylk.chatglm.session.Configuration;
import com.cxylk.chatglm.session.OpenAiSession;
import com.cxylk.chatglm.session.defaults.DefaultOpenAiSessionFactory;
import com.cxylk.chatglm.utils.BearerTokenUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * @author likui
 * @description
 * @date 2023/11/22 16:37
 **/
@Slf4j
public class ApiTest {
    @Test
    public void test_curl() {
        Configuration configuration = new Configuration();
        configuration.setApiSecretKey("4296b8c7fe47ddcaeb9d08e6bf809c1e.HOpbPzacZ8qc1UoH");

        //获取token
        String token = BearerTokenUtils.getToken(configuration.getApiKey(), configuration.getApiSecret());
        log.info("1. 在智谱Ai官网，申请 ApiSeretKey 配置到此测试类中，替换 setApiSecretKey 值。 https://open.bigmodel.cn/usercenter/apikeys");
        log.info("2. 运行 test_curl 获取 token：{}", token);
        log.info("3. 将获得的 token 值，复制到 curl.sh 中，填写到 Authorization: Bearer 后面");
        log.info("4. 执行完步骤3以后，可以复制直接运行 curl.sh 文件，或者复制 curl.sh 文件内容到控制台/终端/ApiPost中运行");
    }

    private OpenAiSession openAiSession;

    @Before
    public void open_session() {
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://open.bigmodel.cn/");
        configuration.setApiSecretKey("4296b8c7fe47ddcaeb9d08e6bf809c1e.HOpbPzacZ8qc1UoH");
        DefaultOpenAiSessionFactory sessionFactory = new DefaultOpenAiSessionFactory(configuration);
        openAiSession = sessionFactory.openSession();
    }

    @Test
    public void test_completions() throws InterruptedException {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.CHATGLM_TURBO);
        request.setIncremental(false);
        request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>() {
            {
                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content("写个Java冒泡排序")
                        .build());

//                add(ChatCompletionRequest.Prompt.builder()
//                        .role(Role.user.getCode())
//                        .content("Okay")
//                        .build());
//
//                /* system 和 user 为一组出现。如果有参数类型为 system 则 system + user 一组一起传递。*/
//                add(ChatCompletionRequest.Prompt.builder()
//                        .role(Role.system.getCode())
//                        .content("1+1=2")
//                        .build());
//
//                add(ChatCompletionRequest.Prompt.builder()
//                        .role(Role.user.getCode())
//                        .content("Okay")
//                        .build());
//
//                add(ChatCompletionRequest.Prompt.builder()
//                        .role(Role.user.getCode())
//                        .content("1+2")
//                        .build());
            }
        });
        openAiSession.completions(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                ChatCompletionResponseSse response = JSON.parseObject(data, ChatCompletionResponseSse.class);
                log.info("测试结果 onEvent：{}", response.getData());
                // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                if (EventType.finish.getCode().equals(type)) {
                    log.info("[输出结束] Tokens {}", JSON.toJSONString(response.getMeta()));
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                log.info("对话完成");
            }
        });

        new CountDownLatch(1).await();
    }

    @Test
    public void test_future() throws ExecutionException, InterruptedException {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.CHATGLM_TURBO);
        request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>() {
            {
                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content("用Java实现一个冒泡排序")
                        .build());
            }
        });
        CompletableFuture<String> future = openAiSession.completions(request);
        String result = future.get();
        log.info("测试结果：{}",result);
    }

    @Test
    public void test_sync(){
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.CHATGLM_TURBO);
        request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>() {
            {
                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content("用Java实现一个冒泡排序")
                        .build());
            }
        });
        ChatCompletionResponse response = openAiSession.completionsSync(request);
        log.info("测试结果：{}",JSON.toJSONString(response));
    }

    @Test
    public void test_async(){
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.CHATGLM_TURBO);
        request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>() {
            {
                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content("用Java实现一个冒泡排序")
                        .build());
            }
        });
        ChatCompletionResponse response = openAiSession.completionsAsync(request);
        log.info("测试结果：{}",JSON.toJSONString(response));
    }

    @Test
    public void test_async_result(){
        //taskId从test_async方法获取
        ChatCompletionResponse response = openAiSession.asyncInvokeResult("507817006386831538142533518177035341");
        log.info("测试结果：{}",JSON.toJSONString(response));
    }
}
