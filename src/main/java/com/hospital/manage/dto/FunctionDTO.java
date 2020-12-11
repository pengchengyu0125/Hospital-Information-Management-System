package com.hospital.manage.dto;

import lombok.Data;

/***
 * 所有功能信息
 */
@Data
public class FunctionDTO {
    private Integer functionId;
    private String functionName;
    private String functionUrl;
    private String originName;
    private String originUrl;
    private String access;
    private Integer belong;
    private String[] hospital;
    private String code;
}
