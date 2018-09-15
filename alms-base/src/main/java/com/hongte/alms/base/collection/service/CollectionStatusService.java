package com.hongte.alms.base.collection.service;

import com.hongte.alms.base.collection.entity.CollectionStatus;
import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.vo.StaffBusinessVo;
import com.hongte.alms.base.entity.CarBusinessAfter;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * 贷后催收状态表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-25
 */
public interface CollectionStatusService extends BaseService<CollectionStatus> {

    /**
     * 设置电催/上门催收人员(界面手动设置)
     * @param list 业务信息列表
     * @param staffUserId 催收人ID
     * @param describe 描述
     * @param setWayEnum 设置方式枚举
     * @return
     */
    public boolean setBusinessStaff(List<StaffBusinessVo> list,
                                    String staffUserId,
                                    String describe,
                                    String staffType,
                                    CollectionSetWayEnum setWayEnum);

    /**
     * 将电催、催收人员设置信息同步到信贷的函数
     * @param voList
     * @param staffUserId
     * @param describe
     * @param staffType
     * @return
     */
    public  boolean SyncBusinessColStatusToXindai(
            List<StaffBusinessVo> voList,
            String staffUserId,
            String describe ,
            String staffType
    );
    /**
     * 设置业务的贷后状态（移交法务、拖车登记、关闭 调用此方法）
     * @param businessId  业务ID
     * @param crpId    还款计划列表ID
     * @param describe    描述
     * @param setWayEnum  设置方式枚举
     * @return
     */
    public boolean setBussinessAfterStatus(
            String businessId,
            String crpId,
            String describe,
            CollectionStatusEnum  satusEnum,
            CollectionSetWayEnum setWayEnum);



    /**
     *
     * 自动移交：
     分配电催规则：
     1）默认逾期 1天 自动分配电催
     2）上一期分配给谁，这一期延续
     3）业务的第一期分配
     1））月还逾期   分配给跟进月还逾期数量最少的人员
     2））末期逾期   分配给跟进末期逾期数量最少的人员
     4）之前某一期移交诉讼则整个业务不分配电催和催收
     分配催收规则：
     1）默认逾期 31天 自动分配电催
     2）上一期分配给谁，这一期延续
     3）业务的第一期分配
     1））月还逾期   分配给跟进月还逾期数量最少的人员
     2））末期逾期   分配给跟进末期逾期数量最少的人员
     4）之前某一期移交诉讼则整个业务 不分配电催和催收
     移交诉讼规则：
     1）默认逾期91天 自动移交诉讼
     2）这个业务之前所有的期数都改成移交诉讼
     催收完成条件：
     1）被催收的那一期还款已还清
     2）整个业务已结清

     */
    public void autoSetBusinessStaff();


    /**
     * 自动移交法务
     */
    public void setBusinessToLaw();

    /**
     * 将指定业务移交法务
     * @param businessId
     */
    public void setOneBusinessToLaw(String businessId);

    //根据用户ID查找出用户跟进的业务列表
    public List<String> selectFollowBusinessIds(String userId);


    /**
     * 回退已关闭的催收
     * @param businessId
     * @return
     */
    public boolean  revokeClosedStatus(String businessId);
    
    /**
     * 获取最近一次已分配催收的CollectionStatus
     * @param businessId
     * @return
     */
    public CollectionStatus  getRecentlyCollectionStatus(String businessId,Integer collectionStatus);


//    /**
//     *
//     * @param carBusinessAfter
//     * @return
//     */
//    public boolean refreshByXinDaiPhoneUser(CarBusinessAfter carBusinessAfter);
//


}
