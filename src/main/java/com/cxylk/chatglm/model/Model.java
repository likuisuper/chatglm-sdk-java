package com.cxylk.chatglm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author likui
 * @description
 * @date 2023/11/22 20:07
 **/
@AllArgsConstructor
@Getter
public enum Model {

    CHATGLM_6B_SSE("chatGLM_6b_SSE", "ChatGLM-6B 测试模型"),
    CHATGLM_LITE("chatglm_lite", "轻量版模型，适用对推理速度和成本敏感的场景"),
    CHATGLM_LITE_32K("chatglm_lite_32k", "标准版模型，适用兼顾效果和成本的场景"),
    CHATGLM_TURBO("chatglm_turbo", "根据输入的自然语言指令完成多种语言类任务，推荐使用 SSE 或异步调用方式请求接口"),
    CHATGLM_PRO("chatglm_pro", "适用于对知识量、推理能力、创造力要求较高的场景"),
    ;
    private final String code;

    private final String info;
}
