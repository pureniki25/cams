package com.hongte.alms.base.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.collection.dto.CollectionStatusCountDto;
import com.hongte.alms.base.collection.entity.CollectionTimeSet;
import com.hongte.alms.base.collection.enums.CollectionCrpTypeEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.enums.StaffPersonType;
import com.hongte.alms.base.collection.mapper.CollectionStatusMapper;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.collection.service.CollectionTimeSetService;
import com.hongte.alms.base.entity.SysJobConfig;
import com.hongte.alms.base.enums.ColTypeEnum;
import com.hongte.alms.base.enums.JobConfigEnums;
import com.hongte.alms.base.service.SysJobConfigService;
import com.hongte.alms.core.CoreServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author:喻尊龙
 * @date: 2018/3/20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreServiceApplication.class)
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



    @Test
    public void autoSetBusinessStaff() throws Exception {
        collectionStatusService.autoSetBusinessStaff();
    }
    @Test
    public void select() throws Exception {

        SysJobConfig config =  sysJobConfigService.getCanExecuteConfig(JobConfigEnums.JobConfigType.SET_BUSINESS_COL.getValue());
        System.out.println(config);
        config.setLastRunTime(new Date());
        sysJobConfigService.updateById(config);
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

}