package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.Api;
import com.hongte.alms.base.mapper.ApiMapper;
import com.hongte.alms.base.service.ApiService;
import com.hongte.alms.base.vo.module.api.XiaodaiParamRequest;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * <p>
 * api对接配置 服务实现类
 * </p>
 *
 * @author dengzhiming
 * @since 2018-03-01
 */
@Service("ApiService")
public class ApiServiceImpl extends BaseServiceImpl<ApiMapper, Api> implements ApiService {


    /**
     * 返回对接小贷token的信息
     *
     * @return java.lang.String
     * @author 伦惠峰
     * @Date 2018/1/31 15:27
     */
    @Override
    public String getXiaoDaiToken() {
        String xiaoDaiToken = "";
        Api xiaoDaiApi = this.selectOne(new EntityWrapper<Api>().eq("apikey", "afterloan_xiaodai"));
        if (xiaoDaiApi != null) {
            xiaoDaiToken = xiaoDaiApi.getToken();
        }
        return xiaoDaiToken;
    }

    /**
     * 返回登录小贷系统页面请求包
     *
     * @param functionCode 请求页面的标识
     * @param bussineId    业务编码
     * @return com.ht.litigation.service.vo.LawSysRequest
     * @author 伦惠峰
     * @Date 2018/1/30 16:32
     */
    @Override
    public XiaodaiParamRequest getXiaodaiParamRequest(String functionCode, String bussineId, String userId) throws Exception {
      /*  String requestId = UUID.randomUUID().toString();
        String time = DateTime.getNowTimeStamp();
        String xiaoDaiToken = getXiaoDaiToken();
        if (functionCode == null || functionCode.equals("")) {
            throw new Exception("functionCode为空！");
        }
        if(bussineId==null||bussineId.equals(""))
        {
            throw new Exception("bussineId为空!");
        }
        if(xiaoDaiToken==null||xiaoDaiToken.equals(""))
        {
            throw new Exception("xiaoDaiToken为空!");
        }
        String message=requestId+xiaoDaiToken+time+bussineId+userId;
        String sign= DESC.getMD5(message);
        XiaodaiParamRequest paramRequest = new XiaodaiParamRequest();
        paramRequest.setRequestId(requestId);
        paramRequest.setTime(time);
        paramRequest.setFunctionCode(functionCode);
        paramRequest.setBussineId(bussineId);
        paramRequest.setUserId(userId);
        paramRequest.setSign(sign);
        return paramRequest;*/
      return null;
    }
}
