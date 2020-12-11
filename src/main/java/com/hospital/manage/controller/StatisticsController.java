package com.hospital.manage.controller;

import com.hospital.manage.dto.ReplyDTO;
import com.hospital.manage.model.Report;
import com.hospital.manage.model.User;
import com.hospital.manage.service.DBChangeService;
import com.hospital.manage.service.StatisticsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private DBChangeService dbChangeServiceImpl;

    Logger logger = Logger.getLogger(this.getClass());

    /***
     * 网上咨询统计报表
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/statistics")
    public String Statistics(HttpServletRequest request) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "statistics";
    }

    /***
     * 分页查询数据重载
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/total", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> total(HttpServletRequest request,
                                     @RequestParam(value = "deptCode", required = false) String deptCode,
                                     @RequestParam(value = "doctorCode", required = false) String doctorCode,
                                     @RequestParam(value = "doctorName", required = false) String doctorName) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        Map<String, Object> map = new HashMap<String, Object>();
        //判断医生姓名是否重复
        if (statisticsService.repeatName(deptCode, doctorName) > 1 && (doctorCode == null || doctorCode == "")) {
            map.put("code", -1);
            return map;
        }
        Report report = new Report();
        //获取回复问题总数
        report.setReply(statisticsService.getReply(deptCode, doctorCode, doctorName));
        //获取未回复问题总数
        report.setUnReply(statisticsService.getUnReply(deptCode, doctorCode, doctorName));
        //获取按时回复问题总数
        report.setOnTime(statisticsService.getOnTime(deptCode, doctorCode, doctorName));
        //设置未按时回复的总数
        report.setLate(report.getReply() - report.getOnTime());
        map.put("code", 0);
        map.put("msg", "");
        map.put("report", report);
        return map;
    }

    /***
     * 分页查询数据重载
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/statisticsTable", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> statisticsTable(HttpServletRequest request,
                                               @RequestParam(value = "deptCode", required = false) String deptCode,
                                               @RequestParam(value = "doctorCode", required = false) String doctorCode,
                                               @RequestParam(value = "doctorName", required = false) String doctorName) throws Exception {
        //获取用户信息进行拦截
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return null;
        }
        //切换数据库
        String datasourceId = user.getHospitalCode();
        dbChangeServiceImpl.changeDb(datasourceId);
        Map<String, Object> map = new HashMap<String, Object>();
        //判断医生姓名是否重复
        if (statisticsService.repeatName(deptCode, doctorName) > 1 && (doctorCode == null || doctorCode == "")) {
            map.put("code", -1);
            return map;
        }
        Report report = new Report();
        //获取回复问题总数
        List<ReplyDTO> replyDTOS = statisticsService.getReplyDTO(deptCode, doctorCode, doctorName);
        map.put("code", 0);
        map.put("msg", "");
        map.put("data", replyDTOS);
        return map;
    }
}
