package com.cxylk.chatglm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author likui
 * @description sse返回消息类型
 * @date 2023/11/22 19:44
 **/
@Getter
@AllArgsConstructor
public enum EventType {
    add("add", "增量"),
    finish("finish", "结束"),
    error("error", "错误"),
    interrupted("interrupted", "中断");

    private final String code;

    private final String info;
}
