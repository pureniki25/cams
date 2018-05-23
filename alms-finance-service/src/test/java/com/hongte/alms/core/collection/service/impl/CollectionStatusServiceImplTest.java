package com.hongte.alms.core.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.dto.CollectionStatusCountDto;
import com.hongte.alms.base.collection.entity.Collection;
import com.hongte.alms.base.collection.entity.CollectionLogXd;
import com.hongte.alms.base.collection.enums.CollectionCrpTypeEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.enums.StaffPersonType;
import com.hongte.alms.base.collection.mapper.CollectionStatusMapper;
import com.hongte.alms.base.collection.service.CollectionLogXdService;
import com.hongte.alms.base.collection.service.CollectionService;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.collection.service.CollectionTimeSetService;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.service.SysJobConfigService;
import com.hongte.alms.base.vo.comm.SmsVo;
import com.hongte.alms.base.vo.module.CollectionReq;
import com.hongte.alms.finance.FinanceServiceApplication;
import com.ht.ussp.bean.LoginUserInfoHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author:喻尊龙
 * @date: 2018/3/20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceApplication.class)
public class CollectionStatusServiceImplTest {

    @Autowired
    private CollectionStatusMapper collectionStatusMapper;

    @Autowired
    @Qualifier("CollectionTimeSetService")
    private CollectionTimeSetService collectionTimeSetService;

    @Autowired
    @Qualifier("CollectionStatusService")
    private CollectionStatusService collectionStatusService;

    @Autowired
    @Qualifier("SysJobConfigService")
    private SysJobConfigService sysJobConfigService;

    @Autowired
    @Qualifier("CollectionService")
    private CollectionService collectionService;

    @Autowired
    private LoginUserInfoHelper loginUserInfoHelper;

    @Autowired
    private EipRemote eipRemote;
    @Autowired
    @Qualifier("CollectionLogXdService")
    private CollectionLogXdService collectionLogXdService;



    @Test
    public void autoSetBusinessStaff() throws Exception {
//        collectionStatusService.autoSetBusinessStaff();
        SmsVo vo = new SmsVo();
        Set<String> phones = new HashSet<>();
        phones.add("13699559437");
        vo.setPhones(phones);
        vo.setContent("123ceshi");
        eipRemote.sendSms(vo);
        System.out.println("success");
    }
    @Test
    public void select() throws Exception {

        CollectionReq req = new CollectionReq();
        req.setOffSet(0);
        req.setPageSize(10);

        int count = collectionService.queryNotTransferCollectionCount();
        System.out.println(count);

        count = collectionLogXdService.queryNotTransferCollectionLogCount();
        System.out.println(count);

        collectionLogXdService.queryNotTransferCollectionLog(req);

        List<Collection> list = collectionService.queryNotTransferCollection(req);
        System.out.println(list);

    }

    @Test
    public void test1(){
        List<String> phonePersons = new ArrayList<>();
        phonePersons.add("1243");
        List<CollectionStatusCountDto> phoneFollowPersonlist = collectionStatusMapper.selectAllPersons(
                StaffPersonType.VISIT_STAFF.getKey()
                ,phonePersons
                , CollectionStatusEnum.COLLECTING.getKey()
                , CollectionCrpTypeEnum.NORMAL.getKey()
        );
    }

    @Test
    public void collectionService(){

        Page<CollectionLogXd> pages = new Page<>();
        pages.setCurrent(1);
        pages.setSize(100);

        pages = collectionLogXdService.selectPage(pages,new EntityWrapper<CollectionLogXd>().and("business_id not in (SELECT business_id from tb_collection)"));

        System.out.println(pages.getTotal());

//        LoginInfoDto dto = loginUserInfoHelper.getUserInfoByUserId("","zengxin");
//        System.out.println(dto);
    }

}