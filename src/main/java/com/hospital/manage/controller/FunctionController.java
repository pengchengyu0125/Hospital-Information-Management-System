package com.hospital.manage.controller;

import com.hospital.manage.config.DBContextHolder;
import com.hospital.manage.dto.FunctionDTO;
import com.hospital.manage.dto.HospitalDTO;
import com.hospital.manage.dto.HospitalJSON;
import com.hospital.manage.model.Function;
import com.hospital.manage.model.User;
import com.hospital.manage.service.FunctionService;
import com.hospital.manage.service.ManageService;
import com.hospital.manage.tools.LoginVerificationCode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FunctionController {
    @Autowired
    private FunctionService functionService;

    @Autowired
    private ManageService manageService;

    Logger logger = Logger.getLogger(this.getClass());

    //手机验证码url
    private String url = "http://39.106.103.34:9000/auth/entrance/service";

    /***
     * 功能管理
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/function")
    public String function(HttpServletRequest request,
                           Model model) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //判断用户权限
        if (user.getAccess().equals("1")) {
            DBContextHolder.clearDataSource();
            //获取父级功能菜单
            List<Function> functions = functionService.getParent();
            //获取该医院下的功能菜单
            List<Function> functionList = manageService.getFunctions(user.getHospitalCode());
            request.getSession().setAttribute("functionList", functionList);
            model.addAttribute("functions", functions);
            return "function";
        } else return "redirect:/manage";
    }

    /***
     * 功能数据表
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/functionTable")
    @ResponseBody
    public Map<String, Object> functionTable(HttpServletRequest request) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //判断用户是否有权限
        if (user.getAccess().equals("1")) {
            DBContextHolder.clearDataSource();
            //获取完整的功能信息
            List<FunctionDTO> functionDTOS = functionService.getFunctionDTO();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", 0);
            map.put("msg", "");
            map.put("data", functionDTOS);
            return map;
        } else return null;
    }

    /***
     * 新增功能
     * @param request
     * @param functionDTO 功能
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/insertFunction", method = RequestMethod.POST)
    public String insertFunction(HttpServletRequest request,
                                 @RequestBody FunctionDTO functionDTO) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //判断用户是否有权限
        if (user.getAccess().equals("1")) {
            DBContextHolder.clearDataSource();
            //新增功能
            String code = functionService.insert(functionDTO);
            logger.info("用户" + user.getUserName() + "新增功能" + functionDTO.getFunctionId());
            return code;
        } else return null;
    }

    /***
     * 删除功能
     * @param request
     * @param functionId 功能ID
     * @param code　验证码
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/deleteFunction", method = RequestMethod.POST)
    public String deleteFunction(HttpServletRequest request,
                                 @RequestParam(value = "functionId") Integer functionId,
                                 @RequestParam(value = "code") String code) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //判断用户是否有权限
        if (user.getAccess().equals("1")) {
            LoginVerificationCode loginVerificationCode = new LoginVerificationCode();
            Map<String, String> map = loginVerificationCode.validateVerificationCode(url, "15822101039", code);
            //判断验证码是否正确
            if (map.get("code").equals("0")) {
                DBContextHolder.clearDataSource();
                //删除功能
                String result = functionService.delete(functionId);
                logger.info("用户" + user.getUserName() + "删除功能" + functionId);
                return result;
            } else return "-1";
        } else return null;
    }

    /***
     * 获取医院集合
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/hospital", method = RequestMethod.GET)
    public List<HospitalJSON> hospital(HttpServletRequest request) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //判断用户是否有权限
        if (user.getAccess().equals("1")) {
            DBContextHolder.clearDataSource();
            //获取所有医院
            List<HospitalDTO> hospitalDTOS = manageService.getHospitals();
            //设置医院信息传递格式
            List<HospitalJSON> jsons = new ArrayList<>();
            for (int i = 0; i < hospitalDTOS.size(); i++) {
                HospitalJSON hospitalJSON = new HospitalJSON();
                hospitalJSON.setId(hospitalDTOS.get(i).getHospitalCode());
                hospitalJSON.setName(hospitalDTOS.get(i).getHospitalName());
                jsons.add(hospitalJSON);
            }
            return jsons;
        } else return null;
    }

    /***
     * 编辑功能
     * @param request
     * @param functionDTO 功能
     * @return
     * @throws Exception
     */
    @PostMapping("/editFunction")
    @ResponseBody
    public String editFunction(HttpServletRequest request,
                               @RequestBody FunctionDTO functionDTO) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //判断用户是否有权限
        if (user.getAccess().equals("1")) {
            LoginVerificationCode loginVerificationCode = new LoginVerificationCode();
            Map<String, String> map = loginVerificationCode.validateVerificationCode(url, "15822101039", functionDTO.getCode());
            //判断验证码是否相同
            if (map.get("code").equals("0")) {
                //切换数据库
                DBContextHolder.clearDataSource();
                //编辑科室信息
                String result = functionService.edit(functionDTO);
                logger.info("用户" + user.getUserName() + "编辑功能" + functionDTO.getFunctionId());
                return result;
            } else return "-2";
        } else return null;
    }

    /***
     * 手机号验证
     * @return
     * @throws Exception
     */
    @GetMapping("/verify")
    @ResponseBody
    public Map<String, Object> verify() throws Exception {
        LoginVerificationCode loginVerificationCode = new LoginVerificationCode();
        //发送验证码
        Map<String, Object> map = loginVerificationCode.sendLoginVerificationCode(url, "15822101039");
        return map;
    }
}
