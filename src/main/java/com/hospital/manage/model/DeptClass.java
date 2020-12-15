package com.hospital.manage.model;

import lombok.Data;

/***
 * 科室分类
 */
@Data
public class DeptClass {
    private String deptClassCode;
    private String deptClassName;
    private String sort;
    private String originCode;
    private String hospitalCode;
    private String originName;
}
