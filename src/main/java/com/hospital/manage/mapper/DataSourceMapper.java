package com.hospital.manage.mapper;

import com.hospital.manage.model.DataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DataSourceMapper {

    //获取数据库源
    @Select("SELECT * FROM database_source")
    List<DataSource> get();

}
