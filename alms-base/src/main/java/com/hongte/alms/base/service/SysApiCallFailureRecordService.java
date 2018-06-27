package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.SysApiCallFailureRecord;
import com.hongte.alms.base.enums.AlmsServiceNameEnums;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * API调用失败记录表 服务类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-26
 */
public interface SysApiCallFailureRecordService extends BaseService<SysApiCallFailureRecord> {

    List<SysApiCallFailureRecord> queryCallFailedDataByApiCode(String apiCode);

    /**
     * 新增保存 API调用失败记录
     *
     * @param moduleName         所属模块
     * @param apiCode            接口编码
     * @param apiName            接口名
     * @param refId              关联的业务标识
     * @param apiParamPlaintext  接口参数明文串
     * @param apiParamCiphertext 接口参数密文串
     * @param apiReturnInfo      接口返回信息
     * @param targetUrl          请求接口url
     * @param createUser         创建人
     * @author 张贵宏
     */
    void save(AlmsServiceNameEnums moduleName, String apiCode, String apiName, String refId, String apiParamPlaintext,
              String apiParamCiphertext, String apiReturnInfo, String targetUrl, String createUser);

}
