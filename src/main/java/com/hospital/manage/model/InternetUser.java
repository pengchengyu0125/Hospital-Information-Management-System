package com.hospital.manage.model;

import lombok.Data;

/***
 * 互联网医生
 */
@Data
public class InternetUser {
    private String username;
    private String phone;
    private String password;
    private String idCard;
    private String uName;
    private String doctorCode;
    private String deptCode;
    private String isOnlineDoctor;
}
