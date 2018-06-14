package com.hongte.alms.core.controller;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.base.collection.entity.Parametertracelog;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.feignClient.CollectionSynceToXindaiRemoteApi;
import com.hongte.alms.base.service.SysParameterService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.entity.CollectionTrackLog;
import com.hongte.alms.base.collection.service.CollectionTrackLogService;
import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.base.collection.vo.CollectionTrckLogReq;
import com.hongte.alms.base.entity.FiveLevelClassifyBusinessChangeLog;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.feignClient.service.EipOperateService;
import com.hongte.alms.base.service.BusinessParameterService;
import com.hongte.alms.base.service.FiveLevelClassifyBusinessChangeLogService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.vo.module.classify.ClassifyConditionVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 贷后跟踪记录表 前端控制器
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-01
 */
@RestController
@RequestMapping("/collectionTrackLog")
@Api(tags = "CollectionTrackLogController", description = "贷后跟踪记录相关接口")
public class CollectionTrackLogController {

    private static final Logger logger = LoggerFactory.getLogger(CollectionTrackLogController.class);

    @Autowired
    @Qualifier("CollectionTrackLogService")
    CollectionTrackLogService collectionTrackLogService;

    @Autowired
    @Qualifier("eipOperateServiceImpl")
    private EipOperateService eipOperateService;
    
    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    private RepaymentBizPlanListService repaymentBizPlanListService;
    
    @Autowired
    @Qualifier("BusinessParameterService")
    private BusinessParameterService businessParameterService;
    
    @Autowired
	@Qualifier("FiveLevelClassifyBusinessChangeLogService")
	private FiveLevelClassifyBusinessChangeLogService fiveLevelClassifyBusinessChangeLogService;

    @Autowired
    @Qualifier("SysParameterService")
    private SysParameterService  sysParameterService;

    @Autowired
    private CollectionSynceToXindaiRemoteApi collectionRemoteApi;


    @Autowired
    private LoginUserInfoHelper loginUserInfoHelper;

    @ApiOperation(value = "获取分页贷后跟踪记录 分页")
    @GetMapping("/selectCollectionTrackLogPage")
    @ResponseBody
    public PageResult<List<CollectionTrackLogVo>> selectCollectionTrackLogPage(@ModelAttribute CollectionTrckLogReq req){

        try{
            Page<CollectionTrackLogVo> pages = collectionTrackLogService.selectCollectionTrackLogByRbpId(req);

            List<CollectionTrackLogVo> list = pages.getRecords();

            for(int i=1;i<list.size()+1;i++){
                list.get(i-1).setSortId(i);
            }

            return PageResult.success(pages.getRecords(),pages.getTotal());
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }

    }




