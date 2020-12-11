package com.hospital.manage.mapper;

import com.hospital.manage.dto.ReplyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface StatisticsMapper {

    //动态拼接条件获取回复问题数
    @Select("<script> select count(*) from t_consult_reply where reply_status='1'" +
            "<if  test= \"(deptCode != null and deptCode != '') || (doctorCode != null and doctorCode != '') || (doctorName != null and doctorName != '')\"> and </if>" +
            "<if  test= \"deptCode != null and deptCode != ''\"> consult_dept_code = #{deptCode} </if>" +
            "<if  test= \"deptCode != null and deptCode != '' and doctorName != null and doctorName != ''\"> and </if>" +
            "<if  test= \"doctorName != null and doctorName != ''\"> reply_doctor_name = #{doctorName} </if>" +
            "<if  test= \"((deptCode != null and deptCode != '') || (doctorName != null and doctorName != '')) and doctorCode != null and doctorCode != ''\"> and </if>" +
            "<if  test= \"doctorCode != null and doctorCode != ''\"> reply_doctor_code = #{doctorCode} </if>" +
            "</script>")
    int getReply(@Param(value = "deptCode") String deptCode, @Param(value = "doctorCode") String doctorCode, @Param(value = "doctorName") String doctorName);

    //动态拼接条件获取未回复问题总数
    @Select("<script> select count(*) from t_consult_reply where reply_status!='1'" +
            "<if  test= \"(deptCode != null and deptCode != '') || (doctorCode != null and doctorCode != '') || (doctorName != null and doctorName != '')\"> and </if>" +
            "<if  test= \"deptCode != null and deptCode != ''\"> consult_dept_code = #{deptCode} </if>" +
            "<if  test= \"deptCode != null and deptCode != '' and doctorName != null and doctorName != ''\"> and </if>" +
            "<if  test= \"doctorName != null and doctorName != ''\"> reply_doctor_name = #{doctorName} </if>" +
            "<if  test= \"((deptCode != null and deptCode != '') || (doctorName != null and doctorName != '')) and doctorCode != null and doctorCode != ''\"> and </if>" +
            "<if  test= \"doctorCode != null and doctorCode != ''\"> reply_doctor_code = #{doctorCode} </if>" +
            "</script>")
    int getUnReply(@Param(value = "deptCode") String deptCode, @Param(value = "doctorCode") String doctorCode, @Param(value = "doctorName") String doctorName);

    //动态拼接条件获取提问时间
    @Select("<script> select createtime from t_consult_reply where reply_status='1'" +
            "<if  test= \"(deptCode != null and deptCode != '') || (doctorCode != null and doctorCode != '') || (doctorName != null and doctorName != '')\"> and </if>" +
            "<if  test= \"deptCode != null and deptCode != ''\"> consult_dept_code = #{deptCode} </if>" +
            "<if  test= \"deptCode != null and deptCode != '' and doctorName != null and doctorName != ''\"> and </if>" +
            "<if  test= \"doctorName != null and doctorName != ''\"> reply_doctor_name = #{doctorName} </if>" +
            "<if  test= \"((deptCode != null and deptCode != '') || (doctorName != null and doctorName != '')) and doctorCode != null and doctorCode != ''\"> and </if>" +
            "<if  test= \"doctorCode != null and doctorCode != ''\"> reply_doctor_code = #{doctorCode} </if>" +
            "</script>")
    List<Date> getBegin(@Param(value = "deptCode") String deptCode, @Param(value = "doctorCode") String doctorCode, @Param(value = "doctorName") String doctorName);

    //动态拼接条件获取回复时间
    @Select("<script> select reply_createtime from t_consult_reply where reply_status='1'" +
            "<if  test= \"(deptCode != null and deptCode != '') || (doctorCode != null and doctorCode != '') || (doctorName != null and doctorName != '')\"> and </if>" +
            "<if  test= \"deptCode != null and deptCode != ''\"> consult_dept_code = #{deptCode} </if>" +
            "<if  test= \"deptCode != null and deptCode != '' and doctorName != null and doctorName != ''\"> and </if>" +
            "<if  test= \"doctorName != null and doctorName != ''\"> reply_doctor_name = #{doctorName} </if>" +
            "<if  test= \"((deptCode != null and deptCode != '') || (doctorName != null and doctorName != '')) and doctorCode != null and doctorCode != ''\"> and </if>" +
            "<if  test= \"doctorCode != null and doctorCode != ''\"> reply_doctor_code = #{doctorCode} </if>" +
            "</script>")
    List<Date> getEnd(@Param(value = "deptCode") String deptCode, @Param(value = "doctorCode") String doctorCode, @Param(value = "doctorName") String doctorName);

    //统计当前科室编码和医生姓名的数量
    @Select("select count(*) from t_consult_reply where consult_dept_code=#{deptCode} and reply_doctor_name = #{doctorName}")
    int repeatName(@Param(value = "deptCode") String deptCode, @Param(value = "doctorName") String doctorName);

    //动态拼接条件获取所有回复问题信息
    @Select("<script> select * from t_consult_reply" +
            "<if  test= \"(deptCode != null and deptCode != '') || (doctorCode != null and doctorCode != '') || (doctorName != null and doctorName != '')\"> where </if>" +
            "<if  test= \"deptCode != null and deptCode != ''\"> consult_dept_code = #{deptCode} </if>" +
            "<if  test= \"deptCode != null and deptCode != '' and doctorName != null and doctorName != ''\"> and </if>" +
            "<if  test= \"doctorName != null and doctorName != ''\"> reply_doctor_name = #{doctorName} </if>" +
            "<if  test= \"((deptCode != null and deptCode != '') || (doctorName != null and doctorName != '')) and doctorCode != null and doctorCode != ''\"> and </if>" +
            "<if  test= \"doctorCode != null and doctorCode != ''\"> reply_doctor_code = #{doctorCode} </if>" +
            "</script>")
    List<ReplyDTO> getReplyDTO(@Param(value = "deptCode") String deptCode, @Param(value = "doctorCode") String doctorCode, @Param(value = "doctorName") String doctorName);
}
