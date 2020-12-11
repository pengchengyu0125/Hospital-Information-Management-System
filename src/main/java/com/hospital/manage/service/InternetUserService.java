package com.hospital.manage.service;

import com.hospital.manage.mapper.InternetUserMapper;
import com.hospital.manage.model.InternetUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InternetUserService {

    @Autowired
    private InternetUserMapper internetUserMapper;

    /***
     * 分页显示用户数据
     * @param page 当前页码
     * @param size 每页数据量
     * @param phone
     * @param uName
     * @param deptCode
     * @param isOnlineDoctor
     * @return
     */
    public List<InternetUser> getInternetUsers(int page, int size, String phone, String uName, String deptCode, String isOnlineDoctor) {
        int offset = size * (page - 1);
        return internetUserMapper.getInternetUsers(offset, size, phone, uName, deptCode, isOnlineDoctor);
    }

    /***
     * 获取总数
     * @return
     * @param phone
     * @param uName
     * @param deptCode
     * @param isOnlineDoctor
     */
    public int getTotal(String phone, String uName, String deptCode, String isOnlineDoctor) {
        return internetUserMapper.getTotal(phone, uName, deptCode, isOnlineDoctor);
    }

    /***
     * 编辑用户信息
     * @param internetUser 用户
     * @return
     */
    public String editUser(InternetUser internetUser) {
        internetUserMapper.editUser(internetUser);
        return "1";
    }

    /***
     * 获取医生编码
     * @param doctorName 医生姓名
     * @return
     */
    public List<String> getDoctorCode(String doctorName) {
        List<String> doctorCodes = internetUserMapper.getDoctorCode(doctorName).stream().distinct().collect(Collectors.toList());
        return doctorCodes;
    }

    /***
     * 获取科室编码
     * @param doctorCode 医生编码
     * @return
     */
    public String getDeptCode(String doctorCode) {
        List<String> deptCodes = internetUserMapper.getDeptCode(doctorCode);
        String codes = null;
        //将多个科室编码用逗号分隔一起显示
        for (int i = 0; i < deptCodes.size(); i++) {
            if (i > 0) {
                codes = codes + ',' + deptCodes.get(i);
            } else {
                codes = deptCodes.get(i);
            }
        }
        return codes;
    }
}
