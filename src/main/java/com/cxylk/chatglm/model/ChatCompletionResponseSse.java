package com.cxylk.chatglm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author likui
 * @description sse响应参数
 * @date 2023/11/23 17:42
 **/
@Data
public class ChatCompletionResponseSse {

    private String data;

    /**
     * SSE调用时响应参数，
     * finish 事件时，通过 meta 发送更多信息，比如数量统计信息 usage
     */
    private String meta;

    @Data
    public static class Meta {
        private String task_status;
        private Usage usage;
        private String task_id;
        private String request_id;
    }

    @Data
    public static class Usage {
        private int completion_tokens;
        private int prompt_tokens;
        private int total_tokens;
    }
}
