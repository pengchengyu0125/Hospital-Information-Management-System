package com.hospital.manage.service;

import com.hospital.manage.dto.Children;
import com.hospital.manage.dto.UserDTO;
import com.hospital.manage.mapper.InsertMapper;
import com.hospital.manage.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class InsertService {
    @Autowired
    private InsertMapper insertMapper;

    /***
     * 获取科室分类集合
     * @return
     */
    public List<DeptData> deptDataList() {
        List<DeptData> list = insertMapper.serachDeptClass();
        return list;
    }

    /***
     * 获取子科室集合
     * @param deptClassCode
     * @return
     */
    public List<Children> departmentList(String deptClassCode) {
        List<Children> children = insertMapper.serachDept(deptClassCode);
        return children;
    }

    /**
     * 批量增加医生信息
     *
     * @param doctorList 医生集合
     */
    @Transactional
    public String insertDoctors(List<Doctor> doctorList) {
        try {
            //从excel表格第二行开始
            for (int i = 2; i < doctorList.size(); i++) {
                Doctor doctor = doctorList.get(i);
                //获取顺序
                int sort = insertMapper.countSort(doctor.getDeptCode());
                String s = String.valueOf(sort + 1);
                doctor.setSort(s);
                int count = insertMapper.count(doctor.getDoctorCode(), doctor.getDeptCode());
                //判断是否重复
                if (count > 0) {
                    throw new RuntimeException("插入信息第" + (i - 1) + "行重复!可能与数据库信息重复或表格自身信息重复,请检查!");
                }
                //插入信息
                insertMapper.insertDoctor(doctor);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //手动回滚失误
            return e.getMessage();
        }
        return null;
    }

    /***
     * 统计同一科室医生数量
     * @param doctor 医生
     * @return
     */
    public int count(Doctor doctor) {
        int count = insertMapper.count(doctor.getDoctorCode(), doctor.getDeptCode());
        return count;
    }

    /***
     * 统计当前科室分类编码的科室分类数量
     * @param deptClass 科室分类
     * @return
     */
    public int count1(DeptClass deptClass) {
        int count = insertMapper.count1(deptClass.getDeptClassCode());
        return count;
    }

    /***
     * 新增科室分类
     * @param deptClass 科室分类
     */
    public void insertDeptClass(DeptClass deptClass) {
        String s = "1";
        //判断是否有其他的科室分类
        if (insertMapper.countDeptClass() > 0) {
            //获取最大科室分类顺序
            int sort = insertMapper.countDeptClassSort();
            s = String.valueOf(sort + 1);
        }
        deptClass.setSort(s);
        //新增科室分类
        insertMapper.insertDeptClass(deptClass);
    }

    /***
     * 统计该科室编码的科室数量
     * @param department 科室
     * @return
     */
    public int count2(Department department) {
        int count = insertMapper.count2(department.getDeptCode());
        return count;
    }

    /***
     * 新增科室
     * @param department 科室
     */
    public void insertDept(Department department) {
        String s = "1";
        //判断在当前科室分类下是否有其他科室
        if (insertMapper.countDept(department.getDeptClassType()) > 0) {
            int sort = insertMapper.countDeptSort(department.getDeptClassType());
            s = String.valueOf(sort + 1);
        }
        //设置科室顺序
        department.setSort(s);
        //新增科室
        insertMapper.insertDept(department);
    }

    /***
     * 增加医生
     * @param doctor 医生
     */
    public void insertDoctor(Doctor doctor) {
        String s = "1";
        //判断在当前科室下是否有其他医生
        if (insertMapper.countDoctor(doctor.getDeptCode()) > 0) {
            //获取当前科室的医生的最大顺序
            int sort = insertMapper.countSort(doctor.getDeptCode());
            s = String.valueOf(sort + 1);
        }
        //设置顺序
        doctor.setSort(s);
        //新增医生
        insertMapper.insertDoctor(doctor);
    }

    /***
     * 判断用户是否重复
     * @param userDTO 用户
     * @return
     */
    public int countUser(UserDTO userDTO) {
        int count = insertMapper.countUser(userDTO.getUserName());
        return count;
    }

    /***
     * 新增用户
     * @param userDTO 用户
     */
    public void insertUser(UserDTO userDTO) {
        //设置当前日期
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userDTO.setCreateTime(df.format(new Date()));
        insertMapper.insertUser(userDTO);
    }

    /***
     * 判断数据库是否重复
     * @param dataSource 数据库
     * @return
     */
    public int countDatabase(DataSource dataSource) {
        return insertMapper.countDatabase(dataSource.getHospitalCode());
    }

    /***
     * 新增数据库
     * @param dataSource 数据库
     */
    public void insertDatabase(DataSource dataSource) {
        //设置当前日期
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dataSource.setCreateTime(df.format(new Date()));
        insertMapper.insertDatabase(dataSource);
    }

    /***
     * 批量增加医生头像
     * @param savePath 图片路径
     * @param doctorCode 医生编号
     */
    public void insertIcon(List<String> savePath, List<String> doctorCode) {
        for (int i = 0; i < savePath.size(); i++) {
            insertMapper.insertIcon(savePath.get(i), doctorCode.get(i));
        }
    }

    /***
     * 判断科室分类名是否重复
     * @param deptClass 科室分类
     * @return
     */
    public int repeatClassName(DeptClass deptClass) {
        int count = insertMapper.repeatClassName(deptClass.getDeptClassName());
        return count;
    }

    /***
     * 统计该科室名的科室数量
     * @param department 科室
     * @return
     */
    public int repeatDeptName(Department department) {
        int count = insertMapper.repeatDeptName(department.getDeptName());
        return count;
    }
}
