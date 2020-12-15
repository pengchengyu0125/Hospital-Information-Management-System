package com.hospital.manage.controller;

import com.hospital.manage.model.User;
import com.hospital.manage.service.DBChangeService;
import com.hospital.manage.service.InsertService;
import com.hospital.manage.tools.FileUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 图片上传Controller
 */
@Controller
public class UploadController {

    @Autowired
    private DBChangeService dbChangeServiceImpl;

    @Autowired
    private InsertService insertService;

    Logger logger = Logger.getLogger(this.getClass());

    /***
     * 图片上传
     * @param files 图片
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    public Map<String, Object> uploadImg(@RequestParam(value = "file", required = false) MultipartFile[] files,
                                         HttpServletRequest request) throws IOException {
        String name = null;
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getOriginalFilename();
            name = fileName;
            //设置文件上传路径
            String filePath = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/static/images/");
            try {
                FileUtil.uploadFile(files[i].getBytes(), filePath, fileName);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("src", "images/" + name);
        return map;
    }

    /***
     * 批量上传
     * @param savePath 图片路径
     * @param doctorCode 医生编号
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/batchUpload", method = RequestMethod.POST)
    public Map<String, Object> batchUpload(@RequestParam(value = "savePath[]", required = false) List<String> savePath,
                                           @RequestParam(value = "doctorCode[]", required = false) List<String> doctorCode,
                                           HttpServletRequest request) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        //写入图片路径和医生编号
        insertService.insertIcon(savePath, doctorCode);
        logger.info("用户" + user.getUserName() + "批量增加医生头像");
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        return map;
    }
}