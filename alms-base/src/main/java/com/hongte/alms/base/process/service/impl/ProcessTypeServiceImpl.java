package com.hongte.alms.base.process.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.hongte.alms.base.process.entity.ProcessType;
import com.hongte.alms.base.process.mapper.ProcessTypeMapper;
import com.hongte.alms.base.process.service.ProcessTypeService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 流程类型 服务实现类
 * </p>
 *
 * @author 伦惠峰
 * @since 2018-01-13
 */
@Service("ProcessTypeService")
public class ProcessTypeServiceImpl extends BaseServiceImpl<ProcessTypeMapper, ProcessType> implements ProcessTypeService {
    @Autowired
    private  ProcessTypeMapper processTypeMapper;

    /**
     * TODO<获取审核类型><br>
     *
     * @param typeCode 流程类型编码
     * @return com.ht.litigation.service.entity.ProcessType 流程类型
     * @author 伦惠峰
     * @Date 2018/1/13 17:03
     */
    @Override
    public  ProcessType  getProcessTypeByCode(String typeCode)
    {
        ProcessType processType=null;
        List<ProcessType> processTypeList=  processTypeMapper.selectList(
                new EntityWrapper<ProcessType>().eq("type_code",typeCode));
         if(processTypeList!=null&&processTypeList.size()>0)
         {
             processType=processTypeList.get(0);
         }
        return processType;
    }

    /**
     * TODO<一句话功能描述><br>
     * TODO<功能详细描述><br>
     * 根据流程类型ID,返回流程类型实体
     * @param typeID
     * @return com.ht.litigation.service.entity.ProcessType
     * @author 伦惠峰
     * @Date 2018/1/16 14:44
     */
    @Override
    public  ProcessType  getProcessTypeByID(String typeID)
    {

        ProcessType processType=null;
        List<ProcessType> processTypeList=  processTypeMapper.selectList(
                new EntityWrapper<ProcessType>().eq("type_id",typeID));
        if(processTypeList!=null&&processTypeList.size()>0)
        {
            processType=processTypeList.get(0);
        }
        return processType;
    }

    /**
     *返回所有流程类型列表
     * @author 伦惠峰
     * @Date 2018/1/19 14:51
     */
    @Override
    public List<ProcessType> getProcessTypeList() {
        List<ProcessType> processTypeList=null;
        processTypeList=processTypeMapper.selectList(new EntityWrapper<ProcessType>().orderBy("create_user"));
        return processTypeList;
    }
}
