package com.hospital.manage.service;

import com.hospital.manage.dto.UserDTO;
import com.hospital.manage.mapper.EditMapper;
import com.hospital.manage.mapper.InsertMapper;
import com.hospital.manage.model.DataSource;
import com.hospital.manage.model.Department;
import com.hospital.manage.model.DeptClass;
import com.hospital.manage.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class EditService {

    @Autowired
    private EditMapper editMapper;

    @Autowired
    private InsertMapper insertMapper;

    /***
     * 编辑医生信息
     * @param doctor 医生
     */
    public String editDoctor(Doctor doctor) {
        //判断医生所属科室的编码是否改变
        if (!doctor.getDeptCode().equals(doctor.getOriginDept())) {
            if (insertMapper.repeatDoctor(doctor.getDeptCode(),doctor.getDoctorCode()) > 0){
                return "-1";
            }
            String s = "1";
            //判断在当前科室下是否有其他医生
            if (insertMapper.countDoctor(doctor.getDeptCode()) > 0) {
                //获取当前科室下医生的最大顺序
                int sort = insertMapper.countSort(doctor.getDeptCode());
                s = String.valueOf(sort + 1);
            }
            //设置当前顺序为最大顺序加一
            doctor.setSort(s);
            editMapper.editDoctor(doctor);
            return "1";
        } else {
            //无需改变顺序更新医生信息
            editMapper.editDoctorNoSort(doctor);
            return "1";
        }
    }

    /***
     * 编辑科室分类
     * @param deptClass 科室分类
     * @return
     */
    public String editClass(DeptClass deptClass) {
        //判断科室分类名是否重复
        if (editMapper.classNameRepeat(deptClass.getDeptClassName()) > 0 && !deptClass.getDeptClassName().equals(deptClass.getOriginName())) {
            return "-2";
        }
        //判断科室分类编号是否重复
        if (editMapper.countDeptClass(deptClass.getDeptClassCode()) > 0 && !deptClass.getDeptClassCode().equals(deptClass.getOriginCode())) {
            return "-1";
        }
        //编辑科室分类信息
        editMapper.editClass(deptClass);
        return "1";
    }

    /***
     * 编辑科室信息
     * @param department 科室
     * @return
     */
    public String editDept(Department department) {
        //判断科室名是否重复
        if (editMapper.deptRepeat(department.getDeptName()) > 0 && !department.getDeptName().equals(department.getOriginName())) {
            return "-2";
        }
        //判断科室编号是否重复
        if (editMapper.countDept(department.getDeptCode()) > 0 && !department.getDeptCode().equals(department.getOriginDept())) {
            return "-1";
        }
        department.setSort(null);
        //判断科室是否变更所属科室分类
        if (!department.getDeptClassType().equals(department.getOriginClass())) {
            String s = "1";
            //判断在当前科室分类下是否有其他科室
            if (insertMapper.countDept(department.getDeptClassType()) > 0) {
                //获取最大顺序
                int sort = editMapper.countDeptSort(department.getDeptClassType());
                s = String.valueOf(sort + 1);
            }
            //设置科室顺序
            department.setSort(s);
            //编辑科室信息
            editMapper.editDept(department);
        } else {
            //无需更新顺序编辑科室信息
            editMapper.editDeptNoSort(department);
        }
        return "1";
    }

    /***
     * 删除医生
     * @param doctorCode 医生编号
     * @param deptCode 科室编号
     */
    public void deleteDoctor(String doctorCode, String deptCode) {
        //删除医生
        editMapper.deleteDoctor(doctorCode, deptCode);
    }

    /***
     * 删除科室分类
     * @param deptClassCode 科室分类编号
     * @return
     */
    public boolean deleteClass(String deptClassCode) {
        int count = editMapper.selectClass(deptClassCode);
        //判断科室分类下是否存在科室
        if (count < 1) {
            //删除科室分类
            editMapper.deleteClass(deptClassCode);
            return true;
        }
        return false;
    }

    /***
     * 删除科室
     * @param deptCode 科室编号
     * @return
     */
    public boolean deleteDept(String deptCode) {
        //统计当前科室编码的医生数量
        int count = editMapper.selectDept(deptCode);
        //判断科室下是否有医生
        if (count < 1) {
            //删除科室
            editMapper.deleteDept(deptCode);
            return true;
        }
        return false;
    }

    /***
     * 更新医生顺序
     * @param doctorCode 医生编号
     * @param deptCode 科室编号
     * @param sort 顺序
     */
    public void editSort(String doctorCode, String deptCode, String sort) {
        editMapper.editSort(doctorCode, deptCode, sort);
    }

    /***
     * 更新科室分类顺序
     * @param deptClassCode 科室分类编号
     * @param deptClassName 科室分类名
     * @param sort 顺序
     */
    public void classSort(String deptClassCode, String deptClassName, String sort) {
        editMapper.classSort(deptClassCode, deptClassName, sort);
    }

    /***
     * 更新科室顺序
     * @param deptCode 科室编号
     * @param deptName 科室名
     * @param sort 顺序
     */
    public void deptSort(String deptCode, String deptName, String sort) {
        editMapper.deptSort(deptCode, deptName, sort);
    }

    /***
     * 删除用户
     * @param userName 用户名
     */
    public void deleteUser(String userName) {
        editMapper.deleteUser(userName);
    }

    /***
     * 编辑用户
     * @param userDTO 用户
     */
    public String editUser(UserDTO userDTO) {
        //判断用户名是否重复
        if (!userDTO.getOriginCode().equals(userDTO.getUserName()) && editMapper.countUser(userDTO.getUserName()) > 0) {
            return "-1";
        } else {
            //设置当前日期
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userDTO.setUpdateTime(df.format(new Date()));
            //编辑用户信息
            editMapper.editUser(userDTO);
            return "1";
        }
    }

    /***
     * 删除数据库
     * @param hospitalCode 医院编号
     */
    public void deleteDatabase(String hospitalCode) {
        editMapper.deleteDatabase(hospitalCode);
    }

    /***
     * 编辑数据库
     * @param dataSource 数据库
     */
    public String editDatabase(DataSource dataSource) {
        //判断数据库医院编号是否重复
        if (!dataSource.getHospitalCode().equals(dataSource.getOriginCode()) && editMapper.countDatabase(dataSource.getHospitalCode()) > 0) {
            return "-1";
        } else {
            //设置当前日期
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dataSource.setUpdateTime(df.format(new Date()));
            //更新数据库信息
            editMapper.editDatabase(dataSource);
            //更新用户所属的医院编码
            editMapper.editHospitalCode(dataSource.getOriginCode(), dataSource.getHospitalCode());
            //更新关系表对应的医院编码
            editMapper.editRelation(dataSource.getOriginCode(), dataSource.getHospitalCode());
            return "1";
        }
    }

    /***
     * 编辑线上咨询科室信息
     * @param department 科室
     * @return
     */
    public String editInternetDept(Department department) {
        //判断科室名是否重复
        if (editMapper.deptRepeat(department.getDeptName()) > 0 && !department.getDeptName().equals(department.getOriginName())) {
            return "-2";
        }
        //判断科室编号是否重复
        if (editMapper.countDept(department.getDeptCode()) > 0 && !department.getDeptCode().equals(department.getOriginDept())) {
            return "-1";
        }
        department.setSort(null);
        //判断科室是否变更所属科室分类
        if (!department.getDeptClassType().equals(department.getOriginClass())) {
            String s = "1";
            //判断在当前科室分类下是否有其他科室
            if (insertMapper.countDept(department.getDeptClassType()) > 0) {
                //获取最大顺序
                int sort = editMapper.countDeptSort(department.getDeptClassType());
                s = String.valueOf(sort + 1);
            }
            //设置科室顺序
            department.setSort(s);
            //编辑科室信息
            editMapper.editInternetDept(department);
        } else {
            //无需更新顺序编辑科室信息
            editMapper.editInternetDeptNoSort(department);
        }
        return "1";
    }
}
