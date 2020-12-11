package com.hospital.manage.dto;

import lombok.Data;

/***
 * 回复问题
 */
@Data
public class ReplyDTO {
    private String consultName;
    private String consultTitle;
    private String consultDescription;
    private String confirmedDisease;
    private String replyText;
    private String replyDoctorCode;
    private String replyDoctorName;
    private String createtime;
    private String replyCreatetime;
}
