package com.cxylk.chatglm.session.defaults;

import com.alibaba.fastjson.JSON;
import com.cxylk.chatglm.IOpenApi;
import com.cxylk.chatglm.model.*;
import com.cxylk.chatglm.session.Configuration;
import com.cxylk.chatglm.session.OpenAiSession;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @author likui
 * @description sse调用才会涉及到eventSource
 * @date 2023/11/22 20:36
 **/
public class DefaultOpenAiSession implements OpenAiSession {

    /**
     * OpenAi 接口
     */
    private final Configuration configuration;
    /**
     * 工厂事件
     */
    private final EventSource.Factory factory;

    private final IOpenApi openApi;

    public DefaultOpenAiSession(Configuration configuration) {
        this.configuration = configuration;
        this.factory = configuration.createRequestFactory();
        this.openApi = configuration.getOpenAiApi();
    }

    @Override
    public EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener listener) {
        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenApi.v3_completions).replace("{model}", chatCompletionRequest.getModel().getCode()).replace("{invoke_method}", InvokeType.SSE.getCode()))
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();

        // 返回事件结果
        return factory.newEventSource(request, listener);
    }

    @Override
    public CompletableFuture<String> completionsSseAsync(ChatCompletionRequest chatCompletionRequest) {
        //用于执行异步任务并获取结果
        CompletableFuture<String> future = new CompletableFuture<>();
        StringBuffer dataBuffer = new StringBuffer();
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenApi.v3_completions).replace("{model}", chatCompletionRequest.getModel().getCode()).replace("{invoke_method}", InvokeType.SSE.getCode()))
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();
        factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                //sse响应是字符串流格式，eg:
                //id: "fb981fde-0080-4933-b87b-4a29eaba8d17"
                //event: "add"
                //data: "作为一个"
                ChatCompletionResponseSse response = JSON.parseObject(data, ChatCompletionResponseSse.class);
                //type:消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                if (EventType.add.getCode().equals(type)) {
                    dataBuffer.append(response.getData());
                } else if (EventType.finish.getCode().equals(type)) {
                    future.complete(dataBuffer.toString());
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }

            @Override
            public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                future.completeExceptionally(new RuntimeException("Request failure before completion"));
            }
        });

        return future;
    }

    @Override
    public ChatCompletionResponse completionsSync(ChatCompletionRequest chatCompletionRequest) {
        return this.openApi.completions(chatCompletionRequest.getModel().getCode(), InvokeType.INVOKE.getCode(), chatCompletionRequest).blockingGet();
    }

    @Override
    public ChatCompletionResponse completionsAsync(ChatCompletionRequest chatCompletionRequest) {
        return this.openApi.completions(chatCompletionRequest.getModel().getCode(), InvokeType.ASYNC.getCode(), chatCompletionRequest).blockingGet();
    }

    @Override
    public ChatCompletionResponse asyncInvokeResult(String taskId) {
        return this.openApi.asyncInvoke(taskId).blockingGet();
    }
}
