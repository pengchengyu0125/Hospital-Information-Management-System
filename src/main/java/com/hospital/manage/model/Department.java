package com.hospital.manage.model;

import lombok.Data;

@Data
public class Department {
    private String deptCode;
    private String deptName;
    private String deptClassType;
    private String sort;
    private String originDept;
    private String hospitalCode;
    private String originClass;
    private String isConsult;
    private String originName;
}
