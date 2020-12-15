package com.hospital.manage.controller;

import com.hospital.manage.model.InternetUser;
import com.hospital.manage.model.User;
import com.hospital.manage.service.DBChangeService;
import com.hospital.manage.service.InternetUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 互联网医生Controller
 */
@Controller
public class InternetUserController {

    @Autowired
    private DBChangeService dbChangeServiceImpl;

    @Autowired
    private InternetUserService internetUserService;

    Logger logger = Logger.getLogger(this.getClass());

    /***
     * 管理网上医生页
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/manageInternetUser")
    public String manageInternetUser(HttpServletRequest request) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "manageInternetUser";
    }

    /***
     * 分页查询数据重载
     * @param request
     * @param page 当前页码
     * @param size 每页数据数
     * @param phone 手机号
     * @param uName 姓名
     * @param deptCode 科室编码
     * @param isOnlineDoctor 是否为互联网医生
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/internetUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> InternetUser(HttpServletRequest request,
                                            @RequestParam(name = "page", defaultValue = "1") int page,
                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                            @RequestParam(value = "phone", required = false) String phone,
                                            @RequestParam(value = "name", required = false) String uName,
                                            @RequestParam(value = "deptCode", required = false) String deptCode,
                                            @RequestParam(value = "isOnlineDoctor", required = false) String isOnlineDoctor) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取互联网医生用户信息
        List<InternetUser> internetUsers = internetUserService.getInternetUsers(page, size, phone, uName, deptCode, isOnlineDoctor);
        //获取数据总数
        int total = internetUserService.getTotal(phone, uName, deptCode, isOnlineDoctor);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("data", internetUsers);
        map.put("total", total);
        return map;
    }

    /***
     * 编辑互联网医生信息
     * @param request
     * @param internetUser 互联网用户
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editInternetUser", method = RequestMethod.POST)
    @ResponseBody
    public String editInternetUser(HttpServletRequest request,
                                   @RequestBody InternetUser internetUser) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //编辑用户信息
        String resultCode = internetUserService.editUser(internetUser);
        logger.info("用户" + user.getUserName() + "修改网络医生" + internetUser.getUsername() + "的信息");
        return resultCode;
    }

    /***
     * 获取医生编码
     * @param request
     * @param doctorName 医生姓名
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getDoctorCode", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getDoctorCode(HttpServletRequest request,
                                      @RequestParam(value = "doctorName") String doctorName) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取医生编码
        List<String> doctorCodes = internetUserService.getDoctorCode(doctorName);
        return doctorCodes;
    }

    /***
     * 获取科室编码
     * @param request
     * @param doctorCode 医生编码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getDeptCode", method = RequestMethod.GET)
    @ResponseBody
    public String getDeptCode(HttpServletRequest request,
                              @RequestParam(value = "doctorCode") String doctorCode) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取科室编码
        String deptCodes = internetUserService.getDeptCode(doctorCode);
        return deptCodes;
    }
}
