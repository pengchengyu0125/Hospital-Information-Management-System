package com.hospital.manage.dto;

import lombok.Data;

/***
 * 医生
 */
@Data
public class DoctorDTO {
    private String doctorCode;
    private String deptCode;
    private String iconUrl;
    private String doctorName;
    private String titleCode;
    private String jobCode;
    private String major;
    private String introduce;
}
