package com.hospital.manage.service;

import com.hospital.manage.dto.FunctionDTO;
import com.hospital.manage.mapper.FunctionMapper;
import com.hospital.manage.model.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunctionService {
    @Autowired
    private FunctionMapper functionMapper;

    /***
     * 获取所有的功能信息
     * @return
     */
    public List<FunctionDTO> getFunctionDTO() {
        //获取所有功能信息
        List<FunctionDTO> functionDTOS = functionMapper.getFunctions();
        String[] hospital = null;
        //获取所有功能的所属医院
        for (int i = 0; i < functionDTOS.size(); i++) {
            //获取该功能的所属医院
            hospital = functionMapper.getHospital(functionDTOS.get(i).getFunctionId());
            functionDTOS.get(i).setHospital(hospital);
        }
        return functionDTOS;
    }

    /***
     * 获取父级功能菜单
     * @return
     */
    public List<Function> getParent() {
        return functionMapper.getParent();
    }

    /***
     * 新增功能
     * @param functionDTO 功能
     * @return
     */
    public String insert(FunctionDTO functionDTO) {
        //判断功能url是否重复
        if (functionMapper.countUrl(functionDTO.getFunctionUrl()) > 0 && functionDTO.getFunctionUrl() != null && !functionDTO.getFunctionUrl().equals("")) {
            return "-1";
        } else {
            //新增功能
            functionMapper.insert(functionDTO);
            //获取新增的功能id
            Integer functionId = functionDTO.getFunctionId();
            for (int i = 0; i < functionDTO.getHospital().length; i++) {
                //新增功能与医院间关系
                functionMapper.insertRelation(functionId, functionDTO.getHospital()[i]);
            }
            return "1";
        }
    }

    /***
     * 删除功能
     * @param functionId 功能ID
     * @return
     */
    public String delete(Integer functionId) {
        //删除功能信息
        functionMapper.deleteFunction(functionId);
        //删除功能对应关系
        functionMapper.deleteRelation(functionId);
        return "1";
    }

    /***
     * 编辑功能
     * @param functionDTO 功能
     * @return
     */
    public String edit(FunctionDTO functionDTO) {
        //判断功能名是否重复
        if (functionMapper.countName(functionDTO.getFunctionName()) > 0 && !functionDTO.getOriginName().equals(functionDTO.getFunctionName())) {
            return "-1";
        }
        //判断功能url是否重复
        else if (functionMapper.countUrl(functionDTO.getFunctionUrl()) > 0 && !functionDTO.getOriginUrl().equals(functionDTO.getFunctionUrl())) {
            return "-1";
        } else {
            //编辑功能信息
            functionMapper.edit(functionDTO);
            //删除之前功能对应的关系
            functionMapper.deleteRelation(functionDTO.getFunctionId());
            for (int i = 0; i < functionDTO.getHospital().length; i++) {
                //新增更新后的功能对应的关系
                functionMapper.insertRelation(functionDTO.getFunctionId(), functionDTO.getHospital()[i]);
            }
            return "1";
        }
    }
}
