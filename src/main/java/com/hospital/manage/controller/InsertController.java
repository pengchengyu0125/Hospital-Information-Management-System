package com.hospital.manage.controller;

import com.google.gson.Gson;
import com.hospital.manage.config.DBContextHolder;
import com.hospital.manage.dto.DeptJSON;
import com.hospital.manage.dto.UserDTO;
import com.hospital.manage.model.*;
import com.hospital.manage.service.DBChangeService;
import com.hospital.manage.service.InsertService;
import com.hospital.manage.service.ManageService;
import com.hospital.manage.tools.DesEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InsertController {
    @Autowired
    private ManageService manageService;

    @Autowired
    private DBChangeService dbChangeServiceImpl;

    @Autowired
    private InsertService insertService;

    Logger logger = Logger.getLogger(this.getClass());

    /***
     * 新增医生页
     * @return
     */
    @GetMapping("/insert")
    public String insert(HttpServletRequest request,
                         Model model) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        DBContextHolder.clearDataSource();
        //获取上传服务地址
        String uploadUrl = manageService.getUploadUrl(datasourceId);
        model.addAttribute("uploadUrl", uploadUrl);
        dbChangeServiceImpl.changeDb(datasourceId);
        return "insert";
    }

    /***
     * 新增医生
     * @param request
     * @param doctor 医生
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/insertDoctor", method = RequestMethod.POST)
    public String insertDoctor(HttpServletRequest request,
                               @RequestBody Doctor doctor) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //判断医生是否重复
        if (insertService.count(doctor) > 0) {
            return "相同医生无法增加！";
        }
        //设置所属医院编号
        doctor.setHospitalCode(datasourceId);
        //新增医生
        insertService.insertDoctor(doctor);
        logger.info("用户" + user.getUserName() + "新增医生" + doctor.getDoctorCode());
        return "增加成功";
    }

    /***
     * 新增科室分类页
     * @return
     */
    @RequestMapping(value = "/insertDeptClass", method = RequestMethod.GET)
    public String insertDeptClass(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "insertDeptClass";
    }

    /***
     * 新增科室分类
     * @param request
     * @param deptClassCode 科室分类号
     * @param deptClassName 科室分类名
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/insertDeptClasses", method = RequestMethod.POST)
    public String insertDeptClasses(HttpServletRequest request,
                                    @RequestParam(value = "deptClassCode") String deptClassCode,
                                    @RequestParam(value = "deptClassName") String deptClassName) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //新增科室分类
        DeptClass deptClass = new DeptClass();
        deptClass.setDeptClassCode(deptClassCode);
        deptClass.setDeptClassName(deptClassName);
        //判断科室分类编码是否相同
        if (insertService.count1(deptClass) > 0) {
            return "相同科室分类无法增加！";
        }
        //判断科室分类名是否相同
        if (insertService.repeatClassName(deptClass) > 0) {
            return "相同科室分类无法增加！";
        }
        //设置所属医院编号
        deptClass.setHospitalCode(datasourceId);
        //新增科室分类
        insertService.insertDeptClass(deptClass);
        logger.info("用户" + user.getUserName() + "新增科室分类" + deptClass.getDeptClassCode());
        return "增加成功";
    }

    /***
     * 新增科室页
     * @return
     */
    @RequestMapping(value = "/insertDept", method = RequestMethod.GET)
    public String insertDept(HttpServletRequest request,
                             Model model) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取科室分类
        List<DeptClass> deptClassList = manageService.deptClassList();
        model.addAttribute("deptClassLists", deptClassList);
        return "insertDept";
    }

    /***
     * 新增科室
     * @param request
     * @param department 科室
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/insertDepts", method = RequestMethod.POST)
    public String insertDepts(HttpServletRequest request,
                              @RequestBody Department department) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //判断科室编码是否重复
        if (insertService.count2(department) > 0) {
            return "相同科室无法增加！";
        }
        //判断科室名称是否重复
        if (insertService.repeatDeptName(department) > 0) {
            return "相同科室无法增加！";
        }
        //设置所属医院编号
        department.setHospitalCode(datasourceId);
        //新增科室
        insertService.insertDept(department);
        logger.info("用户" + user.getUserName() + "新增科室" + department.getDeptCode());
        return "增加成功";
    }

    /***
     * 批量导入excel医生信息
     * @param request
     * @param doctorList 医生集合
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/insertDoctorList", method = RequestMethod.POST)
    public String insertDoctorList(HttpServletRequest request,
                                         @RequestBody List<Doctor> doctorList) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //判断插表中是否有数据
        if (doctorList.size() > 2) {
            //判断插入数据是否重复
            String error = insertService.insertDoctors(doctorList);
            //判断是否出现错误
            if (error != null) {
                return error;
            }
            else {
                logger.info("用户" + user.getUserName() + "批量导入医生信息");
                return "1";
            }
        } else {
            return "-1";
        }
    }

    /***
     * 新增用户
     * @param request
     * @param userDTO 用户
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
    public String insertUser(HttpServletRequest request,
                             @RequestBody UserDTO userDTO) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //判断用户是否有权限
        if (user.getAccess().equals("1")) {
            DBContextHolder.clearDataSource();
            //判断是否重复增加用户
            if (insertService.countUser(userDTO) > 0) {
                return "0";
            }
            //新增用户
            insertService.insertUser(userDTO);
            logger.info("用户" + user.getUserName() + "新增用户" + userDTO.getUserName());
            return "1";
        } else return null;
    }

    /***
     * 新增数据库
     * @param request
     * @param dataSource 数据库
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/insertDatabase", method = RequestMethod.POST)
    public String insertDatabase(HttpServletRequest request,
                                 @RequestBody DataSource dataSource) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //判断用户是否有权限
        if (user.getAccess().equals("1")) {
            DBContextHolder.clearDataSource();
            //判断是否重复增加数据库
            if (insertService.countDatabase(dataSource) > 0) {
                return "0";
            }
            //加密
            DesEncoder encoder = new DesEncoder();
            dataSource.setUrl(encoder.encrypt(dataSource.getUrl(), dataSource.getHospitalName()));
            dataSource.setPassword(encoder.encrypt(dataSource.getPassword(), dataSource.getIconUrl()));
            //新增数据库
            insertService.insertDatabase(dataSource);
            logger.info("用户" + user.getUserName() + "新增数据库" + dataSource.getHospitalCode());
            return "1";
        } else return null;
    }

    /***
     * 树状科室选择栏信息
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/deptInfo")
    @ResponseBody
    public String lists(HttpServletRequest request) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //获取科室
        List<DeptData> lists = new ArrayList<>();
        lists = insertService.deptDataList();
        //树状选择栏返回数据
        List<DeptJSON> deptJSONList = new ArrayList<>();
        int num = lists.size();
        for (int i = 0; i < num; i++) {
            DeptJSON deptJSON = new DeptJSON();
            //添加根节点标识
            deptJSON.setId("Root" + lists.get(i).getDeptClassCode());
            deptJSON.setName(lists.get(i).getDeptClassName());
            deptJSONList.add(deptJSON);
            //添加子节点数据
            deptJSONList.get(i).setChildren(insertService.departmentList(lists.get(i).getDeptClassCode()));
        }
        Gson gson = new Gson();
        String jsonString = gson.toJson(deptJSONList);
        return jsonString;
    }
}
