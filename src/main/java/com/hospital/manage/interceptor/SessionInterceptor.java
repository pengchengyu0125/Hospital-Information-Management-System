package com.hospital.manage.interceptor;

import com.hospital.manage.config.DBContextHolder;
import com.hospital.manage.mapper.LoginMapper;
import com.hospital.manage.model.User;
import com.hospital.manage.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * 拦截
 */
@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie
        Cookie[] cookies = request.getCookies();
        User user = null;
        //设置session时效
        request.getSession().setMaxInactiveInterval(60*60*2);
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                //获取token
                if (cookie.getName().equals("myToken")) {
                    String token = cookie.getValue();
                    if (request.getSession().getAttribute("user") != null) {
                        break;
                    }
                    DBContextHolder.clearDataSource();
                    //获取用户信息
                    user = loginMapper.findUser(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
