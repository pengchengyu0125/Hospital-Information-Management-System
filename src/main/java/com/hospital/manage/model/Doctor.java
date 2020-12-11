package com.hospital.manage.model;

import lombok.Data;

@Data
public class Doctor {
    private String doctorCode;
    private String iconUrl;
    private String doctorName;
    private String titleCode;
    private String jobCode;
    private String major;
    private String introduce;
    private String deptCode;
    private String deptName;
    private String originDept;
    private String sort;
    private String hospitalCode;
}
