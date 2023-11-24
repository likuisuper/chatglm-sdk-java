package com.cxylk.chatglm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author likui
 * @description
 * @date 2023/11/23 15:24
 **/
@Getter
@AllArgsConstructor
public enum InvokeType {
    SSE("sse-invoke", "sse调用"),
    ASYNC("async-invoke", "异步调用"),
    INVOKE("invoke", "同步调用"),
    ;
    private final String code;

    private final String info;
}
