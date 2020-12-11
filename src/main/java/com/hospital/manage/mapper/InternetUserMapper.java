package com.hospital.manage.mapper;

import com.hospital.manage.model.InternetUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface InternetUserMapper {

    //获取分页互联网医生用户信息
    @Select("<script> select * from t_mg_user" +
            "<if  test= \"(phone != null and phone != '') || (uName != null and uName != '') || (deptCode != null and deptCode != '') || (isOnlineDoctor != null and isOnlineDoctor != '')\"> where </if>" +
            "<if  test= \"phone != null and phone != ''\"> phone regexp #{phone} </if>" +
            "<if  test= \"phone != null and phone != '' and uName != null and uName != ''\"> and </if>" +
            "<if  test= \"uName != null and uName != ''\"> u_name regexp #{uName} </if>" +
            "<if  test= \"((uName != null and uName != '') || (phone != null and phone != '')) and deptCode != null and deptCode != ''\"> and </if>" +
            "<if  test= \"deptCode != null and deptCode != ''\"> dept_code regexp #{deptCode} </if>" +
            "<if  test= \"((uName != null and uName != '') || (phone != null and phone != '') || (deptCode != null and deptCode != '')) and isOnlineDoctor != null and isOnlineDoctor != ''\"> and </if>" +
            "<if  test= \"isOnlineDoctor != null and isOnlineDoctor != ''\"> is_online_doctor = #{isOnlineDoctor} </if>" +
            "limit #{offset},#{size} </script>")
    List<InternetUser> getInternetUsers(@Param(value = "offset") int offset, @Param(value = "size") int size, @Param(value = "phone") String phone, @Param(value = "uName") String uName, @Param(value = "deptCode") String deptCode, @Param(value = "isOnlineDoctor") String isOnlineDoctor);

    //获取问题总数
    @Select("<script> select count(*) from t_mg_user" +
            "<if  test= \"(phone != null and phone != '') || (uName != null and uName != '') || (deptCode != null and deptCode != '') || (isOnlineDoctor != null and isOnlineDoctor != '')\"> where </if>" +
            "<if  test= \"phone != null and phone != ''\"> phone regexp #{phone} </if>" +
            "<if  test= \"phone != null and phone != '' and uName != null and uName != ''\"> and </if>" +
            "<if  test= \"uName != null and uName != ''\"> u_name regexp #{uName} </if>" +
            "<if  test= \"((uName != null and uName != '') || (phone != null and phone != '')) and deptCode != null and deptCode != ''\"> and </if>" +
            "<if  test= \"deptCode != null and deptCode != ''\"> dept_code regexp #{deptCode} </if>" +
            "<if  test= \"((uName != null and uName != '') || (phone != null and phone != '') || (deptCode != null and deptCode != '')) and isOnlineDoctor != null and isOnlineDoctor != ''\"> and </if>" +
            "<if  test= \"isOnlineDoctor != null and isOnlineDoctor != ''\"> is_online_doctor = #{isOnlineDoctor} </if>" +
            "</script>")
    int getTotal(@Param(value = "phone") String phone, @Param(value = "uName") String uName, @Param(value = "deptCode") String deptCode, @Param(value = "isOnlineDoctor") String isOnlineDoctor);

    //编辑互联网医生用户信息
    @Update("update t_mg_user set doctor_code=#{doctorCode},dept_code=#{deptCode},is_online_doctor=#{isOnlineDoctor} where username=#{username}")
    void editUser(InternetUser internetUser);

    @Select("select count(*) from t_mg_user where doctor_code=#{doctorCode}")
    int doctorCodeRepeat(InternetUser internetUser);

    //根据医生姓名获取医生编码
    @Select("select doctor_code from t_mg_doctor where doctor_name=#{doctorName}")
    List<String> getDoctorCode(@Param(value = "doctorName") String doctorName);

    //根据医生编码获取科室编码
    @Select("select dept_code from t_mg_doctor where doctor_code=#{doctorCode}")
    List<String> getDeptCode(@Param(value = "doctorCode") String doctorCode);
}
