package com.hospital.manage.model;

import lombok.Data;

@Data
public class User {
    private String userName;
    private String token;
    private String hospitalCode;
    private String hospitalName;
    private String iconUrl;
    private String access;
}
