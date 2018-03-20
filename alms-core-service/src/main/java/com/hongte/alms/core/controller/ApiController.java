package com.hongte.alms.core.controller;




import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RenewalBusiness;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RenewalBusinessService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.SignUtil;
import feign.Feign;
import feign.Retryer;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * api对接配置 前端控制器
 * </p>
 *
 * @author 伦惠峰
 * @since 2018-01-30
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    private Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    @Qualifier("RenewalBusinessService")
    private RenewalBusinessService renewalBusinessService;

    @Autowired
    @Qualifier("BasicBusinessService")
    private BasicBusinessService basicBusinessService;

    @Value(value = "${xiaoDai.domain}")
    private String xindaiDomain;

    @Value(value = "${xiaoDai.afterLoanKey}")
    private String afterLoanKey;

    @Value(value = "${xiaoDai.afterLoanSecret}")
    private String afterLoanSecret;


    @Value(value="${ht.test.txt}")
    private  String txt;

    @RequestMapping("txt")
    @ApiOperation(value = "获取小贷的图片查看")
    public String getXindaiThumbnailView(){
            return txt;
    }


    @RequestMapping("getXindaiThumbnailView")
    @ApiOperation(value = "获取小贷的图片查看")
    public Result<String> getXindaiThumbnailView(String businessId) throws Exception {
        if(businessId==null||businessId.isEmpty())
        {
            return Result.error("500", "业务信息不存在");
        }
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String appKey = this.afterLoanKey;
            String appSecret = this.afterLoanSecret;
            Map<String, String> params = new HashMap<String, String>();
            params.put("businessId", businessId);
            params.put("timestamp", timestamp);
            String sign = SignUtil.signTopRequest(params, appSecret, "MD5");
            String xiaoDaiThumbnailUrl = xindaiDomain + "CommonArea/OpenPhoto/ThumbnailView/" + businessId + "?appKey=" + appKey + "&timestamp=" + timestamp + "&sign=" + sign;
            return Result.success(xiaoDaiThumbnailUrl);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }
    }

    @RequestMapping("getXindaiHouseView")
    @ApiOperation(value = "获取信贷房贷业务详情")
    public Result<String> getXindaiHouseView(String businessId) throws Exception {
        if(businessId==null||businessId.isEmpty())
        {
            return Result.error("500", "业务信息不存在");
        }
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String appKey = this.afterLoanKey;
            String appSecret = this.afterLoanSecret;
            Map<String, String> params = new HashMap<String, String>();
            params.put("businessId", businessId);
            params.put("timestamp", timestamp);
            String sign = SignUtil.signTopRequest(params, appSecret, "MD5");
            String xindaiHouseViewUrl = xindaiDomain + "Operation/OpenHouseView/Basic/" + businessId + "?appKey=" + appKey + "&timestamp=" + timestamp + "&sign=" + sign;
            return Result.success(xindaiHouseViewUrl);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }
    }

    @RequestMapping("getXindaiCarView")
    @ApiOperation(value = "获取信贷车贷业务详情")
    public Result<String> getXindaiCarView(String businessId) throws Exception {
        if(businessId==null||businessId.isEmpty())
        {
            return Result.error("500", "业务信息不存在");
        }
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String appKey = this.afterLoanKey;
            String appSecret = this.afterLoanSecret;
            Map<String, String> params = new HashMap<String, String>();
            params.put("businessId", businessId);
            params.put("timestamp", timestamp);
            String sign = SignUtil.signTopRequest(params, appSecret, "MD5");
            String xindaiHouseViewUrl = xindaiDomain + "Operation/OpenCarView/Basic/" + businessId + "?appKey=" + appKey + "&timestamp=" + timestamp + "&sign=" + sign;
            return Result.success(xindaiHouseViewUrl);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }
    }

    @RequestMapping("getXindaiDeferView")
    @ApiOperation(value = "获取信贷展期业务详情")
    public Result<String> getXindaiDeferView(String businessId) throws Exception {
        if(businessId==null||businessId.isEmpty())
        {
            return Result.error("500", "业务信息不存在");
        }
        try {
            RenewalBusiness renewalBusiness = renewalBusinessService.selectOne(new EntityWrapper<RenewalBusiness>().eq("renewal_business_id",businessId));
            BasicBusiness business = basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id",businessId));
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String appKey = this.afterLoanKey;
            String appSecret = this.afterLoanSecret;
            Map<String, String> params = new HashMap<String, String>();
            params.put("businessId", renewalBusiness.getOriginalBusinessId());
            params.put("extensionId", businessId);
            params.put("timestamp", timestamp);
            String sign = SignUtil.signTopRequest(params, appSecret, "MD5");
            int carType=9;
            int houseType=11;
            if (business.getBusinessType() == carType) {
                String xindaiCarDeferUrl = xindaiDomain + "Operation/OpenDeferTrace/DetailDefer?" + "businessId=" + renewalBusiness.getOriginalBusinessId() + "&extensionId=" + businessId + "&appKey=" + appKey + "&timestamp=" + timestamp + "&sign=" + sign;
                return Result.success(xindaiCarDeferUrl);
            }
            if (business.getBusinessType() == houseType) {
                String xindaiHouseDeferUrl = xindaiDomain + "Operation/OpenDeferTrace/DetailHouseDefer?" + "businessId=" + renewalBusiness.getOriginalBusinessId() + "&extensionId=" + businessId + "&appKey=" + appKey + "&timestamp=" + timestamp + "&sign=" + sign;
                return Result.success(xindaiHouseDeferUrl);
            }
            throw new UnsupportedOperationException();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }
    }
}