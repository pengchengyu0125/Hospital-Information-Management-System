package com.hospital.manage.model;

import lombok.Data;

/***
 * 功能
 */
@Data
public class Function {
    private Integer functionId;
    private String functionName;
    private String functionUrl;
    private String access;
    private Integer belong;
}
