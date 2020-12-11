package com.hospital.manage.dto;

import lombok.Data;

import java.util.List;

/***
 * 树状科室选择栏返回数据格式
 */
@Data
public class DeptJSON {
    private String id;
    private String name;
    private boolean open=false;
    private List<Children> children;
}
