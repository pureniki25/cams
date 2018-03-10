package com.hongte.alms.base.collection.vo;

import com.hongte.alms.base.collection.entity.CollectionTrackLog;
import com.hongte.alms.common.util.Constant;

import java.util.Date;
import java.util.UUID;

/**
 * @author zengkun
 * @since 2018/2/1
 */

/**
 * 贷后跟踪记录Vo
 */
public class CollectionTrackLogVo extends CollectionTrackLog  {

//    private  String  rpcID;
    //期数
    private Integer periods;

    //排序ID
    private Integer sortId;

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    //设置默认值
    public void setDefaultVal() {
        if(this.getCreateTime()==null) {
            this.setCreateTime(new Date());
        }
        if(this.getRecordDate()==null) {
            this.setRecordDate(new Date());
        }
        if(this.getRecorderUser()==null) {
            this.setRecorderUser(Constant.DEV_DEFAULT_USER);
        }
/*        if(this.getTrackLogId()==null){
            this.setTrackLogId(UUID.randomUUID().toString());
        }*/
    }
    //设置默认值

    /**
     *
     * @param log
     * @param loginUserId  当前登录的用户ID
     */
    public static void setDefaultVal(CollectionTrackLog log,String loginUserId) {
        if(log.getCreateTime()==null) {
            log.setCreateTime(new Date());
        }
        if(log.getCreateUser()==null) {
//            log.setCreateUser(Constant.DEV_DEFAULT_USER);
            log.setCreateUser(loginUserId);
        }
//        if(log.getRecordDate()==null) {
            log.setRecordDate(new Date());
//        }
//        if(log.getRecorderUser()==null) {
            log.setRecorderUser(loginUserId);
//            log.setRecorderUser(Constant.DEV_DEFAULT_USER);
//        }

        log.setUpdateTime(new Date());
//        log.setUpdateUser(Constant.DEV_DEFAULT_USER);
        log.setUpdateUser(loginUserId);

/*        if(this.getTrackLogId()==null){
            this.setTrackLogId(UUID.randomUUID().toString());
        }*/
    }

    public Integer getSortId() {
        return sortId;
    }

    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }
}
