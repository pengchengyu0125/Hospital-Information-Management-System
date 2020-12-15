package com.hospital.manage.mapper;

import com.hospital.manage.dto.UserDTO;
import com.hospital.manage.model.DataSource;
import com.hospital.manage.model.Department;
import com.hospital.manage.model.DeptClass;
import com.hospital.manage.model.Doctor;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EditMapper {

    //编辑医生信息
    @Update("update t_mg_doctor set icon_url=#{iconUrl}, doctor_name=#{doctorName}, title_code=#{titleCode}, job_code=#{jobCode}, major=#{major}, introduce=#{introduce}, dept_code=#{deptCode}, sort=#{sort} where doctor_code=#{doctorCode} and dept_code=#{originDept}")
    void editDoctor(Doctor doctor);

    //编辑科室分类
    @Update("UPDATE t_mg_dept_class_type as a left join t_mg_hospital_dept as b on a.dept_class_code = b.dept_class_type SET a.dept_class_code=#{deptClassCode},a.dept_class_name=#{deptClassName},b.dept_class_type=#{deptClassCode} WHERE a.dept_class_code=#{originCode}")
    void editClass(DeptClass deptClass);

    //编辑科室信息
    @Update("UPDATE t_mg_hospital_dept SET dept_code=#{deptCode},dept_name=#{deptName},dept_class_type=#{deptClassType}, sort=#{sort} WHERE dept_code=#{originDept}")
    void editDept(Department department);

    //删除医生信息
    @Delete("delete from t_mg_doctor where doctor_code=#{doctorCode} and dept_code=#{deptCode}")
    void deleteDoctor(@Param(value = "doctorCode") String doctorCode, @Param(value = "deptCode") String deptCode);

    //统计当前科室分类编码的科室数量
    @Select("SELECT COUNT(*) FROM t_mg_hospital_dept WHERE dept_class_type=#{deptClassCode}")
    int selectClass(@Param(value = "deptClassCode") String deptClassCode);

    //删除科室分类
    @Delete("delete from t_mg_dept_class_type where dept_class_code=#{deptClassCode}")
    void deleteClass(@Param(value = "deptClassCode") String deptClassCode);

    //统计当前科室编码的医生数量
    @Select("SELECT COUNT(*) FROM t_mg_doctor WHERE dept_code=#{deptCode}")
    int selectDept(@Param(value = "deptCode") String deptCode);

    //删除科室
    @Delete("delete from t_mg_hospital_dept where dept_code=#{deptCode}")
    void deleteDept(@Param(value = "deptCode") String deptCode);

    //更新排序
    @Update("update t_mg_doctor set sort=#{sort} where doctor_code=#{doctorCode} and dept_code=#{deptCode}")
    void editSort(@Param(value = "doctorCode") String doctorCode, @Param(value = "deptCode") String deptCode, @Param(value = "sort") String sort);

    //更新科室分类排序
    @Update("update t_mg_dept_class_type set sort=#{sort} where dept_class_code=#{deptClassCode} and dept_class_name=#{deptClassName}")
    void classSort(@Param(value = "deptClassCode") String deptClassCode, @Param(value = "deptClassName") String deptClassName, @Param(value = "sort") String sort);

    //更新科室排序
    @Update("update t_mg_hospital_dept set sort=#{sort} where dept_code=#{deptCode} and dept_name=#{deptName}")
    void deptSort(@Param(value = "deptCode") String deptCode, @Param(value = "deptName") String deptName, @Param(value = "sort") String sort);

    //删除用户
    @Delete("delete from user where user_name=#{userName}")
    void deleteUser(@Param(value = "userName") String userName);

    //编辑用户信息
    @Update("update user set user_name=#{userName}, password=#{password}, hospital_code=#{hospitalCode}, phone=#{phone}, update_time=#{updateTime} where user_name=#{originCode}")
    void editUser(UserDTO userDTO);

    //删除数据库
    @Delete("delete from database_source where hospital_code=#{hospitalCode}")
    void deleteDatabase(@Param(value = "hospitalCode") String hospitalCode);

    //编辑数据库
    @Update("update database_source set user_name=#{userName}, password=#{password}, hospital_code=#{hospitalCode}, hospital_name=#{hospitalName}, url=#{url}, icon_url=#{iconUrl}, upload_url=#{uploadUrl}, update_time=#{updateTime} where hospital_code=#{originCode}")
    void editDatabase(DataSource dataSource);

    //统计当前科室分类编号的科室分类数量
    @Select("SELECT COUNT(*) FROM t_mg_dept_class_type WHERE dept_class_code=#{deptClassCode}")
    int countDeptClass(@Param(value = "deptClassCode") String deptClassCode);

    //统计当前科室编码的科室数量
    @Select("SELECT COUNT(*) FROM t_mg_hospital_dept WHERE dept_code=#{deptCode}")
    int countDept(@Param(value = "deptCode") String deptCode);

    //获取医生排序最大值
    @Select("select sort from t_mg_doctor where dept_code=#{deptCode} order by sort desc limit 1")
    int countSort(@Param(value = "deptCode") String deptCode);

    //获取当前科室分类下科室的最大顺序
    @Select("select sort from t_mg_hospital_dept where dept_class_type=#{deptClassType} order by sort desc limit 1")
    int countDeptSort(@Param(value = "deptClassType") String deptClassType);

    //无需更新顺序编辑科室信息
    @Update("UPDATE t_mg_hospital_dept SET dept_code=#{deptCode},dept_name=#{deptName},dept_class_type=#{deptClassType} WHERE dept_code=#{originDept}")
    void editDeptNoSort(Department department);

    //无需更新排序编辑医生信息
    @Update("update t_mg_doctor set icon_url=#{iconUrl}, doctor_name=#{doctorName}, title_code=#{titleCode}, job_code=#{jobCode}, major=#{major}, introduce=#{introduce}, dept_code=#{deptCode} where doctor_code=#{doctorCode} and dept_code=#{originDept}")
    void editDoctorNoSort(Doctor doctor);

    //统计当前科室分类名的科室分类数量
    @Select("select count(*) from t_mg_dept_class_type where dept_class_name=#{deptClassName}")
    int classNameRepeat(@Param(value = "deptClassName") String deptClassName);

    //统计当前科室名称的科室数量
    @Select("select count(*) from t_mg_hospital_dept where dept_name=#{deptName}")
    int deptRepeat(@Param(value = "deptName") String deptName);

    //统计当前医院编码的数据库数量
    @Select("select count(*) from database_source where hospital_code=#{hospitalCode}")
    int countDatabase(@Param(value = "hospitalCode") String hospitalCode);

    //更新用户表所属医院编码
    @Update("update user set hospital_code=#{hospitalCode} where hospital_code=#{originCode}")
    void editHospitalCode(@Param(value = "originCode") String originCode, @Param(value = "hospitalCode") String hospitalCode);

    //更新关系表信息
    @Update("update relation set hospital_code=#{hospitalCode} where hospital_code=#{originCode}")
    void editRelation(@Param(value = "originCode") String originCode, @Param(value = "hospitalCode") String hospitalCode);

    //统计当前用户名的用户数量
    @Select("select count(*) from user where user_name=#{userName}")
    int countUser(@Param(value = "userName") String userName);

    //编辑科室信息
    @Update("UPDATE t_mg_hospital_dept SET dept_code=#{deptCode},dept_name=#{deptName},dept_class_type=#{deptClassType}, sort=#{sort}, is_consult=#{isConsult} WHERE dept_code=#{originDept}")
    void editInternetDept(Department department);

    //无需更新顺序编辑科室信息
    @Update("UPDATE t_mg_hospital_dept SET dept_code=#{deptCode},dept_name=#{deptName},dept_class_type=#{deptClassType}, is_consult=#{isConsult} WHERE dept_code=#{originDept}")
    void editInternetDeptNoSort(Department department);
}
