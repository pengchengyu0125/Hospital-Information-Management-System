package com.hospital.manage.controller;

import com.hospital.manage.config.DBContextHolder;
import com.hospital.manage.dto.HospitalDTO;
import com.hospital.manage.dto.UserDTO;
import com.hospital.manage.model.*;
import com.hospital.manage.service.DBChangeService;
import com.hospital.manage.service.EditService;
import com.hospital.manage.service.ManageService;
import com.hospital.manage.tools.DesEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class EditController {

    @Autowired
    private EditService editService;

    @Autowired
    private DBChangeService dbChangeServiceImpl;

    @Autowired
    private ManageService manageService;

    Logger logger = Logger.getLogger(this.getClass());

    /***
     * 编辑医生信息
     * @param request 请求
     * @param doctor 医生
     */
    @PostMapping("/edit")
    @ResponseBody
    public String edit(HttpServletRequest request,
                       @RequestBody Doctor doctor) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //编辑医生信息
        String code = editService.editDoctor(doctor);
        logger.info("用户" + user.getUserName() + "修改科室" + doctor.getDeptCode() + "下" + doctor.getDoctorCode() + "医生的信息");
        return code;
    }

    /***
     * 更新医生排序
     * @param request 请求
     * @param doctors 变更排序的所有医生
     */
    @ResponseBody
    @RequestMapping(value = "/updateSort", method = RequestMethod.POST)
    public String updateSort(HttpServletRequest request,
                             @RequestBody List<Doctor> doctors) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //更新每一个医生的排序
        for (int i = 0; i < doctors.size(); i++) {
            editService.editSort(doctors.get(i).getDoctorCode(), doctors.get(i).getDeptCode(), doctors.get(i).getSort());
        }
        logger.info("用户" + user.getUserName() + "更新医生顺序");
        return null;
    }

    /***
     * 删除医生
     * @param request 请求
     * @param doctorCode 医生ID
     * @param deptCode 科室ID
     */
    @PostMapping("/delete")
    @ResponseBody
    public String delete(HttpServletRequest request,
                         @RequestParam(name = "doctorCode") String doctorCode,
                         @RequestParam(name = "deptCode") String deptCode) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //删除医生
        editService.deleteDoctor(doctorCode, deptCode);
        logger.info("用户" + user.getUserName() + "删除科室" + deptCode + "下的医生" + doctorCode);
        return "delete";
    }

    /***
     * 编辑科室分类
     * @param request 请求
     * @param deptClass 科室分类
     */
    @PostMapping("/editClass")
    @ResponseBody
    public String editClass(HttpServletRequest request,
                            @RequestBody DeptClass deptClass) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //编辑科室分类信息
        String code = editService.editClass(deptClass);
        logger.info("用户" + user.getUserName() + "修改科室分类" + deptClass.getDeptClassCode() + "的信息");
        return code;
    }

    /***
     * 更新科室分类排序
     * @param request 请求
     * @param deptClasses 变更排序的所有科室分类
     */
    @ResponseBody
    @RequestMapping(value = "/updateClassSort", method = RequestMethod.POST)
    public String updateClassSort(HttpServletRequest request,
                                  @RequestBody List<DeptClass> deptClasses) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //更改每一个科室分类的顺序
        for (int i = 0; i < deptClasses.size(); i++) {
            editService.classSort(deptClasses.get(i).getDeptClassCode(), deptClasses.get(i).getDeptClassName(), deptClasses.get(i).getSort());
        }
        logger.info("用户" + user.getUserName() + "更新科室分类顺序");
        return null;
    }

    /***
     * 删除科室分类
     * @param request 请求
     * @param deptClassCode 科室分类编号
     */
    @PostMapping("/deleteClass")
    @ResponseBody
    public boolean deleteClass(HttpServletRequest request,
                               @RequestParam(name = "deptClassCode") String deptClassCode) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return false;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //删除科室
        if (editService.deleteClass(deptClassCode)) {
            logger.info("用户" + user.getUserName() + "删除科室分类" + deptClassCode);
            return true;
        }
        return false;
    }

    /***
     * 编辑科室信息
     * @param request 请求
     * @param department 科室
     * @return
     * @throws Exception
     */
    @PostMapping("/editDept")
    @ResponseBody
    public String editDept(HttpServletRequest request,
                           @RequestBody Department department) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //编辑科室信息
        String code = editService.editDept(department);
        logger.info("用户" + user.getUserName() + "修改科室" + department.getDeptCode() + "的信息");
        return code;
    }

    /***
     * 编辑线上咨询科室信息
     * @param request 请求
     * @param department 科室
     * @return
     * @throws Exception
     */
    @PostMapping("/editInternetDept")
    @ResponseBody
    public String editInternetDept(HttpServletRequest request,
                                   @RequestBody Department department) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //编辑科室信息
        String code = editService.editInternetDept(department);
        logger.info("用户" + user.getUserName() + "修改科室" + department.getDeptCode() + "的信息");
        return code;
    }

    /***
     * 更新科室排序
     * @param request 请求
     * @param departments 变更排序的所有科室
     */
    @ResponseBody
    @RequestMapping(value = "/updateDeptSort", method = RequestMethod.POST)
    public String updateDeptSort(HttpServletRequest request,
                                 @RequestBody List<Department> departments) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //更新每个科室排序
        for (int i = 0; i < departments.size(); i++) {
            editService.deptSort(departments.get(i).getDeptCode(), departments.get(i).getDeptName(), departments.get(i).getSort());
        }
        logger.info("用户" + user.getUserName() + "更新科室顺序");
        return null;
    }

    /***
     * 删除科室
     * @param request 请求
     * @param deptCode 科室编号
     */
    @PostMapping("/deleteDept")
    @ResponseBody
    public boolean deleteDept(HttpServletRequest request,
                              @RequestParam(name = "deptCode") String deptCode) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return false;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //删除科室
        if (editService.deleteDept(deptCode)) {
            logger.info("用户" + user.getUserName() + "删除科室" + deptCode);
            return true;
        }
        return false;
    }

    /***
     * 编辑用户信息
     * @param request 请求
     * @param userDTO 用户
     */
    @PostMapping("/editUser")
    @ResponseBody
    public String editUser(HttpServletRequest request,
                           @RequestBody UserDTO userDTO) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //判断用户权限，如果为1则有权限
        if (user.getAccess().equals("1")) {
            //数据库切换至主数据库
            DBContextHolder.clearDataSource();
            //编辑用户信息
            String code = editService.editUser(userDTO);
            logger.info("用户" + user.getUserName() + "修改用户" + userDTO.getUserName() + "的信息");
            return code;
        } else return null;
    }

    /***
     * 删除用户
     * @param request 请求
     * @param userName 用户名
     */
    @PostMapping("/deleteUser")
    @ResponseBody
    public boolean deleteUser(HttpServletRequest request,
                              @RequestParam(name = "userName") String userName) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return false;
        }
        //判断用户权限，如果为1则有权限
        if (user.getAccess().equals("1")) {
            //数据库切换至主数据库
            DBContextHolder.clearDataSource();
            //删除用户
            editService.deleteUser(userName);
            logger.info("用户" + user.getUserName() + "删除用户" + userName);
            return true;
        } else return false;
    }

    /***
     * 编辑数据库信息
     * @param request 请求
     * @param dataSource 数据库
     */
    @PostMapping("/editDatabase")
    @ResponseBody
    public String editDatabase(HttpServletRequest request,
                               @RequestBody DataSource dataSource) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        //判断用户权限，如果为1则有权限
        if (user.getAccess().equals("1")) {
            //数据库切换至主数据库
            DBContextHolder.clearDataSource();
            //数据库信息加密
            DesEncoder encoder = new DesEncoder();
            dataSource.setUrl(encoder.encrypt(dataSource.getUrl(), dataSource.getHospitalName()));
            dataSource.setPassword(encoder.encrypt(dataSource.getPassword(), dataSource.getIconUrl()));
            //更新数据库信息
            String code = editService.editDatabase(dataSource);
            logger.info("用户" + user.getUserName() + "修改医院" + dataSource.getHospitalCode() + "的数据库信息");
            //获取医院集合
            List<HospitalDTO> hospitals = manageService.getHospitals();
            //设置当前session的医院信息
            user.setHospitalCode(dataSource.getHospitalCode());
            user.setHospitalName(dataSource.getHospitalName());
            request.getSession().setAttribute("hospitals", hospitals);
            return code;
        } else return null;
    }

    /***
     * 删除数据库
     * @param request 请求
     * @param hospitalCode 医院编号
     */
    @PostMapping("/deleteDatabase")
    @ResponseBody
    public boolean deleteDatabase(HttpServletRequest request,
                                  @RequestParam(name = "hospitalCode") String hospitalCode) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return false;
        }
        //判断用户权限，如果为1则有权限
        if (user.getAccess().equals("1")) {
            //数据库切换至主数据库
            DBContextHolder.clearDataSource();
            //删除数据库
            editService.deleteDatabase(hospitalCode);
            logger.info("用户" + user.getUserName() + "删除医院" + hospitalCode + "的数据库信息");
            return true;
        } else return false;
    }
}