package com.hongte.alms.finance.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.collection.entity.CollectionPersonSet;
import com.hongte.alms.base.collection.entity.CollectionPersonSetDetail;
import com.hongte.alms.base.collection.service.CollectionPersonSetDetailService;
import com.hongte.alms.base.collection.service.CollectionPersonSetService;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.vo.module.CollectionStrategySinglePersonSettingReq;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.finance.service.CollectionStrategyPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/17
 */
@Service
public class CollectionStrategyPersonServiceImpl implements CollectionStrategyPersonService {

    @Autowired
    @Qualifier("CollectionPersonSetService")
    private CollectionPersonSetService collectionPersonSettingService;


    @Autowired
    @Qualifier("BasicCompanyService")
    BasicCompanyService basicCompanyService;

    @Autowired
    @Qualifier("CollectionPersonSetDetailService")
    private CollectionPersonSetDetailService collectionPersonSetDetailService;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveStrategyPerson(CollectionStrategySinglePersonSettingReq req, HttpHeaders headers) {
        System.out.println(req.getCollectionGroup1Users());
        List<String> collectionGroup1Users = new ArrayList<>();
        List<String> collectionGroup2Users = new ArrayList<>();
        List<String> companyList = new ArrayList<>();
        collectionGroup1Users = req.getCollectionGroup1Users();
        collectionGroup2Users = req.getCollectionGroup2Users();
        companyList = req.getCompanyId();


        for (String com:companyList) {
            CollectionPersonSet personSet = collectionPersonSettingService.selectOne(new EntityWrapper<CollectionPersonSet>().eq("company_code",com));
            if(personSet !=null){
                personSet.setUpdateTime(new Date());
                personSet.setUpdateUser("admin");
                personSet.setCreatUser("admin");
                collectionPersonSettingService.update(personSet,new EntityWrapper<CollectionPersonSet>().eq("col_person_id",personSet.getColPersonId()));
            }else {
                BasicCompany basicCompany = basicCompanyService.selectOne(new EntityWrapper<BasicCompany>().eq("area_id",com).eq("area_level", AreaLevel.COMPANY_LEVEL.getKey()));
                personSet = new CollectionPersonSet();
                personSet.setAreaCode(basicCompany.getAreaPid());
                personSet.setCompanyCode(com);
                personSet.setCreateTime(new Date());
                personSet.setCreatUser("admin");
                collectionPersonSettingService.insert(personSet);
            }

            String colPersonId = personSet.getColPersonId();

            List<CollectionPersonSetDetail> personSetDetails = new ArrayList<>();

        /*
        构建第一组人员详情设置
         */
            for (String firstGroup:collectionGroup1Users ) {
                CollectionPersonSetDetail personSetDetail = new CollectionPersonSetDetail();
                personSetDetail.setColPersonId(colPersonId);
                personSetDetail.setTeam(1);
                personSetDetail.setUserId(firstGroup);
                personSetDetail.setCreateUser("admin");
                personSetDetail.setCreateTime(new Date());
                personSetDetails.add(personSetDetail);
            }

        /*
        构建第二组人员详情设置
         */
            for (String secendGroup:collectionGroup2Users ) {

                CollectionPersonSetDetail personSetDetail = new CollectionPersonSetDetail();
                personSetDetail.setColPersonId(colPersonId);
                personSetDetail.setTeam(2);
                personSetDetail.setUserId(secendGroup);
                personSetDetail.setCreateUser("admin");
                personSetDetail.setCreateTime(new Date());
                personSetDetails.add(personSetDetail);
            }

            collectionPersonSetDetailService.delete(new EntityWrapper<CollectionPersonSetDetail>().eq("col_person_id",colPersonId));
            collectionPersonSetDetailService.insertBatch(personSetDetails);
        }


        return Result.success();
    }
}
