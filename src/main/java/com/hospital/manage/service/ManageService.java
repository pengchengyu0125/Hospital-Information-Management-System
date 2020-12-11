package com.hospital.manage.service;

import com.hospital.manage.dto.DeptDTO;
import com.hospital.manage.dto.DoctorDTO;
import com.hospital.manage.dto.HospitalDTO;
import com.hospital.manage.dto.UserDTO;
import com.hospital.manage.mapper.ManageMapper;
import com.hospital.manage.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManageService {

    @Autowired
    private ManageMapper manageMapper;

    /***
     * 获取科室分类名
     * @return
     */
    public List<DeptClass> deptClassList() {
        List<DeptClass> deptClasses = manageMapper.deptClasses();
        return deptClasses;
    }

    /***
     * 获取科室名
     * @return
     */
    public List<DeptDTO> deptList() {
        List<DeptDTO> deptDTOS = manageMapper.deptList();
        return deptDTOS;
    }

    /***
     * 获取所有医生id
     * @return
     */
    public List<DoctorDTO> doctorDTOS() {
        List<DoctorDTO> doctorDTOS = manageMapper.doctorDTOS();
        return doctorDTOS;
    }

    /***
     * 获取该科室下的医生
     * @param deptCode 科室编号
     * @return
     */
    public List<Doctor> doctorList(String deptCode) {
        List<Doctor> doctorList = manageMapper.doctorList(deptCode);
        return doctorList;
    }

    /***
     * 根据条件模糊查询医生
     * @param doctorCode 医生编号
     * @param doctorName 医生名
     * @param titleCode 职称编号
     * @return
     */
    public List<Doctor> searchDoctor(String doctorCode, String doctorName, String titleCode) {
        List<Doctor> searchDoctor = new ArrayList<>();
        if (doctorCode != "" && doctorName != "" && titleCode != "") {
            searchDoctor = manageMapper.searchList(doctorCode, doctorName, titleCode);
        } else if (doctorCode == "" && doctorName != "" && titleCode != "") {
            searchDoctor = manageMapper.searchList1(doctorName, titleCode);
        } else if (doctorCode != "" && doctorName == "" && titleCode != "") {
            searchDoctor = manageMapper.searchList2(doctorCode, titleCode);
        } else if (doctorCode != "" && doctorName != "" && titleCode == "") {
            searchDoctor = manageMapper.searchList3(doctorCode, doctorName);
        } else if (doctorCode != "" && doctorName == "" && titleCode == "") {
            searchDoctor = manageMapper.searchList4(doctorCode);
        } else if (doctorCode == "" && doctorName != "" && titleCode == "") {
            searchDoctor = manageMapper.searchList5(doctorName);
        } else if (doctorCode == "" && doctorName == "" && titleCode != "") {
            searchDoctor = manageMapper.searchList6(titleCode);
        }
        return searchDoctor;
    }

    /***
     * 查询结果重载
     * @param doctorCode 医生编号
     * @param doctorName 医生名
     * @param titleCode 职称编号
     * @param deptCode 科室编号
     * @return
     */
    public List<Doctor> searchReload(String doctorCode, String doctorName, String titleCode, String deptCode) {
        List<Doctor> searchDoctor = new ArrayList<>();
        if (doctorCode != "" && doctorName != "" && titleCode != "") {
            searchDoctor = manageMapper.reloadList(doctorCode, doctorName, titleCode, deptCode);
        } else if (doctorCode == "" && doctorName != "" && titleCode != "") {
            searchDoctor = manageMapper.reloadList1(doctorName, titleCode, deptCode);
        } else if (doctorCode != "" && doctorName == "" && titleCode != "") {
            searchDoctor = manageMapper.reloadList2(doctorCode, titleCode, deptCode);
        } else if (doctorCode != "" && doctorName != "" && titleCode == "") {
            searchDoctor = manageMapper.reloadList3(doctorCode, doctorName, deptCode);
        } else if (doctorCode != "" && doctorName == "" && titleCode == "") {
            searchDoctor = manageMapper.reloadList4(doctorCode, deptCode);
        } else if (doctorCode == "" && doctorName != "" && titleCode == "") {
            searchDoctor = manageMapper.reloadList5(doctorName, deptCode);
        } else if (doctorCode == "" && doctorName == "" && titleCode != "") {
            searchDoctor = manageMapper.reloadList6(titleCode, deptCode);
        }
        return searchDoctor;
    }

    /***
     * 获取该科室分类下的科室
     * @param deptClassType 科室分类号
     * @return
     */
    public List<Department> departments(String deptClassType) {
        List<Department> departmentList = manageMapper.departments(deptClassType);
        return departmentList;
    }

    /***
     * 科室模糊查询
     * @param deptCode 科室编号
     * @param deptName 科室名称
     * @return
     */
    public List<Department> searchDept(String deptCode, String deptName) {
        List<Department> searchDept = new ArrayList<>();
        if (deptCode != "" && deptName != "") {
            searchDept = manageMapper.searchDeptList(deptCode, deptName);
        } else if (deptCode == "" && deptName != "") {
            searchDept = manageMapper.searchDeptList1(deptName);
        } else if (deptCode != "" && deptName == "") {
            searchDept = manageMapper.searchDeptList2(deptCode);
        }
        return searchDept;
    }

    /***
     * 重载查询科室结果
     * @param deptCode 科室编号
     * @param deptName 科室名
     * @param deptClassType 科室分类编号
     * @return
     */
    public List<Department> searchReloadDept(String deptCode, String deptName, String deptClassType) {
        List<Department> searchDept = new ArrayList<>();
        if (deptCode != "" && deptName != "") {
            searchDept = manageMapper.reloadDept(deptCode, deptName, deptClassType);
        } else if (deptCode == "" && deptName != "") {
            searchDept = manageMapper.reloadDept1(deptName, deptClassType);
        } else if (deptCode != "" && deptName == "") {
            searchDept = manageMapper.reloadDept2(deptCode, deptClassType);
        }
        return searchDept;
    }

    /***
     * 获取职称信息
     * @param hospitalCode 医院编号
     * @return
     */
    public List<String> titleList(String hospitalCode) {
        List<String> titleList = manageMapper.getTitle(hospitalCode);
        return titleList;
    }

    /***
     * 获取医院信息
     * @return
     */
    public List<HospitalDTO> getHospitals() {
        return manageMapper.getHospitals();
    }

    /***
     * 获取用户信息
     * @return
     */
    public List<UserDTO> getUsers() {
        return manageMapper.getUsers();
    }

    /***
     * 获取数据库信息
     * @return
     */
    public List<DataSource> getDataSource() {
        return manageMapper.getDataSource();
    }

    /***
     * 获取上传服务地址
     * @param hospitalCode 医院编码
     * @return
     */
    public String getUploadUrl(String hospitalCode) {
        return manageMapper.getUploadUrl(hospitalCode);
    }

    /***
     * 获取该医院的功能菜单
     * @param datasourceId 医院编码
     * @return
     */
    public List<Function> getFunctions(String datasourceId) {
        return manageMapper.getFunctions(datasourceId);
    }
}
