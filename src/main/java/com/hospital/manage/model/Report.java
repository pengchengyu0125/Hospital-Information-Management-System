package com.hospital.manage.model;

import lombok.Data;

/***
 * 回复问题报表
 */
@Data
public class Report {
    private int reply;//总回复
    private int unReply;//未回复
    private int onTime;//按时
    private int late;//未按时
}