    @ApiOperation(value = "根据ID查找")
    @GetMapping("/selectById")
    @ResponseBody
    public Result<CollectionTrackLog> selectById(@RequestParam("trackLogId") String trackLogId){

        try{
            CollectionTrackLog log =  collectionTrackLogService.selectById(trackLogId);
            if(log==null){

                return Result.error("500","查询失败");
            }else{
            	String origBusinessId = repaymentBizPlanListService.selectById(log.getRbpId()).getOrigBusinessId();
				FiveLevelClassifyBusinessChangeLog changeLog = fiveLevelClassifyBusinessChangeLogService
						.selectOne(new EntityWrapper<FiveLevelClassifyBusinessChangeLog>()
								.eq("orig_business_id", origBusinessId).eq("valid_status", "1"));
				if (changeLog != null) {
					String borrowerConditionDesc = changeLog.getBorrowerConditionDesc();
					
					if (StringUtil.notEmpty(borrowerConditionDesc)) {
						String[] arrBorrower = borrowerConditionDesc.split(Constant.FIVE_LEVEL_CLASSIFY_SPLIT);
						log.setBorrowerConditionDescList(Arrays.asList(arrBorrower));
					}
					
					String guaranteeConditionDesc = changeLog.getGuaranteeConditionDesc();
					
					if (StringUtil.notEmpty(guaranteeConditionDesc)) {
						String[] arrBorrower = guaranteeConditionDesc.split(Constant.FIVE_LEVEL_CLASSIFY_SPLIT);
						log.setGuaranteeConditionDescList(Arrays.asList(arrBorrower));
					}
				}
				return Result.success(log);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }

    }

    @ApiOperation(value = "根据ID删除历史记录")
    @DeleteMapping("/deleteLog")
    public Result<Integer> deleteLog(@RequestParam("id") Integer trackLogId){

        try{
            // -- 删除信贷历史记录   开始  ------------
            CollectionTrackLog trackLog =  collectionTrackLogService.selectById(trackLogId);
            if(trackLog!=null &&trackLog.getXdIndexId()!=null){
                Result ret =  collectionRemoteApi.deleteXdCollectionLogById(trackLog.getXdIndexId());
                if(!ret.getCode().equals("1")){
                    logger.error("根据ID删除信贷的历史记录，失败： trackLog："+ JSON.toJSONString(trackLog) +"   失败原因："+ret.getMsg());
                    return Result.error("500","删除信贷历史贷后跟踪记录失败");
                }
            }
            // -- 删除信贷历史记录   结束  ------------

            boolean ret =  collectionTrackLogService.deleteById(trackLogId);

            if(ret){
                return Result.success(1);
            }else{
                return Result.error("500","删除失败");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }

    }

    @ApiOperation(value = "新增/修改历史记录")
    @PostMapping("/addOrUpdateLog")
    public Result<Integer> addOrUpdateLog(@RequestBody CollectionTrackLog log){

        com.ht.ussp.core.Result result = new com.ht.ussp.core.Result();

        result = eipOperateService.addProjectTract(log);

        try{
            collectionTrackLogService.addOrUpdateLog(log);

            try{
                // --------  将贷后跟踪记录同步到信贷  开始 -------------------
                RepaymentBizPlanList planList = repaymentBizPlanListService.selectById(log.getRbpId());
                planList.getAfterId();
                planList.getBusinessId();
                Parametertracelog parametertracelog = new Parametertracelog();
                parametertracelog.setId(log.getXdIndexId());
                parametertracelog.setCarBusinessId(planList.getBusinessId());
                parametertracelog.setCarBusinessAfterId( planList.getAfterId());
                parametertracelog.setTranceContent(log.getContent());

                LoginInfoDto loginInfoDto = loginUserInfoHelper.getUserInfoByUserId(log.getRecorderUser(),null);
                if(loginInfoDto == null){
                    parametertracelog.setTranceName("admin");
                }else{
                    parametertracelog.setTranceName(loginInfoDto.getBmUserId());
                }

                parametertracelog.setTranceDate(log.getRecordDate());
                LoginInfoDto creatUDto = loginUserInfoHelper.getUserInfoByUserId(log.getCreateUser(),null);
                if(creatUDto == null){
                    parametertracelog.setCreateUser("admin");
                }else{
                    parametertracelog.setCreateUser(creatUDto.getBmUserId());
                }
                parametertracelog.setCreateTime(log.getCreateTime());
                parametertracelog.setIsDelete(0);

                parametertracelog.setStatus(Integer.valueOf(log.getTrackStatusId()));
                parametertracelog.setStatusName(log.getTrackStatusName());
                Result<Integer> ret =  collectionRemoteApi.transferOneCollectionLogToXd(parametertracelog);

                if(ret == null || !ret.getCode().equals("1")){
                    String retStr = "ret为空";
                    if(ret!=null){
                        retStr =ret.getMsg();
                    }
                    logger.error("将贷后跟踪记录同步到信贷，失败： trackLog："+ JSON.toJSONString(log) +"   失败原因："+ retStr);
                    return Result.error("500","将贷后跟踪记录同步到信贷");
                }else{
                    log.setXdIndexId(ret.getData());
                    collectionTrackLogService.insertOrUpdate(log);
                }

                // --------  将贷后跟踪记录同步到信贷  结束 -------------------
            }catch (Exception e){
                logger.error("贷后跟踪记录数据同步到信贷，失败   parametertracelog："+JSON.toJSONString(log)+"          失败原因："+e.getMessage());
            }
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }

        if(!"0000".equals(result.getReturnCode())){
            return Result.error("400","推送平台未成功");
        }

        return Result.success(1);
    }
    
    @SuppressWarnings("unchecked")
	@PostMapping("/matchBusinessClassify")
    @ResponseBody
    public Result<String> matchBusinessClassify(@RequestBody Map<String, Object> paramMap) {
    	try {
    		if (paramMap.isEmpty()) {
				return Result.error("-500", "参数不能为空！");
			}
    		
    		String rbpId = (String) paramMap.get("rbpId");
    		List<String> borrowerConditionDescList = (List<String>) paramMap.get("borrowerConditionDescList");
    		List<String> guaranteeConditionDescList = (List<String>) paramMap.get("guaranteeConditionDescList");
    		
    		ClassifyConditionVO vo = new ClassifyConditionVO();
    		
    		if (StringUtil.notEmpty(rbpId)) {
				RepaymentBizPlanList planList = repaymentBizPlanListService.selectById(rbpId);
				if (planList != null) {
					vo.setBusinessId(planList.getOrigBusinessId());
				}
			}
    		
    		if (CollectionUtils.isNotEmpty(borrowerConditionDescList)) {
				vo.setMainBorrowerConditions(borrowerConditionDescList);
			}
    		
    		if (CollectionUtils.isNotEmpty(guaranteeConditionDescList)) {
				vo.setGuaranteeConditions(guaranteeConditionDescList);
			}
    		
    		vo.setOpSourse(Constant.FIVE_LEVEL_CLASSIFY_OP_SOUSE_TYPE_ALMS_LOG); 	//	操作源： 2、 贷后跟踪记录
    		
    		String className = businessParameterService.fiveLevelClassifyForBusiness(vo);
    		return Result.success(className);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Result.error("-500", "系统异常，匹配五级分类失败！");
		}
    }
}

