package com.hospital.manage.service;


import com.hospital.manage.model.DataSource;

import java.util.List;


/***
 * 切换数据库接口
 */
public interface DBChangeService {

    List<DataSource> get();

    boolean changeDb(String datasourceId) throws Exception;

}
