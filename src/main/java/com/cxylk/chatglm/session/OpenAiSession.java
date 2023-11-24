package com.cxylk.chatglm.session;

import com.cxylk.chatglm.model.ChatCompletionRequest;
import com.cxylk.chatglm.model.ChatCompletionResponse;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.concurrent.CompletableFuture;

/**
 * @author likui
 * @description
 * @date 2023/11/22 20:30
 **/
public interface OpenAiSession {
    /**
     * 标准sse请求，流式对话
     * @param chatCompletionRequest
     * @param listener
     * @return
     */
    EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener listener);

    /**
     * 异步执行sse，执行结束后会打印完整的结果
     * @param chatCompletionRequest
     * @return
     */
    CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest);

    /**
     * 同步调用，直接返回结果
     * @param chatCompletionRequest
     * @return
     */
    ChatCompletionResponse completionsSync(ChatCompletionRequest chatCompletionRequest);

    /**
     * 异步调用，返回task_id，需要调用 asyncInvokeResult 接口
     * @param chatCompletionRequest
     * @return
     */
    ChatCompletionResponse completionsAsync(ChatCompletionRequest chatCompletionRequest);

    /**
     * 获取异步调用结果
     * @param taskId
     * @return
     */
    ChatCompletionResponse asyncInvokeResult(String taskId);
}
