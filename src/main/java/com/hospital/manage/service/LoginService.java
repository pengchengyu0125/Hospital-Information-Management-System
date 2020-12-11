package com.hospital.manage.service;

import com.hospital.manage.mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginMapper loginMapper;

    /***
     * 验证用户名密码
     * @param username 用户名
     * @return
     */
    public String loginVerify(String username) {
        String originPass = loginMapper.loginVerify(username);
        return originPass;
    }

    /***
     * 更新用户token
     * @param username 用户名
     * @param myToken token
     */
    public void updateToken(String username, String myToken) {
        loginMapper.updateToken(username, myToken);
    }

    /***
     * 获取医院数据库编号
     * @param username 用户名
     * @return
     */
    public String getDatabaseId(String username) {
        String databaseId = loginMapper.getDatabase(username);
        if (databaseId == null) {
            databaseId = "1";
        }
        return databaseId;
    }

    /***
     * 判断用户名和手机号是否匹配
     * @param username 用户名
     * @param phone 手机号
     * @return
     */
    public int judge(String username, String phone) {
        return loginMapper.judge(username, phone);
    }

    /***
     * 更新用户密码
     * @param username 用户名
     * @param phone 手机号
     * @param password 密码
     */
    public void updatePassword(String username, String phone, String password) {
        loginMapper.updatePassword(username, phone, password);
    }
}
