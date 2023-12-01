package com.cxylk.chatglm;

import com.cxylk.chatglm.model.ChatCompletionRequest;
import com.cxylk.chatglm.model.ChatCompletionResponse;
import com.cxylk.chatglm.model.ChatCompletionResponseSse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author likui
 * @description 官网定义的标准api
 * @date 2023/11/22 20:24
 **/
public interface IOpenApi {
    String v3_completions="api/paas/v3/model-api/{model}/{invoke_method}";

    /**
     * 查询异步调用结果接口
     */
    String v3_async_invoke="api/paas/v3/model-api/-/async-invoke/{task_id}";

    @POST(v3_completions)
    Single<ChatCompletionResponse> completions(@Path("model") String model, @Path("invoke_method") String invokeMethod,@Body ChatCompletionRequest chatCompletionRequest);

    @GET(v3_async_invoke)
    Single<ChatCompletionResponse> asyncInvoke(@Path("task_id") String taskId);
}
