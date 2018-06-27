package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.SysApiCallFailureRecord;
import com.hongte.alms.base.enums.AlmsServiceNameEnums;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.SysApiCallFailureRecordMapper;
import com.hongte.alms.base.service.SysApiCallFailureRecordService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * API调用失败记录表 服务实现类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-26
 */
@Service("SysApiCallFailureRecordService")
public class SysApiCallFailureRecordServiceImpl extends BaseServiceImpl<SysApiCallFailureRecordMapper, SysApiCallFailureRecord> implements SysApiCallFailureRecordService {

	@Autowired
	private SysApiCallFailureRecordMapper sysApiCallFailureRecordMapper;

	@Override
	public List<SysApiCallFailureRecord> queryCallFailedDataByApiCode(String apiCode) {
		return sysApiCallFailureRecordMapper.queryCallFailedDataByApiCode(apiCode);
	}

    @Override
    public void save(AlmsServiceNameEnums moduleName, String apiCode, String apiName, String refId, String apiParamPlaintext,
                     String apiParamCiphertext, String apiReturnInfo, String targetUrl,  String createUser) {
        SysApiCallFailureRecord record = new SysApiCallFailureRecord();
        record.setModuleName(moduleName.getName());
        record.setApiCode(apiCode);
        record.setApiName(apiName);
        record.setRefId(refId);
        record.setApiParamPlaintext(apiParamPlaintext);
        record.setApiParamCiphertext(apiParamCiphertext);
        record.setApiReturnInfo(apiReturnInfo);
        record.setTargetUrl(targetUrl);
        record.setRetryCount(0);
        record.setRetrySuccess(0);
        record.setCreateUser(createUser);
        record.setCraeteTime(new Date());
        boolean ret =  super.insert(record);
        if(!ret){
            throw new ServiceRuntimeException("保存失败");
        }
    }
}
