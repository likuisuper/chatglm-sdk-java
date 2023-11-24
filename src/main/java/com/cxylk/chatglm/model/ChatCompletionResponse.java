package com.cxylk.chatglm.model;

import lombok.Data;

import java.util.List;

/**
 * @author likui
 * @description
 * @date 2023/11/22 20:16
 **/
@Data
public class ChatCompletionResponse {
    private int code;
    private String msg;
    private Data data;
    private boolean success;

    /**
     * SSE调用时响应参数，
     * finish 事件时，通过 meta 发送更多信息，比如数量统计信息 usage
     */
    private Meta meta;

    @lombok.Data
    public static class Data {
        private String task_status;
        private Usage usage;
        private List<Choice> choices;
        private String task_id;
        private String request_id;
    }

    @lombok.Data
    public static class Choice {
        private String role;
        private String content;
    }

    @lombok.Data
    public static class Meta {
        private String task_status;
        private Usage usage;
        private String task_id;
        private String request_id;
    }

    @lombok.Data
    public static class Usage {
        private int completion_tokens;
        private int prompt_tokens;
        private int total_tokens;
    }
}
