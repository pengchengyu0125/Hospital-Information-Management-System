package com.hospital.manage.model;

import lombok.Data;

import java.util.List;

/***
 * 科室数据格式
 */
@Data
public class DeptData {
    private String deptClassCode;
    private String deptClassName;
    private List<Department> departmentList;
}
