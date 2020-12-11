package com.hospital.manage.model;

import lombok.Data;

import java.util.List;

@Data
public class DeptData {
    private String deptClassCode;
    private String deptClassName;
    private List<Department> departmentList;
}
