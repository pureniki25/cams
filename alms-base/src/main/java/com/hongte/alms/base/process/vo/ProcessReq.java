package com.hongte.alms.base.process.vo;

import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.vo.PageRequest;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.util.Date;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/2/11
 * 流程查询的数据访问Req
 */
public class ProcessReq  extends PageRequest {

    private String keyWord      		;   //关键字  (标题、业务编号、客户名称、发起人)
    private Date createTimeBegin		;	//发起时间  开始
    private Date createTimeEnd		;	//发起时间   结束
    private String processTypeId		;	//审批类型

    private Date finishTimeBegin	;	//结束时间  开始
    private Date finishTimeEnd		;//结束时间   结束
    private Date finishTimeRange	;	//结束时间   范围
    private Integer	 processStatus	; 	//审批状态,
    private String companyId		;//分公司

    private List<String> companyIds;//用户可查看的分公司列表

    //当前用户ID
    private String currentUserId;

    //发起用户列表
    private List<String> startUserIds;

    //需要我审批的数据查询标志位
    private boolean waitTpApproveFlage = false;

    //我的审批 界面类型
    private String reqPageeType; //waitToApprove 需要我审批的;    Approved 我已审批的;      SelfStart 我发起的;   CopySendToMe 抄送我的;  Search 审批查询;




    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = DateUtil.getThatDayBegin(createTimeBegin);
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = DateUtil.getDayEnd(createTimeEnd);
    }

    public String getProcessTypeId() {
        return processTypeId;
    }

    public void setProcessTypeId(String processTypeId) {
        this.processTypeId = processTypeId;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public boolean isWaitTpApproveFlage() {
        return waitTpApproveFlage;
    }

    public void setWaitTpApproveFlage(boolean waitTpApproveFlage) {
        this.waitTpApproveFlage = waitTpApproveFlage;
    }

    public String getReqPageeType() {
        return reqPageeType;
    }

    public void setReqPageeType(String reqPageeType) {
        this.reqPageeType = reqPageeType;
    }

    public Date getFinishTimeBegin() {
        return finishTimeBegin;
    }

    public void setFinishTimeBegin(Date finishTimeBegin) {
        this.finishTimeBegin = DateUtil.getThatDayBegin(finishTimeBegin);
    }

    public Date getFinishTimeEnd() {
        return finishTimeEnd;
    }

    public void setFinishTimeEnd(Date finishTimeEnd) {
        this.finishTimeEnd = DateUtil.getThatDayEnd(finishTimeEnd);
    }

    public Date getFinishTimeRange() {
        return finishTimeRange;
    }

    public void setFinishTimeRange(Date finishTimeRange) {
        this.finishTimeRange = finishTimeRange;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<String> getStartUserIds() {
        return startUserIds;
    }

    public void setStartUserIds(List<String> startUserIds) {
        this.startUserIds = startUserIds;
    }

    public List<String> getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(List<String> companyIds) {
        this.companyIds = companyIds;
    }
}
