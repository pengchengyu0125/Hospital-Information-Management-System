package com.hospital.manage.mapper;

import com.hospital.manage.dto.FunctionDTO;
import com.hospital.manage.model.Function;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FunctionMapper {

    //获取所有功能信息
    @Select("select * from function")
    List<FunctionDTO> getFunctions();

    //获取该功能的所属医院编码
    @Select("select hospital_code from relation where function_id=#{functionId}")
    String[] getHospital(@Param("functionId") Integer functionId);

    //获取父级功能菜单
    @Select("select function_id,function_name from function where belong is null and (function_url is null or function_url='')")
    List<Function> getParent();

    //统计当前功能名称的功能数量
    @Select("select count(*) from function where function_name=#{functionName}")
    int countName(@Param("functionName") String functionName);

    //统计当前功能url的功能数量
    @Select("select count(*) from function where function_url=#{functionUrl}")
    int countUrl(@Param("functionUrl") String functionUrl);

    //新增功能并返回新增的功能id
    @Insert("insert into function (function_name,function_url,access,belong) values (#{functionName},#{functionUrl},#{access},#{belong})")
    @Options(useGeneratedKeys = true, keyProperty = "functionId")
    Integer insert(FunctionDTO functionDTO);

    //新增功能与医院之间关系
    @Insert("insert into relation (hospital_code,function_id) values (#{hospitalCode},#{functionId})")
    void insertRelation(@Param("functionId") Integer functionId, @Param("hospitalCode") String hospitalCode);

    //删除功能信息
    @Delete("delete from function where function_id=#{functionId}")
    void deleteFunction(@Param("functionId") Integer functionId);

    //删除功能对应关系
    @Delete("delete from relation where function_id=#{functionId}")
    void deleteRelation(@Param("functionId") Integer functionId);

    //编辑功能信息
    @Update("update function set function_name=#{functionName}, function_url=#{functionUrl}, access=#{access}, belong=#{belong} where function_id=#{functionId}")
    void edit(FunctionDTO functionDTO);
}
