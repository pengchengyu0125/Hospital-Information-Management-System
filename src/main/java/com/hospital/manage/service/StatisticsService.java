package com.hospital.manage.service;

import com.hospital.manage.dto.ReplyDTO;
import com.hospital.manage.mapper.StatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;

    /***
     * 获取总回复数
     * @param deptCode 科室编码
     * @param doctorCode 医生编码
     * @param doctorName 医生姓名
     * @return
     */
    public int getReply(String deptCode, String doctorCode, String doctorName) {
        return statisticsMapper.getReply(deptCode, doctorCode, doctorName);
    }

    /***
     * 获取未回复总数
     * @param deptCode 科室编码
     * @param doctorCode 医生编码
     * @param doctorName 医生姓名
     * @return
     */
    public int getUnReply(String deptCode, String doctorCode, String doctorName) {
        return statisticsMapper.getUnReply(deptCode, doctorCode, doctorName);
    }

    /***
     * 获取按时回复总数
     * @param deptCode 科室编码
     * @param doctorCode 医生编码
     * @param doctorName 医生姓名
     * @return
     */
    public int getOnTime(String deptCode, String doctorCode, String doctorName) {
        //获取提问时间
        List<Date> begin = statisticsMapper.getBegin(deptCode, doctorCode, doctorName);
        //获取回复时间
        List<Date> end = statisticsMapper.getEnd(deptCode, doctorCode, doctorName);
        int count = 0;
        //计算二十四小时内回复的数量
        for (int i = 0; i < begin.size(); i++) {
            if ((end.get(i).getTime() - begin.get(i).getTime()) / 1000 < 86401) {
                ++count;
            }
        }
        return count;
    }

    /***
     * 判断医生姓名是否重复
     * @param deptCode 科室编码
     * @param doctorName 医生姓名
     * @return
     */
    public int repeatName(String deptCode, String doctorName) {
        if (deptCode != null && deptCode != "" && doctorName != null && doctorName != "") {
            return statisticsMapper.repeatName(deptCode, doctorName);
        } else {
            return 0;
        }
    }

    /***
     * 获取所有回复问题信息
     * @param deptCode
     * @param doctorCode
     * @param doctorName
     * @return
     */
    public List<ReplyDTO> getReplyDTO(String deptCode, String doctorCode, String doctorName) {
        return statisticsMapper.getReplyDTO(deptCode, doctorCode, doctorName);
    }
}
