package com.hospital.manage.dto;

import lombok.Data;

/***
 * 科室树状孩子
 */
@Data
public class Children {
    private String id;
    private String name;
    private boolean open = false;
    private boolean checked = false;
}
