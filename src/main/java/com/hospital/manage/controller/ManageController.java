package com.hospital.manage.controller;

import com.hospital.manage.config.DBContextHolder;
import com.hospital.manage.dto.DeptDTO;
import com.hospital.manage.dto.DoctorDTO;
import com.hospital.manage.dto.HospitalDTO;
import com.hospital.manage.dto.UserDTO;
import com.hospital.manage.model.*;
import com.hospital.manage.service.DBChangeService;
import com.hospital.manage.service.LoginService;
import com.hospital.manage.service.ManageService;
import com.hospital.manage.tools.DesEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class ManageController {

    @Autowired
    private ManageService manageService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private DBChangeService dbChangeServiceImpl;

    Logger logger = Logger.getLogger(this.getClass());

    /***
     * 管理页面首页
     * @param hospitalCode 医院编号
     * @param hospitalName 医院名称
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/manage")
    public String manage(@RequestParam(value = "hospitalCode", required = false) String hospitalCode,
                         @RequestParam(value = "hospitalName", required = false) String hospitalName,
                         HttpServletRequest request,
                         Model model) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //判断用户是否有权限
        if (user.getAccess().equals("1")) {
            //判断用户的医院编码是否为空
            if (user.getHospitalCode() == null) {
                //获取医院集合
                List<HospitalDTO> hospitals = manageService.getHospitals();
                //设置默认的医院
                user.setHospitalCode(hospitals.get(1).getHospitalCode());
                user.setHospitalName(hospitals.get(1).getHospitalName());
                request.getSession().setAttribute("user", user);
                request.getSession().setAttribute("hospitals", hospitals);
            }
            //判断传递的医院编码是否为空
            if (hospitalCode != null) {
                user.setHospitalCode(hospitalCode);
                user.setHospitalName(hospitalName);
                request.getSession().setAttribute("user", user);
            }
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        DBContextHolder.clearDataSource();
        //获取上传服务的地址
        String uploadUrl = manageService.getUploadUrl(datasourceId);
        model.addAttribute("uploadUrl", uploadUrl);
        //获取当前医院的功能列表
        List<Function> functionList = manageService.getFunctions(datasourceId);
        request.getSession().setAttribute("functionList", functionList);

        dbChangeServiceImpl.changeDb(datasourceId);
        //获取科室分类信息
        List<DeptClass> deptClassList = manageService.deptClassList();
        //获取科室信息
        List<DeptDTO> deptList = manageService.deptList();
        //获取医生信息
        List<DoctorDTO> doctorDTOS = manageService.doctorDTOS();
        //获取职称信息
        List<String> titleList = manageService.titleList(datasourceId);

        model.addAttribute("deptClassLists", deptClassList);
        model.addAttribute("deptLists", deptList);
        model.addAttribute("doctorDTOS", doctorDTOS);
        model.addAttribute("titleLists", titleList);
        return "manage";
    }

    /***
     * 获取当前科室下医生
     * @param request
     * @param deptCode 科室编号
     * @return
     * @throws Exception
     */
    @GetMapping("/doctor")
    @ResponseBody
    public Map<String, Object> doctor(HttpServletRequest request,
                                      @RequestParam(name = "deptCode") String deptCode) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取医生集合
        List<Doctor> doctorList = manageService.doctorList(deptCode);
        //返回医生信息
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("data", doctorList);
        return map;
    }

    /***
     * 模糊查询医生
     * @param request
     * @param doctorCode 医生编号
     * @param doctorName 医生姓名
     * @param titleCode 医生职称
     * @return
     */
    @GetMapping("/search")
    @ResponseBody
    public Map<String, Object> search(HttpServletRequest request,
                                      @RequestParam(name = "doctorCode") String doctorCode,
                                      @RequestParam(name = "doctorName") String doctorName,
                                      @RequestParam(name = "titleCode") String titleCode) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取医生集合
        List<Doctor> doctorList = manageService.searchDoctor(doctorCode, doctorName, titleCode);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("data", doctorList);
        return map;
    }

    /***
     * 医生表格数据重载
     * @param request
     * @param doctorCode 医生编号
     * @param doctorName 医生姓名
     * @param titleCode 职称
     * @param deptCode 科室编号
     * @return
     * @throws Exception
     */
    @GetMapping("/reload")
    @ResponseBody
    public Map<String, Object> reload(HttpServletRequest request,
                                      @RequestParam(name = "doctorCode") String doctorCode,
                                      @RequestParam(name = "doctorName") String doctorName,
                                      @RequestParam(name = "titleCode") String titleCode,
                                      @RequestParam(name = "deptCode") String deptCode) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取重载医生信息
        List<Doctor> doctorList = manageService.searchReload(doctorCode, doctorName, titleCode, deptCode);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("data", doctorList);
        return map;
    }

    /***
     * 科室分类页
     * @return
     */
    @GetMapping("/deptClass")
    public String deptClass(HttpServletRequest request) {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "deptClass";
    }

    /***
     * 科室分类信息
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/deptClassTable")
    @ResponseBody
    public Map<String, Object> deptClassTable(HttpServletRequest request) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取科室分类信息
        List<DeptClass> deptClassList = manageService.deptClassList();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("data", deptClassList);
        return map;
    }

    /***
     * 科室页
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/dept")
    public String dept(Model model,
                       HttpServletRequest request) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取科室集合
        List<DeptClass> deptClassList = manageService.deptClassList();
        List<DeptDTO> deptList = manageService.deptList();
        model.addAttribute("deptLists", deptList);
        model.addAttribute("deptClassLists", deptClassList);
        return "dept";
    }

    /***
     * 含线上咨询科室页
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/InternetDept")
    public String InternetDept(Model model,
                       HttpServletRequest request) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取科室集合
        List<DeptClass> deptClassList = manageService.deptClassList();
        List<DeptDTO> deptList = manageService.deptList();
        model.addAttribute("deptLists", deptList);
        model.addAttribute("deptClassLists", deptClassList);
        return "InternetDept";
    }

    /***
     * 同一科室分类下的科室信息
     * @param request
     * @param deptClassType 科室分类编号
     * @return
     * @throws Exception
     */
    @GetMapping("/deptTable")
    @ResponseBody
    public Map<String, Object> deptTable(HttpServletRequest request,
                                         @RequestParam(name = "deptClassType") String deptClassType) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取科室信息
        List<Department> departments = manageService.departments(deptClassType);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("data", departments);
        return map;
    }

    /***
     * 模糊查询科室
     * @param request
     * @param deptCode 科室编号
     * @param deptName 科室名称
     * @return
     * @throws Exception
     */
    @GetMapping("/searchDept")
    @ResponseBody
    public Map<String, Object> searchDept(HttpServletRequest request,
                                          @RequestParam(name = "deptCode") String deptCode,
                                          @RequestParam(name = "deptName") String deptName) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取搜索到的信息
        List<Department> departmentList = manageService.searchDept(deptCode, deptName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("data", departmentList);
        return map;
    }

    /***
     * 科室表格信息重载
     * @param request
     * @param deptCode 科室编号
     * @param deptName 科室名称
     * @param deptClassType 科室分类
     * @return
     * @throws Exception
     */
    @GetMapping("/reloadDept")
    @ResponseBody
    public Map<String, Object> reloadDept(HttpServletRequest request,
                                          @RequestParam(name = "deptCode") String deptCode,
                                          @RequestParam(name = "deptName") String deptName,
                                          @RequestParam(name = "deptClassType") String deptClassType) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取重载科室表格数据
        List<Department> departmentList = manageService.searchReloadDept(deptCode, deptName, deptClassType);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("data", departmentList);
        return map;
    }

    /***
     * 用户管理
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/manageUser")
    public String manageUser(HttpServletRequest request,
                             Model model) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //判断用户权限
        if (user.getAccess().equals("1")) {
            DBContextHolder.clearDataSource();
            //获取所有医院
            List<HospitalDTO> hospitalDTOS = manageService.getHospitals();
            request.getSession().setAttribute("hospitals", hospitalDTOS);
            model.addAttribute("hospitalDTOS", hospitalDTOS);
            return "manageUser";
        } else return "redirect:/manage";
    }

    /***
     * 用户数据表
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/userTable")
    @ResponseBody
    public Map<String, Object> userTable(HttpServletRequest request) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //判断用户是否有权限
        if (user.getAccess().equals("1")) {
            DBContextHolder.clearDataSource();
            //获取所有普通用户
            List<UserDTO> userDTOS = manageService.getUsers();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", 0);
            map.put("msg", "");
            map.put("data", userDTOS);
            return map;
        } else return null;
    }

    /***
     * 数据库管理
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/manageDatabase")
    public String manageDatabase(HttpServletRequest request,
                                 Model model) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //判断用户权限
        if (user.getAccess().equals("1")) {
            DBContextHolder.clearDataSource();
            //获取所有数据库信息
            List<DataSource> dataSources = manageService.getDataSource();
            request.getSession().setAttribute("hospitals", dataSources);
            model.addAttribute("dataSources", dataSources);
            return "manageDatabase";
        } else return "redirect:/manage";
    }

    /***
     * 数据库信息表
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/databaseTable")
    @ResponseBody
    public Map<String, Object> databaseTable(HttpServletRequest request) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //判断用户是否有权限
        if (user.getAccess().equals("1")) {
            DBContextHolder.clearDataSource();
            //获取所有数据库信息
            List<DataSource> dataSources = manageService.getDataSource();
            DesEncoder encoder = new DesEncoder();
            //解密
            for (int i = 0; i < dataSources.size(); i++) {
                dataSources.get(i).setUrl(encoder.decrypt(dataSources.get(i).getUrl(), dataSources.get(i).getHospitalName()));
                dataSources.get(i).setPassword(encoder.decrypt(dataSources.get(i).getPassword(), dataSources.get(i).getIconUrl()));
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", 0);
            map.put("msg", "");
            map.put("data", dataSources);
            return map;
        } else return null;
    }

    /***
     * 批量操作页
     * @return
     */
    @RequestMapping(value = "/batch", method = RequestMethod.GET)
    public String batch(HttpServletRequest request,
                        Model model) {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //判断用户权限
        if (user.getAccess().equals("1")) {
            DBContextHolder.clearDataSource();
            //获取上传服务地址
            String uploadUrl = manageService.getUploadUrl(user.getHospitalCode());
            //获取所有数据库信息
            List<DataSource> dataSources = manageService.getDataSource();
            request.getSession().setAttribute("hospitals", dataSources);
            model.addAttribute("uploadUrl", uploadUrl);
            model.addAttribute("dataSources", dataSources);
            return "batch";
        } else return "redirect:/manage";
    }
}
