package com.cxylk.chatglm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author likui
 * @description
 * @date 2023/11/22 20:06
 **/
@Getter
@AllArgsConstructor
public enum Role {
    /**
     * user 用户输入的内容，role位user
     */
    user("user"),
    /**
     * 模型生成的内容，role位assistant
     */
    assistant("assistant"),

    /**
     * 系统
     */
    system("system"),

    ;
    private final String code;
}
