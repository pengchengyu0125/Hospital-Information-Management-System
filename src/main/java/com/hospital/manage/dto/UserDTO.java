package com.hospital.manage.dto;

import lombok.Data;

/***
 * 用户
 */
@Data
public class UserDTO {
    private String userName;
    private String password;
    private String hospitalCode;
    private String hospitalName;
    private String originCode;
    private String phone;
    private String createTime;
    private String updateTime;
}
