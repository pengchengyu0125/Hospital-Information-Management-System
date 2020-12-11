package com.hospital.manage.mapper;

import com.hospital.manage.dto.Children;
import com.hospital.manage.dto.UserDTO;
import com.hospital.manage.model.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InsertMapper {

    //查询科室类别
    @Select("select * from t_mg_dept_class_type")
    List<DeptData> serachDeptClass();

    //查询科室
    @Results(id = "childrenMap", value = {
            @Result(column = "dept_code", property = "id"),
            @Result(column = "dept_name", property = "name")
    })
    @Select("select * from t_mg_hospital_dept where dept_class_type=#{deptClassCode}")
    List<Children> serachDept(@Param(value = "deptClassCode") String deptClassCode);

    //增加医生
    @Insert("insert into t_mg_doctor (icon_url,doctor_code,doctor_name,title_code,job_code,major,introduce,dept_code,sort,inner_code,hospital_code) values (#{iconUrl},#{doctorCode},#{doctorName},#{titleCode},#{jobCode},#{major},#{introduce},#{deptCode},#{sort},#{doctorCode},#{hospitalCode})")
    void insertDoctor(Doctor doctor);

    //判断重复医生
    @Select("select count(*) from t_mg_doctor where doctor_code=#{doctorCode} and dept_code=#{deptCode}")
    int count(@Param(value = "doctorCode") String doctorCode, @Param(value = "deptCode") String deptCode);

    //获取医生排序最大值
    @Select("select sort from t_mg_doctor where dept_code=#{deptCode} order by sort desc limit 1")
    int countSort(@Param(value = "deptCode") String deptCode);

    //统计当前科室分类编码的科室分类数量
    @Select("select count(*) from t_mg_dept_class_type where dept_class_code=#{deptClassCode}")
    int count1(@Param(value = "deptClassCode") String deptClassCode);

    //获取科室分类顺序
    @Select("select sort from t_mg_dept_class_type order by sort desc limit 1")
    int countDeptClassSort();

    //新增科室分类
    @Insert("insert into t_mg_dept_class_type (hospital_code,dept_class_code,dept_class_name,sort,alive) values (#{hospitalCode},#{deptClassCode},#{deptClassName},#{sort},'1')")
    void insertDeptClass(DeptClass deptClass);

    //统计该科室编码的科室数量
    @Select("select count(*) from t_mg_hospital_dept where dept_code=#{deptCode}")
    int count2(@Param(value = "deptCode") String deptCode);

    //获取科室顺序
    @Select("select sort from t_mg_hospital_dept where dept_class_type=#{deptClassType} order by sort desc limit 1")
    int countDeptSort(@Param(value = "deptClassType") String deptClassType);

    //新增科室
    @Insert("insert into t_mg_hospital_dept (hospital_code,dept_code,dept_name,dept_class_type,sort,alive) values (#{hospitalCode},#{deptCode},#{deptName},#{deptClassType},#{sort},'1')")
    void insertDept(Department department);

    //统计当前用户名的用户数量
    @Select("select count(*) from user where user_name=#{userName}")
    int countUser(@Param(value = "userName") String userName);

    //新增用户
    @Insert("insert into user (user_name,password,hospital_code,access,phone,create_time) values (#{userName},#{password},#{hospitalCode},'0',#{phone},#{createTime})")
    void insertUser(UserDTO userDTO);

    //判断重复数据库
    @Select("select count(*) from database_source where hospital_code=#{hospitalCode}")
    int countDatabase(@Param(value = "hospitalCode") String hospitalCode);

    //新增数据库
    @Insert("insert into database_source (url,user_name,password,hospital_code,hospital_name,database_type,icon_url,upload_url,create_time) values (#{url},#{userName},#{password},#{hospitalCode},#{hospitalName},'mysql',#{iconUrl},#{uploadUrl},#{createTime})")
    void insertDatabase(DataSource dataSource);

    //增加医生图片
    @Update("update t_mg_doctor set icon_url=#{iconUrl} where doctor_code=#{doctorCode}")
    void insertIcon(@Param(value = "iconUrl") String iconUrl, @Param(value = "doctorCode") String doctorCode);

    //统计同一科室分类下科室数量
    @Select("select count(*) from t_mg_hospital_dept where dept_class_type=#{deptClassType}")
    int countDept(@Param(value = "deptClassType") String deptClassType);

    //统计同一科室下的医生数量
    @Select("select count(*) from t_mg_doctor where dept_code=#{deptCode}")
    int countDoctor(@Param(value = "deptCode") String deptCode);

    //统计科室分类数量
    @Select("select count(*) from t_mg_dept_class_type")
    int countDeptClass();

    //统计当前科室分类名的科室分类数量
    @Select("select count(*) from t_mg_dept_class_type where dept_class_name=#{deptClassName}")
    int repeatClassName(@Param(value = "deptClassName") String deptClassName);

    //统计该科室名的科室数量
    @Select("select count(*) from t_mg_hospital_dept where dept_name=#{deptName}")
    int repeatDeptName(@Param(value = "deptName") String deptName);

    //统计当前科室编码和医生编码的医生数量
    @Select("select count(*) from t_mg_doctor where dept_code=#{deptCode} and doctor_code=#{doctorCode}")
    int repeatDoctor(@Param(value = "deptCode") String deptCode, @Param(value = "doctorCode") String doctorCode);
}
