package com.hospital.manage.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hospital.manage.config.DBContextHolder;
import com.hospital.manage.service.DBChangeService;
import com.hospital.manage.service.LoginService;
import com.hospital.manage.tools.LoginVerificationCode;
import com.hospital.manage.tools.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Controller
public class LoginController {
    //手机验证码url
    private String url = "http://39.106.103.34:9000/auth/entrance/service";

    @Autowired
    private LoginService loginService;

    @Autowired
    private DBChangeService dbChangeServiceImpl;

    Logger logger = Logger.getLogger(this.getClass());

    /***
     * 登陆页面
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /***
     * 用户登录
     * @param request
     * @param phone 手机号
     * @param code 手机验证码
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    @ResponseBody
    public String userLogin(HttpServletRequest request,
                            @RequestParam(value = "phone") String phone,
                            @RequestParam(value = "code") String code,
                            @RequestParam(value = "username") String username,
                            @RequestParam(value = "password") String password) throws Exception {
        if (request.getSession().getAttribute("user") != null) {
            return "0";
        }
        //验证手机验证码是否正确
        LoginVerificationCode loginVerificationCode = new LoginVerificationCode();
        Map<String, String> map = loginVerificationCode.validateVerificationCode(url, phone, code);
        if (map.get("code").equals("0")) {
            DBContextHolder.clearDataSource();
            //判断用户密码是否正确
            String originPass = loginService.loginVerify(username);
            originPass = MD5Util.string2MD5(originPass);
            String myToken = "";
            String Date = String.valueOf(new Date());
            if (loginService.judge(username, phone) != 1) {
                return "-3";
            } else if (originPass.equals(password)) {
                String datasourceId = loginService.getDatabaseId(username);
                //生成token
                myToken = JWT.create()
                        .withClaim("username", username)
                        .withClaim("datasourceId", datasourceId)
                        .sign(Algorithm.HMAC256(Date));
                //更新token
                loginService.updateToken(username, myToken);
                logger.info("用户" + username + "登录系统");
                return myToken;
            } else return "-2";
        } else return "-1";
    }

    /***
     * 退出登录
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) {
        httpServletRequest.getSession().removeAttribute("user");
        Cookie[] cookies = httpServletRequest.getCookies();
        //清除cookie信息
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
        }
        return "redirect:/manage";
    }

    /***
     * 发送验证码
     * @param request
     * @param phone 手机号
     * @return
     * @throws Exception
     */
    @GetMapping("/sendCode")
    @ResponseBody
    public Map<String, Object> sendCode(HttpServletRequest request,
                                        @RequestParam(name = "phone") String phone) throws Exception {
        LoginVerificationCode loginVerificationCode = new LoginVerificationCode();
        Map<String, Object> map = loginVerificationCode.sendLoginVerificationCode(url, phone);
        return map;
    }

    @GetMapping("/forget")
    public String forget() {
        return "forget";
    }

    /***
     * 忘记密码
     * @param request
     * @param phone 手机号
     * @param code 验证码
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
    @ResponseBody
    public String forgetPassword(HttpServletRequest request,
                                 @RequestParam(value = "phone") String phone,
                                 @RequestParam(value = "code") String code,
                                 @RequestParam(value = "username") String username,
                                 @RequestParam(value = "password") String password) throws Exception {
        //验证手机验证码是否正确
        LoginVerificationCode loginVerificationCode = new LoginVerificationCode();
        Map<String, String> map = loginVerificationCode.validateVerificationCode(url, phone, code);
        if (map.get("code").equals("0")) {
            DBContextHolder.clearDataSource();
            //判断用户名和手机号是否正确
            int count = loginService.judge(username, phone);
            if (count != 1) {
                return "1";
            } else {
                //更新密码
                loginService.updatePassword(username, phone, password);
                logger.info(username + "用户修改密码");
                return "0";
            }
        } else return "-1";
    }
}
