package com.hongte.alms.open.controller;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.common.open.OpenJsonConvert;
import com.hongte.alms.common.open.OpenRequestContent;
import com.hongte.alms.open.vo.BusinessRepaymentPlan;
import com.hongte.alms.open.vo.ProjectRepaymentPlan;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
/**
 * Created by huanghui on 2018/1/15.
 * 还款计划请求控制器
 */
@RestController
@RequestMapping("")
@Api(tags = "RepaymentPlanController", description = "还款计划相关", hidden = true)
public class RepaymentPlanController {
//    @Value("${openService.tripleDESKey}")
    private String tripleDESKey;

    /**
     * Created by huanghui on 2018/1/15.
     * 业务还款计划接受方法
     */
    @PostMapping("/RepaymentPlan/businessRepayment")
    public String businessRepayment(@RequestBody OpenRequestContent requestContent)
    {
        try {
            BusinessRepaymentPlan bsinessRepaymentPlan = OpenJsonConvert.deserializeTripleDESEncryptText(requestContent.getStrJson(), tripleDESKey, BusinessRepaymentPlan.class);
            String result = JSON.toJSONString(bsinessRepaymentPlan);
            return result;
        }
        catch(Exception ex) {
            return ex.getMessage() + ex.getStackTrace();
        }
    }

    /**
     * Created by huanghui on 2018/1/15.
     * 项目还款计划接受方法
     */
    @PostMapping("/RepaymentPlan/projectRepayment")
    public String projectRepayment(@RequestBody OpenRequestContent requestContent)
    {
        try {
            ProjectRepaymentPlan projectRepaymentPlan = OpenJsonConvert.deserializeTripleDESEncryptText(requestContent.getStrJson(), tripleDESKey, ProjectRepaymentPlan.class);
            String result = JSON.toJSONString(projectRepaymentPlan);
            return result;
        }
        catch(Exception ex) {
            return ex.getMessage() + ex.getStackTrace();
        }
    }
}
