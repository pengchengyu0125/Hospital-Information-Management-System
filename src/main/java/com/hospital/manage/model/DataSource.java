package com.hospital.manage.model;

import lombok.Data;
import lombok.ToString;

/***
 * 数据源
 */
@Data
@ToString
public class DataSource {
    private String hospitalCode;
    private String hospitalName;
    private String url;
    private String userName;
    private String password;
    private String databaseType;
    private String iconUrl;
    private String originCode;
    private String uploadUrl;
    private String createTime;
    private String updateTime;
}
