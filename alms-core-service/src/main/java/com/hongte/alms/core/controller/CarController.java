package com.hongte.alms.core.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.enums.CarStatusEnums;
import com.hongte.alms.base.assets.car.service.CarService;
import com.hongte.alms.base.assets.car.vo.*;
import com.hongte.alms.base.baseException.AlmsBaseExcepiton;
import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.collection.vo.StaffBusinessVo;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.AuctionStatusEnums;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.entity.ProcessLog;
import com.hongte.alms.base.process.entity.ProcessType;
import com.hongte.alms.base.process.enums.ProcessStatusEnums;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.service.ProcessLogService;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.service.ProcessTypeService;
import com.hongte.alms.base.process.service.ProcessTypeStepService;
import com.hongte.alms.base.process.vo.ProcessSaveReq;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.CarAuctionBidderService;
import com.hongte.alms.base.service.CarAuctionRegService;
import com.hongte.alms.base.service.CarAuctionService;
import com.hongte.alms.base.service.CarBasicService;
import com.hongte.alms.base.service.CarConvBusAplyService;
import com.hongte.alms.base.service.CarDetectionService;
import com.hongte.alms.base.service.CarDragService;
import com.hongte.alms.base.service.CarReturnRegService;
import com.hongte.alms.base.service.DocService;
import com.hongte.alms.base.service.DocTmpService;
import com.hongte.alms.base.service.DocTypeService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.SysCityService;
import com.hongte.alms.base.service.SysCountyService;
import com.hongte.alms.base.service.SysProvinceService;
import com.hongte.alms.base.service.SysUserRoleService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.EasyPoiExcelExportUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.core.vo.modules.car.CarDragDoc;
import com.hongte.alms.core.vo.modules.car.CarDragRegistrationBusinessVo;
import com.hongte.alms.core.vo.modules.car.CarDragRegistrationInfo;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import com.ht.ussp.util.BeanUtils;
import com.ht.ussp.util.DateUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * <p>
 * 贷后移交前端控制器
 * </p>
 *
 * @since 2018-01-25
 */
@RestController
@RequestMapping("/car")
@Api(tags = "CarController", description = "资产管理-车辆管理", hidden = true)
public class CarController {
    private Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private LoginUserInfoHelper loginUserInfoHelper;

    @Autowired
    @Qualifier("CarService")
    private CarService carService;
    
    @Autowired
    @Qualifier("CarBasicService")
    private CarBasicService carBasicService;
    
    @Autowired
    @Qualifier("CarDetectionService")
    private CarDetectionService carDetectionService;
    
    @Autowired
    @Qualifier("CarDragService")
    private CarDragService carDragService;
    
    @Autowired
    @Qualifier("BasicBusinessService")
    private BasicBusinessService basicBusinessService;
    
    @Autowired
    @Qualifier("SysProvinceService")
    private SysProvinceService sysProvinceService;
    
    @Autowired
    @Qualifier("SysCityService")
    private SysCityService sysCityService;
    
    @Autowired
    @Qualifier("SysCountyService")
    private SysCountyService sysCountyService;
    
    @Autowired
    @Qualifier("DocService")
    private DocService docService;
    
    @Autowired
    @Qualifier("DocTmpService")
    private DocTmpService docTmpService;
    
    @Autowired
    @Qualifier("DocTypeService")
    private DocTypeService docTypeService;
    
    @Autowired
    @Qualifier("CarReturnRegService")
    private CarReturnRegService carReturnRegService ;
    
    @Autowired
    @Qualifier("RepaymentBizPlanService")
    private RepaymentBizPlanService repaymentBizPlanService;
    
    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    private RepaymentBizPlanListService repaymentBizPlanListService;
    
    @Autowired
    @Qualifier("RepaymentBizPlanListDetailService")
    private RepaymentBizPlanListDetailService  repaymentBizPlanListDetailService;

    
    @Autowired
    @Qualifier("BizOutputRecordService")
    private BizOutputRecordService bizOutputRecordService;
    
    
    @Autowired
    @Qualifier("CarAuctionService")
    private CarAuctionService carAuctionService;
    
    @Autowired
    @Qualifier("ProcessTypeStepService")
    private ProcessTypeStepService processTypeStepService;
    
    @Autowired
    @Qualifier("ProcessTypeService")
    private ProcessTypeService processTypeService;
    
    @Autowired
    @Qualifier("ProcessService")
    private ProcessService processService;
   
    @Autowired
    @Qualifier("ProcessLogService")
    private ProcessLogService processLogService;
    
    
    @Autowired
    @Qualifier("CarConvBusAplyService")
    private CarConvBusAplyService carConvBusAplyService;
    
    @Autowired
    @Qualifier("CarAuctionRegService")
    private CarAuctionRegService carAuctionRegService;
    
    @Autowired
    @Qualifier("CarAuctionBidderService")
    private CarAuctionBidderService carAuctionBidderService;
    
    @Autowired
    @Qualifier("SysUserRoleService")
    private SysUserRoleService sysUserRoleService;


    @Autowired
	@Qualifier("CollectionStatusService")
    private CollectionStatusService collectionStatusService;
    
    @Value("${ht.auction.delayed.min:10}")
    private Integer delayedMin;//延长时间(分钟)
    
	@ApiOperation(value="获取拖车登记业务基本信息")
	@GetMapping("getCarDragRegistrationBusinessInfo")
	public Result<CarDragRegistrationBusinessVo> getCarDragRegistrationBusinessInfo(@RequestParam("businessId") String businessId)
	{
		try {
			BasicBusiness basicBusiness = basicBusinessService.selectOne(new EntityWrapper().eq("business_id", businessId));
			if(basicBusiness==null)
			{
				return Result.error("500","业务信息不存在");
			}
			CarBasic carBasic = carBasicService.selectOne(new EntityWrapper().eq("business_id", businessId));
			if(carBasic==null)
			{
				return Result.error("500","车辆信息不存在");
			}
			CarDetection carDetection = carDetectionService.selectOne(new EntityWrapper().eq("business_id", businessId));
			if(carDetection==null)
			{
				return Result.error("500","车辆评估信息不存在");
			}
			LoginInfoDto detectionUser=loginUserInfoHelper.getUserInfoByUserId("",carBasic.getCreateUser());
			if(detectionUser==null)
			{
				return Result.error("500","获取车辆评估人失败");

			}
			LoginInfoDto loginUser=loginUserInfoHelper.getLoginInfo();
			if(loginUser==null)
			{
				return Result.error("500","获取登录用户失败");
			}
            String detectionUsername=detectionUser!=null?detectionUser.getUserName():"";
			CarDragRegistrationBusinessVo carDragRegistrationBusinessVo = new CarDragRegistrationBusinessVo();
			carDragRegistrationBusinessVo.setBusinessId(basicBusiness.getBusinessId());
			carDragRegistrationBusinessVo.setCustomerName(basicBusiness.getCustomerName());
			carDragRegistrationBusinessVo.setBorrowMoney(basicBusiness.getBorrowMoney());
			carDragRegistrationBusinessVo.setBorrowLimit(basicBusiness.getBorrowLimit());
			carDragRegistrationBusinessVo.setBorrowLimitUnit(basicBusiness.getBorrowLimitUnit());
			carDragRegistrationBusinessVo.setRepaymentTypeId(basicBusiness.getRepaymentTypeId());
			carDragRegistrationBusinessVo.setBorrowRate(basicBusiness.getBorrowRate());
			carDragRegistrationBusinessVo.setBorrowRateUnit(basicBusiness.getBorrowRateUnit());
			carDragRegistrationBusinessVo.setLicensePlateNumber(carBasic.getLicensePlateNumber());
			carDragRegistrationBusinessVo.setModel(carBasic.getModel());
			carDragRegistrationBusinessVo.setVin(carBasic.getVin());
			carDragRegistrationBusinessVo.setEvaluationTime(carDetection.getCreateTime());
			carDragRegistrationBusinessVo.setEvaluationAmount(carDetection.getEvaluationAmount());
			carDragRegistrationBusinessVo.setEvaluationUser(detectionUsername);
			carDragRegistrationBusinessVo.setCurrentUserName(loginUser.getUserName());
			return Result.success(carDragRegistrationBusinessVo);

		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
			return Result.error("500",ex.getMessage());
		}
	}

	@ApiOperation(value="保存拖车登记")
	@PostMapping("saveDragRegistrationInfo")
	public Result<Object> saveDragRegistrationInfo(@RequestBody CarDragRegistrationInfo registrationInfo)
	{
		try {
			//
			CarBasic carBasic=carBasicService.selectById(registrationInfo.getBusinessId());
			if(carBasic==null) {
				logger.error("车辆信息不存在,businessId="+registrationInfo.getBusinessId());
				return Result.error("500","车辆信息不存在");
			}
			if(!CarStatusEnums.RETURNED.getStatusCode().equals(carBasic.getStatus())&&!CarStatusEnums.DEFAULT.getStatusCode().equals(carBasic.getStatus())) {
				logger.error("车辆未归还或非默认状态不允许拖车,businessId="+registrationInfo.getBusinessId());
				return Result.error("500","车辆未归还不允许拖车");
			}
			SimpleDateFormat dragDateFormat=new SimpleDateFormat("yyyy-MM-dd");
			Date dragDate=dragDateFormat.parse(registrationInfo.getDragDate());
			CarDrag carDrag=new CarDrag();
			carDrag.setId(UUID.randomUUID().toString() );
			carDrag.setBusinessId(registrationInfo.getBusinessId());
			carDrag.setDragDate( dragDate);
			carDrag.setDragHandler(registrationInfo.getDragHandler());
			carDrag.setProvince(registrationInfo.getProvince());
			carDrag.setCity(registrationInfo.getCity());
			carDrag.setCounty(registrationInfo.getCounty());
			carDrag.setDetailAddress(registrationInfo.getDetailAddress());
			carDrag.setFee(registrationInfo.getFee());
			carDrag.setOtherFee(registrationInfo.getOtherFee());
			carDrag.setRemark(registrationInfo.getRemark());
			carDrag.setCreateUser(loginUserInfoHelper.getLoginInfo().getUserId());
			carDrag.setCreateTime(new Date());
			carDragService.insert(carDrag);
			CarBasic originCarBasic= carBasicService.selectOne(new EntityWrapper<CarBasic>().eq("business_id",registrationInfo.getBusinessId()));
            originCarBasic.setStatus(CarStatusEnums.PENDING.getStatusCode());
            carBasicService.updateAllColumnById(originCarBasic);
			List<CarDragDoc> attachments=registrationInfo.getAttachments();
			if(attachments!=null&&attachments.size()>0) {

				for(CarDragDoc attachment:attachments) {
					//将临时表保存的上传信息保存到主表中
					DocTmp tmp=docTmpService.selectById(attachment.getDocId());
					if(tmp!=null) {
						Doc doc=new Doc();
						BeanUtils.copyProperties(tmp, doc);
						if(attachment.getNewName()!=null&&!attachment.getNewName().isEmpty()) {
							String newNameWithExtension=attachment.getNewName()+"."+tmp.getFileType();
							doc.setOriginalName(newNameWithExtension);
						}
						docService.insertOrUpdate(doc);
					}

				}
			}

			//在贷后状态里面 设置拖车登记状态
			collectionStatusService.setBussinessAfterStatus(
					registrationInfo.getBusinessId(),null,"",
					CollectionStatusEnum.TRAILER_REG,
					CollectionSetWayEnum.MANUAL_SET);
			//同时更新信贷的贷后状态
			List<StaffBusinessVo> voList = new LinkedList<>();
			StaffBusinessVo vo  = new StaffBusinessVo();
			vo.setBusinessId(registrationInfo.getBusinessId());
			voList.add(vo);
			collectionStatusService.SyncBusinessColStatusToXindai(voList,null,"界面设置移交诉讼",CollectionStatusEnum.TO_LAW_WORK.getPageStr());

			return Result.success();
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage());
			return Result.error("500",ex.getMessage());
		}
	}

    @ApiOperation(value = "获取车辆管理信息列表")
    @GetMapping("/carList")
    @ResponseBody
    public PageResult<List<CarVo>> selectCarPage(@ModelAttribute CarReq req){

        try{
        	 Wrapper<SysUserRole> wrapperSysUserRole = new EntityWrapper<SysUserRole>();
             wrapperSysUserRole.eq("user_id",loginUserInfoHelper.getUserId());
             wrapperSysUserRole.and(" role_code in (SELECT role_code FROM tb_sys_role WHERE role_area_type = 1 AND page_type = 2 ) ");
             List<SysUserRole> userRoles = sysUserRoleService.selectList(wrapperSysUserRole);
             if(null != userRoles && !userRoles.isEmpty()) {
             	req.setNeedPermission(0);//全局用户 不需要验证权限
             }
        	
        	Page<CarVo>  page=carService.selectCarPage(req);
            return PageResult.success(page.getRecords(),page.getTotal());
        }catch (Exception ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
            return PageResult.error(500, "数据库访问异常");
        }
    }
    @ApiOperation(value = "贷后首页台账 导出成excel")
//  @RequestMapping("/download")
  @PostMapping("/download")
  public void download(HttpServletRequest request, HttpServletResponse response,@ModelAttribute CarReq  req) throws Exception {
    try {
    	EasyPoiExcelExportUtil.setResponseHead(response,"car.xls");
      req.setPage(1);
      req.setLimit(5000);
      
      Wrapper<SysUserRole> wrapperSysUserRole = new EntityWrapper<SysUserRole>();
      wrapperSysUserRole.eq("user_id",req.getUserId());
      wrapperSysUserRole.and(" role_code in (SELECT role_code FROM tb_sys_role WHERE role_area_type = 1 AND page_type = 2 ) ");
      List<SysUserRole> userRoles = sysUserRoleService.selectList(wrapperSysUserRole);
      if(null != userRoles && !userRoles.isEmpty()) {
      	req.setNeedPermission(0);//全局用户 不需要验证权限
      }
      
      List<CarVo> list = carService.selectCarList(req);
      Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("车辆信息报表","车辆信息"), CarVo.class, list);

      workbook.write(response.getOutputStream());
	}catch (Exception e) {
      	logger.error(e.getMessage());
      	throw e;
  	}
  }
  
    
    @ApiOperation(value = "获取车辆管理信息详情")
    @PostMapping("/carDetail")
    public Result<Object> getCarDetail(@ModelAttribute("businessId") String businessId){
    	try {
    	CarBasic carBasic=carBasicService.selectById(businessId);
    	CarDetection carDetection=carDetectionService.selectById(carBasic.getLastDetectionId());
    	List<CarAuction>		 carAuctions=carAuctionService.selectList(new EntityWrapper<CarAuction>().eq("business_id", businessId));	
    	CarAuction carAuction=new CarAuction();
    	if(carAuctions!=null&&carAuctions.size()>0) {
    		carAuction=carAuctions.get(0);
    	}
    	CarAuctionReg carAuctionReg=new CarAuctionReg();
    	CarAuctionBidder carAuctionBidder=new CarAuctionBidder();
    	if(!StringUtils.isEmpty(carAuction.getAuctionId())) {
    		List<CarAuctionReg>	carAuctionRegs=carAuctionRegService.selectList(new EntityWrapper<CarAuctionReg>().eq("auction_id", carAuction.getAuctionId()).eq("is_auction_success", true));
    		if(carAuctionRegs!=null&&carAuctionRegs.size()>0) {
    			carAuctionReg=carAuctionRegs.get(0);
    			carAuctionBidder=carAuctionBidderService.selectById(carAuctionReg.getRegTel());
    		}
    	}
    	Map<String, Object> map=new HashMap<String,Object>();
    	map.put("carBasic", carBasic==null?new CarBasic():carBasic);
    	map.put("carDetection", carDetection==null?new CarDetection():carDetection);
    	map.put("carAuction",carAuction);
     	map.put("carAuctionReg",carAuctionReg);
      	map.put("carAuctionBidder",carAuctionBidder);
        	//carDragService.selectById(id)
    	//Page<CarVo>  page=carService.selectCarPage(req);
        return Result.build("0000", "操作成功", map);
	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
  }
      
    
    @ApiOperation(value = "重新评估")
    @PostMapping("/againAssess")
    public Result<Object> againAssess(@RequestBody Map<String,Object> params){
    	try {
    	CarBasic carBasic=JsonUtil.map2obj((Map<String,Object>)params.get("carBasic"), CarBasic.class);
    	CarDetection carDetection=JsonUtil.map2obj((Map<String,Object>)params.get("carDetection"), CarDetection.class);
    	AuctionAplyVo auctionAplyVo=new AuctionAplyVo();
    	//carBasic.setLastEvaluationAmount(carBasic.getEvaluationAmount());
    	//carBasic.setLastEvaluationTime(new Date());
    	//carBasic.setEvaluationAmount(carBasic.getEvaluationAmount());
    	//carDetection.setEvaluationAmount(carBasic.getEvaluationAmount());
    	auctionAplyVo.setCarBase(carBasic);
    	auctionAplyVo.setCarDetection(carDetection);
    	carService.againAssess(auctionAplyVo);
        return Result.build("0000", "操作成功", "");
	}catch (AlmsBaseExcepiton e) {
		logger.error(e.getMsg());
		return Result.error(e.getCode(),e.getMsg());
	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
  }
    
    @ApiOperation(value = "获取车辆归还信息")
    @PostMapping("/getReturnReg")
    public Result<Object> getReturnReg(@ModelAttribute("businessId") String businessId,@ModelAttribute("dragId") String dragId){
    	try {
    	BasicBusiness business=basicBusinessService.selectById(businessId);
    	CarBasic carBasic=carBasicService.selectById(businessId);
    	if(!CarStatusEnums.PENDING.getStatusCode().equals(carBasic.getStatus())) {
    		logger.error("该车处于非处置状态不允许归还操作,businessId="+businessId);
    		return Result.error("9999", "该车处于非处置状态不允许归还操作");
    	}
    	List<CarDrag> drag=carDragService.selectList(
    	        new EntityWrapper<CarDrag>().eq("id", dragId)); 
    	List<SysProvince> provs=new ArrayList<SysProvince>();
    
    	Map<String, Object> map=new HashMap<String,Object>();
    	map.put("carBasic", carBasic==null?new CarBasic():carBasic);
    	map.put("business", business==null?new BasicBusiness():business);
    	map.put("drag", (drag==null||drag.size()<=0)?new CarDrag():drag.get(0));
    	SysProvince p=new SysProvince();
    	p.setId(0);
    	p.setName("--请选择省--");
    	provs.add(p);
        List<SysProvince> proList= sysProvinceService.selectList(new EntityWrapper<SysProvince>().orderBy("id"));
        provs.addAll(proList);
    	map.put("provs", provs);
    	
    	List<SysCity> citys=new ArrayList<SysCity>();
    	SysCity c=new SysCity();
    	c.setId(0);
    	c.setName("--请选择市--");
    	citys.add(c);
    	map.put("citys", citys);
    	
    	List<SysCounty> countys=new ArrayList<SysCounty>();
    	SysCounty ct=new SysCounty();
    	ct.setId(0);
    	ct.setName("--请选择县区--");
    	countys.add(ct);
    	map.put("countys", countys);
    	
    	//查询车辆归还附件
    	List<DocType> docTypes=docTypeService.selectList(new EntityWrapper<DocType>().eq("type_code", "AfterLoan_Material_ReturnReg"));
    	if(docTypes!=null&&docTypes.size()==1) {
    	List<Doc> fileList=docService.selectList(new EntityWrapper<Doc>().eq("doc_type_id",docTypes.get(0).getDocTypeId()).eq("business_id", businessId).orderBy("doc_id"));
        map.put("returnRegFiles", fileList);
    	}
    	//carDragService.selectById(id)
    	//Page<CarVo>  page=carService.selectCarPage(req);
        return Result.build("0000", "操作成功", map);
      
	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
  }
    @ApiOperation(value = "通过省份id查询其下所有城市")
    @PostMapping("/getCitysByProId")
    public Result<Object> getCitysByProId(@ModelAttribute("proId") String proId){
    try {
    	Map<String, Object> map=new HashMap<String,Object>();
    	List<SysCity> citys=new ArrayList<SysCity>();
    	SysCity c=new SysCity();
    	c.setId(0);
    	c.setName("--请选择市--");
    	citys.add(c);
        List<SysCity> cityList=  sysCityService.selectList(new EntityWrapper<SysCity>().eq("province_id", proId).orderBy("id"));
        citys.addAll(cityList);
    	map.put("citys", citys);
        return Result.build("0000", "操作成功", map);
	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
  }
    
    @ApiOperation(value = "通过城市id查询其下所有县区")
    @PostMapping("/getCountysByCityId")
    public Result<Object> getCountysByCityId(@ModelAttribute("cityId") String cityId){
try {
    	Map<String, Object> map=new HashMap<String,Object>();
    	List<SysCounty> countys=new ArrayList<SysCounty>();
    	SysCounty c=new SysCounty();
    	c.setId(0);
    	c.setName("--请选择县区--");
    	countys.add(c);
        List<SysCounty> countyList=  sysCountyService.selectList(new EntityWrapper<SysCounty>().eq("city_id", cityId).orderBy("id"));
        countys.addAll(countyList);
    	map.put("countys", countys);
        	//carDragService.selectById(id)
    	//Page<CarVo>  page=carService.selectCarPage(req);
        return Result.build("0000", "操作成功", map);
	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
  }
      
    

    @SuppressWarnings("unchecked")
	@ApiOperation(value = "归还登记")
    @PostMapping("/addReturnReg")
    public Result<Object> addReturnReg( @RequestBody Map<String,Object> params){
    	try {
    	carService.addReturnReg(params);

    	return Result.build("0000", "操作成功", "");
	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
  }
    
    @ApiOperation(value="查询拍卖登记信息")
    @PostMapping("/auctionDetail")
    public Result<Object> AuctionDetail(@ModelAttribute("businessId") String businessId,@ModelAttribute("dragId") String dragId){
     	try {
    	BasicBusiness business=basicBusinessService.selectById(businessId);
    	CarBasic carBasic=carBasicService.selectById(businessId);
    	//拖车信息
    	List<CarDrag> drag=carDragService.selectList(
    	        new EntityWrapper<CarDrag>().eq("id", dragId)); 
    	//获取最新评估信息
    	CarDetection detection=carDetectionService.selectById(carBasic.getLastDetectionId());
    	//获取抵押时的评估信息
    	List<CarDetection> carDetections= carDetectionService.selectList(new EntityWrapper<CarDetection>().eq("business_id", businessId).eq("is_origin", true));
    	//还款信息
    	List<RepaymentBizPlan> plans=repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId));
    	if(plans==null||plans.size()!=1) {
    		logger.error("该业务编号下还款计划不存在或存在多条", plans==null?null:com.ht.ussp.util.JsonUtil.obj2Str(plans));
    		return Result.error("9999", "还款信息有误");
    	}
    	String planId=plans.get(0).getPlanId();
    	Map<String,Object> plan=new HashMap<String,Object>();
    	//还款计划列表
    	List<RepaymentBizPlanList> planLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("plan_id", planId).eq("current_status", "已还款").orderBy("fact_repay_date", false));
    	if(planLists!=null&&planLists.size()>0) {

    	
    	Set<String> set=new HashSet<String>();
    	Date lastPayDate=planLists.get(0).getFactRepayDate();//按还款日期降序取第一个还款日期
    	for(RepaymentBizPlanList planList:planLists) {
  
    		set.add(planList.getPlanListId());
    	}
    	
    	List<RepaymentBizPlanListDetail> planListDetails=repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().in("plan_list_id",set).eq("business_id", businessId));
       	if(planListDetails==null) {
    		logger.error("该业务编号下还款计划列表明细不存在");
    		return Result.error("9999", "还款信息有误");
    	}
       	BigDecimal payedPrincipal=new BigDecimal(0);
       	BigDecimal payedInterest=new BigDecimal(0);
       	for(RepaymentBizPlanListDetail detail:planListDetails) {
       		if(10==detail.getPlanItemType()) {
       			payedPrincipal=payedPrincipal.add((detail.getFactAmount()==null?new BigDecimal(0):detail.getFactAmount()));
       		}
       		if(20==detail.getPlanItemType()) {
       			payedInterest=payedInterest.add((detail.getFactAmount()==null?new BigDecimal(0):detail.getFactAmount()));
       		}
       	}
    	
       	
       	plan.put("payedPrincipal", payedPrincipal);
       	plan.put("payedInterest", payedInterest);
       	plan.put("lastPayDate", lastPayDate);
       	int overdueDays=DateUtil.getDiffDays(lastPayDate, new Date());//逾期天数
    
     	plan.put("overdueDays", overdueDays);
    	}
     	//出款信息
     	Set<Integer> outTypes=new HashSet<Integer>();
     	outTypes.add(0);
     	outTypes.add(1);
     	List<BizOutputRecord> outputRecords=bizOutputRecordService.selectList(new EntityWrapper<BizOutputRecord>().eq("business_id", businessId).in("withdraw_type", outTypes).orderBy("fact_output_date", true));
    	Map<String,Object> outputRecord=new HashMap<String,Object>();
    	if(outputRecords!=null&&outputRecords.size()>0) {
    		outputRecord.put("factOutputDate", outputRecords.get(0).getFactOutputDate());
    		outputRecord.put("outputUserName", outputRecords.get(0).getOutputUserName());
    	}
    	
    	List<CarAuction> carAuctions=carAuctionService.selectList(new EntityWrapper<CarAuction>().eq("business_id", businessId).orderBy("create_time",false));
    	CarAuction carAuction=new CarAuction();
    	if(carAuctions!=null&&carAuctions.size()==1) {
    		carAuction=carAuctions.get(0);
    	}
    	Map<String, Object> map=new HashMap<String,Object>();
    	//拍卖登记附件
    	List<DocType> docTypes=docTypeService.selectList(new EntityWrapper<DocType>().eq("type_code", "AfterLoan_Material_CarAuction"));
    	if(docTypes!=null&&docTypes.size()==1) {
    	List<Doc> fileList=docService.selectList(new EntityWrapper<Doc>().eq("doc_type_id",docTypes.get(0).getDocTypeId()).eq("business_id", businessId).orderBy("doc_id"));
    	map.put("returnRegFiles", fileList);
    	}
     	
    	map.put("carBasic", carBasic==null?new CarBasic():carBasic);
    	map.put("business", business==null?new BasicBusiness():business);
    	map.put("drag", (drag==null||drag.size()<=0)?new CarDrag():drag.get(0));
    	map.put("detection", detection);
    	map.put("mortgageDetection",(carDetections==null||carDetections.size()<=0)?new CarDetection():carDetections.get(0) );
    	map.put("repayPlan", plan);
    	map.put("outputRecord", outputRecord);
    	map.put("carAuction", carAuction);
    	  ProcessType processType = processTypeService.getProcessTypeByCode(ProcessTypeEnums.Aply_CarAuction.getKey());
          if(processType == null){
          	logger.error("流程类型未定义 type_code="+ProcessTypeEnums.Aply_CarAuction.getKey());
      		return Result.error("9999", "流程类型未定义");
             
          }
          List<com.hongte.alms.base.process.entity.Process> pross=processService.selectList(new EntityWrapper<com.hongte.alms.base.process.entity.Process>().eq("business_id", businessId).eq("process_typeid", processType.getTypeId()));
          if(pross!=null&&pross.size()>1) {
        	  logger.error("该业务存在多条拍卖流程,businessId"+businessId);
        	  return Result.error("9999", "该业务存在多条拍卖流程");
          }
          if(pross!=null&&pross.size()>0) {
          com.hongte.alms.base.process.entity.Process p=pross.get(0);
          map.put("processId", p.getProcessId());
          }
    	
    	return Result.build("0000", "操作成功", map);
	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
  }
    
    @SuppressWarnings("unchecked")
  	@ApiOperation(value = "拍卖申请")
      @PostMapping("/auctionAply")
      public Result<Object> auctionAply( @RequestBody Map<String,Object> params){
      	try {
    	CarBasic carBase=JsonUtil.map2obj((Map<String,Object>)params.get("carBasic"), CarBasic.class);
      	CarAuction carAuction=JsonUtil.map2obj((Map<String,Object>)params.get("carAuction"), CarAuction.class);
      	List<FileVo> files=JsonUtil.map2objList(params.get("returnRegFiles"), FileVo.class);
      //	CarBasic retCarBasic=carBasicService.selectById(carAuction.getBusinessId());
      	CarDetection carDetection=JsonUtil.map2obj((Map<String,Object>)params.get("detection"), CarDetection.class);
      	String submitType=(String) params.get("subType");
      	String processId=(String) params.get("processId");
      	AuctionAplyVo vo=new AuctionAplyVo();
      	vo.setCarBase(carBase);
      	vo.setCarAuction(carAuction);
      	vo.setFiles(files);
      	vo.setSubmitType(submitType);
      	vo.setProcessId(processId);
      	vo.setCarDetection(carDetection);
      	String pId=carService.auctionAply(vo);
      	Map<String, Object> map=new HashMap<String,Object>();
      	map.put("processId",pId);
      	return Result.build("0000", "操作成功", map);
 	}catch (AlmsBaseExcepiton e) {
      	logger.error(e.getMessage());
      	
      	return Result.error(e.getCode(), e.getMsg());
  	}catch (Exception e) {
  		logger.error(e.getMessage());
  		return Result.error("9999", "系统异常");
	}
  }
      
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "拍卖撤销")
    @PostMapping("/auctionCancel")
    public Result<Object> auctionAplyCancel( @RequestBody Map<String,Object> params){
    try {
    	String businessId=(String) params.get("businessId");
    	String auctionId=(String) params.get("auctionId");
    	String processId=(String) params.get("processId");
    	List<FileVo> files=JsonUtil.map2objList(params.get("returnRegFiles"), FileVo.class);
    	
    	CarBasic retCarBasic=carBasicService.selectById(businessId);

    	if(retCarBasic==null) {
    		logger.error("该业务记录不存在");
    		return Result.error("9999", "无效业务");
    	}

    	CarAuction carAuctions=carAuctionService.selectById(auctionId);
    	if(carAuctions==null) {
    		logger.error("该拍卖申请记录不存在");
    		return Result.error("9999", "该拍卖申请记录不存在");
    		}
        ProcessType processType = processTypeService.getProcessTypeByCode(ProcessTypeEnums.Aply_CarAuction.getKey());
        if(processType == null){
        	logger.error("流程类型未定义 type_code="+ProcessTypeEnums.Aply_CarAuction.getKey());
    		return Result.error("9999", "流程类型未定义");
           
        }
        
        List<com.hongte.alms.base.process.entity.Process> processList=processService.selectList(new EntityWrapper<com.hongte.alms.base.process.entity.Process>().
    			eq("process_typeid", processType.getTypeId()).ne("status", ProcessStatusEnums.CNACL.getKey()).eq("business_id", businessId));
    	if(processList!=null&&processList.size()==1) {
    		com.hongte.alms.base.process.entity.Process process=processList.get(0);
        //撤销流程
	    	if(process!=null) {
	    		List<ProcessLog> processLogs=processLogService.selectList(new EntityWrapper<ProcessLog>().eq("process_id", processId));
		    	if(processLogs!=null&&processLogs.size()>0) {
		    		logger.error("审核进行中不允许撤销,process_id="+processId);
		    		return Result.error("9999", "审核进行中不允许撤销");
		    	}
		    	process.setStatus(ProcessStatusEnums.CNACL.getKey());
		    	process.setUpdateTime(new Date());
		    	process.setUpdateUser(loginUserInfoHelper.getUserId());
		    	processService.updateById(process);
	    	}
    	}else if(processList!=null&&processList.size()>1) {
    		logger.error("流程类型未定义 process_typeid="+processType.getTypeId()+",business_id="+businessId);
    		return Result.error("9999", "该业务存在多条非注销的流程");
    	}
    	carAuctions.setStatus(AuctionStatusEnums.CANCEL.getKey());
    	carAuctionService.updateById(carAuctions);	
    	if(files!=null&&files.size()>0) {
    		for(FileVo file:files) {
    			docService.deleteById(file.getOldDocId());		
    		}
    	}
    	return Result.build("0000", "操作成功", "");
 	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
  }
    
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "转公车申请")
    @PostMapping("/convBusAply")
    public Result<Object> convBusAply( @RequestBody Map<String,Object> params){
    	try {
    	CarBasic carBase=JsonUtil.map2obj((Map<String,Object>)params.get("carBasic"), CarBasic.class);
    	CarConvBusAply carConvBusAply=JsonUtil.map2obj((Map<String,Object>)params.get("carConvBusAply"), CarConvBusAply.class);
    	CarBasic retCarBasic=carBasicService.selectById(carConvBusAply.getBusinessId());
    	
    	String submitType=(String) params.get("subType");
    	if(retCarBasic==null) {
    		logger.error("该业务记录不存在");
    		return Result.error("9999", "无效业务");
    	}
    	carBase.setBusinessId(retCarBasic.getBusinessId());
    	carBasicService.updateById(carBase);
    	List<CarConvBusAply> ccarConvBusAplys=carConvBusAplyService.selectList(new EntityWrapper<CarConvBusAply>().eq("business_id", carConvBusAply.getBusinessId()).ne("status", AuctionStatusEnums.DRAFT.getKey()));
    	if(ccarConvBusAplys!=null&&ccarConvBusAplys.size()>0) {
    		for(CarConvBusAply carConv:ccarConvBusAplys) {
    			if(AuctionStatusEnums.AUDIT.getKey().equals(carConv.getStatus())) {
    				logger.error("该申请已提交审核，business_id="+carConv.getBusinessId());
    	    		return Result.error("9999", "该申请已提交审核,请勿重复提交");
    			}else {
    				carConv.setConvBusId(carConv.getConvBusId());
    				
    				
    			}
    		}
    	}else {
    		if(StringUtils.isEmpty(carConvBusAply.getConvBusId())) {
    			carConvBusAply.setConvBusId(UUID.randomUUID().toString());
    			carConvBusAply.setCreateTime(new Date());
    			carConvBusAply.setCreateUser(loginUserInfoHelper.getUserId());
    		}
    	}
    	Map<String, Object> map=new HashMap<String,Object>();
    	if("audit".equals(submitType)) {
    		carConvBusAply.setStatus(AuctionStatusEnums.AUDIT.getKey());
    		//创建审核流程
    		ProcessSaveReq processSaveReq=new ProcessSaveReq();
    		processSaveReq.setBusinessId(carConvBusAply.getBusinessId());
    		processSaveReq.setProcessStatus(ProcessStatusEnums.NEW.getKey());
    		processSaveReq.setTitle(ProcessTypeEnums.Aply_CarAuction.getName());
    		com.hongte.alms.base.process.entity.Process process=processService.saveProcess(processSaveReq, ProcessTypeEnums.Aply_ConvBus);
    		map.put("processId", process.getProcessId());
    	}else {
    		carConvBusAply.setStatus(AuctionStatusEnums.DRAFT.getKey());
    	}
    	carConvBusAplyService.insertOrUpdate(carConvBusAply);
    	
    	map.put("carConvBusAply", carConvBusAply);

    	return Result.build("0000", "操作成功", map);
 	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
  }
    
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "转公车撤销")
    @PostMapping("/convBusCancel")
    public Result<Object> convBusCancel( @RequestBody Map<String,Object> params){
    try {
    	String businessId=(String) params.get("businessId");
    	String convBusId=(String) params.get("convBusId");
    	String processId=(String) params.get("processId");
   
    	
    	CarBasic retCarBasic=carBasicService.selectById(businessId);

    	if(retCarBasic==null) {
    		logger.error("该业务记录不存在");
    		return Result.error("9999", "无效业务");
    	}

    	CarConvBusAply carConvBusAply=carConvBusAplyService.selectById(convBusId);
    	if(carConvBusAply==null) {
    		logger.error("该公车申请记录不存在");
    		return Result.error("9999", "该公车申请记录不存在");
    		}
        ProcessType processType = processTypeService.getProcessTypeByCode(ProcessTypeEnums.Aply_ConvBus.getKey());
        if(processType == null){
        	logger.error("流程类型未定义 type_code="+ProcessTypeEnums.Aply_ConvBus.getKey());
    		return Result.error("9999", "流程类型未定义");
           
        }
        
        List<com.hongte.alms.base.process.entity.Process> processList=processService.selectList(new EntityWrapper<com.hongte.alms.base.process.entity.Process>().
    			eq("process_typeid", processType.getTypeId()).ne("status", ProcessStatusEnums.CNACL.getKey()).eq("business_id", businessId));
    	if(processList!=null&&processList.size()==1) {
    		com.hongte.alms.base.process.entity.Process process=processList.get(0);
        //撤销流程
	    	if(process!=null) {
	    		List<ProcessLog> processLogs=processLogService.selectList(new EntityWrapper<ProcessLog>().eq("process_id", processId));
		    	if(processLogs!=null&&processLogs.size()>0) {
		    		logger.error("审核进行中不允许撤销,process_id="+processId);
		    		return Result.error("9999", "审核进行中不允许撤销");
		    	}
		    	process.setStatus(ProcessStatusEnums.CNACL.getKey());
		    	process.setUpdateTime(new Date());
		    	process.setUpdateUser(loginUserInfoHelper.getUserId());
		    	processService.updateById(process);
	    	}
    	}else if(processList!=null&&processList.size()>1) {
    		logger.error("流程类型未定义 process_typeid="+processType.getTypeId()+",business_id="+businessId);
    		return Result.error("9999", "该业务存在多条非注销的流程");
    	}
    	carConvBusAply.setStatus(AuctionStatusEnums.CANCEL.getKey());
    	carConvBusAplyService.updateById(carConvBusAply);	
    	return Result.build("0000", "操作成功", "");
 	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
  }
    
    @ApiOperation(value="查询转公车申请信息")
    @PostMapping("/convBusAplyDetail")
    public Result<Object> convBusAplyDetail(@ModelAttribute("businessId") String businessId){
     try {
    	BasicBusiness business=basicBusinessService.selectById(businessId);
    	CarBasic carBasic=carBasicService.selectById(businessId);
    	//拖车信息
    	List<CarDrag> drag=carDragService.selectList(
    	        new EntityWrapper<CarDrag>().eq("business_id", businessId)); 
    	//还款信息
    	List<RepaymentBizPlan> plans=repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId));
    	if(plans==null||plans.size()!=1) {
    		logger.error("该业务编号下还款计划不存在或存在多条", plans==null?null:com.ht.ussp.util.JsonUtil.obj2Str(plans));
    		return Result.error("9999", "还款信息有误");
    	}
    	String planId=plans.get(0).getPlanId();
    	//还款计划列表
    	List<RepaymentBizPlanList> planLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("plan_id", planId).orderBy("fact_repay_date", false));
    	if(planLists==null||planLists.size()<=0) {
    		logger.error("该业务编号下还款计划列表不存在");
    		return Result.error("9999", "还款信息有误");
    	}
    	Set<String> set=new HashSet<String>();
    	Date lastPayDate=planLists.get(0).getFactRepayDate();//按还款日期降序取第一个还款日期
    	for(RepaymentBizPlanList planList:planLists) {
  
    		set.add(planList.getPlanListId());
    	}
    	List<RepaymentBizPlanListDetail> planListDetails=repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().in("plan_list_id",set).eq("business_id", businessId));
       	if(planListDetails==null) {
    		logger.error("该业务编号下还款计划列表明细不存在");
    		return Result.error("9999", "还款信息有误");
    	}
       	BigDecimal payedPrincipal=new BigDecimal(0);
       	BigDecimal payedInterest=new BigDecimal(0);
       	for(RepaymentBizPlanListDetail detail:planListDetails) {
       		if(10==detail.getPlanItemType()) {
       			payedPrincipal=payedPrincipal.add((detail.getFactAmount()==null?new BigDecimal(0):detail.getFactAmount()));
       		}
       		if(20==detail.getPlanItemType()) {
       			payedInterest=payedInterest.add((detail.getFactAmount()==null?new BigDecimal(0):detail.getFactAmount()));
       		}
       	}
       	Map<String,Object> plan=new HashMap<String,Object>();
       	plan.put("payedPrincipal", payedPrincipal);
       	plan.put("payedInterest", payedInterest);
       	plan.put("lastPayDate", lastPayDate);
       	int overdueDays=DateUtil.getDiffDays(lastPayDate, new Date());//逾期天数
     	plan.put("overdueDays", overdueDays);
     	//出款信息
     	Set<Integer> outTypes=new HashSet<Integer>();
     	outTypes.add(0);
     	outTypes.add(1);
     	List<BizOutputRecord> outputRecords=bizOutputRecordService.selectList(new EntityWrapper<BizOutputRecord>().eq("business_id", businessId).in("withdraw_type", outTypes).orderBy("fact_output_date", true));
    	Map<String,Object> outputRecord=new HashMap<String,Object>();
    	if(outputRecords!=null&&outputRecords.size()>0) {
    		outputRecord.put("factOutputDate", outputRecords.get(0).getFactOutputDate());
    		outputRecord.put("outputUserName", outputRecords.get(0).getOutputUserName());
    	}
    	
    	List<CarConvBusAply> carConvBusAplys=carConvBusAplyService.selectList(new EntityWrapper<CarConvBusAply>().eq("business_id", businessId).ne("status", AuctionStatusEnums.CANCEL.getKey()));
    	CarConvBusAply carConvBusAply=new CarConvBusAply();
    	if(carConvBusAplys!=null&&carConvBusAplys.size()==1) {
    		carConvBusAply=carConvBusAplys.get(0);
    	}
    	Map<String, Object> map=new HashMap<String,Object>();

     	
    	map.put("carBasic", carBasic==null?new CarBasic():carBasic);
    	map.put("business", business==null?new BasicBusiness():business);
    	map.put("drag", (drag==null||drag.size()<=0)?new CarDrag():drag.get(0));
    	map.put("repayPlan", plan);
    	map.put("outputRecord", outputRecord);
    	map.put("carConvBusAply", carConvBusAply);
    
    	
    	return Result.build("0000", "操作成功", map);
 	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
  }
    
    @ApiOperation(value = "获取竞价信息")
    @GetMapping("/auctionRegList")
    @ResponseBody
    public PageResult<List<BusinessBidsVo>> selectBusinessBids(@ModelAttribute BusinessBidsReq req) {
		try {
			Page<BusinessBidsVo> pages = carService.selectBusinessBids(req);
			return PageResult.success(pages.getRecords(), pages.getTotal());
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			return PageResult.error(9999, "系统异常");
		}
	}

    @ApiOperation(value = "获取车辆拍卖信息")
    @PostMapping("/getAuctionReg")
    public Result<Object> getAuctionReg(@ModelAttribute("regId") String regId) {
		try {
			CarAuctionReg reg = carAuctionRegService.selectById(regId);
			if (reg == null) {
				logger.error("拍卖登记信息不存在，regId=" + regId);
				return Result.error("9999", "拍卖登记信息不存在");
			}
			return Result.build("0", "操作成功", reg);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return Result.error("9999", "操作异常");
		}
	}
      
    
    @ApiOperation(value = "更新拍卖登记信息")
    @PostMapping("/updateAuctionReg")
    public synchronized Result<Object> updateAuctionReg(@ModelAttribute CarAuctionReg req) {
		try {
			if (req.getAuctionSuccess() == null) {
				logger.error("auctionSuccess不能为null");
				return Result.error("9999", "auctionSuccess不能为null");
			}
			if (req.getAuctionSuccess() == false && req.getTransPrice() != null) {
				logger.error("是否竞拍成功 为否时，成交价格 不能有值");
				return Result.error("9999", "是否竞拍成功 为否时，成交价格 不能有值");
			}
			if (req.getAuctionSuccess() == true && req.getTransPrice() == null) {
				logger.error("是否竞拍成功 为是时，成交价格 必须有值");
				return Result.error("9999", "是否竞拍成功 为是时，成交价格 必须有值");
			}
			CarAuctionReg reg = carAuctionRegService.selectById(req.getRegId());
			if (reg == null) {
				logger.error("拍卖登记信息不存在，regId=" + req.getRegId());
				return Result.error("9999", "拍卖登记信息不存在");
			}
			CarAuction carAuction = carAuctionService.selectById(reg.getAuctionId());
			if (carAuction == null) {
				logger.error("拍卖信息不存在，auctionId=" + reg.getAuctionId());
				return Result.error("9999", "拍卖信息不存在");
			}
			CarAuctionReg existSuccessReg = carAuctionRegService.selectOne(new EntityWrapper<CarAuctionReg>().eq("auction_id", reg.getAuctionId()).eq("is_auction_success", true));
			if (existSuccessReg != null&&(!existSuccessReg.getUserId().equals(reg.getUserId())) ) {
				logger.error("已经存在竞拍成功的纪录，auctionId=" + reg.getAuctionId());
				return Result.error("9999", "已经存在竞拍成功的纪录");
			}
			if (req.getAuctionSuccess()) {

				carAuction.setStatus(AuctionStatusEnums.AUCTION.getKey());
				carAuctionService.updateById(carAuction);
			}
			reg.setTransPrice(req.getTransPrice());
			reg.setAuctionSuccess(req.getAuctionSuccess());
			reg.setUpdateTime(new Date());
			reg.setUpdateUser(loginUserInfoHelper.getUserId());
			carAuctionRegService.updateAllColumnById(reg);
			return Result.build("0", "操作成功", "");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return Result.error("9999", "操作异常");
		}
	}
      
    
    @ApiOperation(value="拍卖审核信息")
    @PostMapping("/auctionAuditDetail")
    public Result<Object> auctionAuditDetail(@ModelAttribute("businessId") String businessId,@ModelAttribute("processId") String processId){
     	try{BasicBusiness business=basicBusinessService.selectById(businessId);
    	CarBasic carBasic=carBasicService.selectById(businessId);
    	Process process = processService.selectOne(new EntityWrapper<Process>().eq("business_id", businessId).eq("process_name", "车辆拍卖申请")) ;
    	//拖车信息
    	List<CarDrag> drag=carDragService.selectList(
    	        new EntityWrapper<CarDrag>().eq("business_id", businessId).orderBy("create_time", false)); 
    	//获取最新评估信息
    	CarDetection detection=carDetectionService.selectById(carBasic.getLastDetectionId());
    	//获取抵押时的评估信息
    	List<CarDetection> carDetections= carDetectionService.selectList(new EntityWrapper<CarDetection>().eq("business_id", businessId).eq("is_origin", true));
    	
    	//还款信息
    	List<RepaymentBizPlan> plans=repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId));
    	if(plans==null||plans.size()!=1) {
    		logger.error("该业务编号下还款计划不存在或存在多条", plans==null?null:com.ht.ussp.util.JsonUtil.obj2Str(plans));
    		return Result.error("9999", "还款信息有误");
    	}
    	String planId=plans.get(0).getPlanId();
     	Map<String,Object> plan=new HashMap<String,Object>();
    	//还款计划列表
    	List<RepaymentBizPlanList> planLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("plan_id", planId).eq("current_status", "已还款").orderBy("fact_repay_date", false));
    	if(planLists!=null&&planLists.size()>0) {

 
    	Set<String> set=new HashSet<String>();
    	Date lastPayDate=planLists.get(0).getFactRepayDate();//按还款日期降序取第一个还款日期
    	for(RepaymentBizPlanList planList:planLists) {
  
    		set.add(planList.getPlanListId());
    	}
    	List<RepaymentBizPlanListDetail> planListDetails=repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().in("plan_list_id",set).eq("business_id", businessId));
       	if(planListDetails==null) {
    		logger.error("该业务编号下还款计划列表明细不存在");
    		return Result.error("9999", "还款信息有误");
    	}
       	BigDecimal payedPrincipal=new BigDecimal(0);
       	BigDecimal payedInterest=new BigDecimal(0);
       	for(RepaymentBizPlanListDetail detail:planListDetails) {
       		if(10==detail.getPlanItemType()) {
       			payedPrincipal=payedPrincipal.add((detail.getFactAmount()==null?new BigDecimal(0):detail.getFactAmount()));
       		}
       		if(20==detail.getPlanItemType()) {
       			payedInterest=payedInterest.add((detail.getFactAmount()==null?new BigDecimal(0):detail.getFactAmount()));
       		}
       	}
  
       	plan.put("payedPrincipal", payedPrincipal);
       	plan.put("payedInterest", payedInterest);
       	plan.put("lastPayDate", lastPayDate);
       	int overdueDays=DateUtil.getDiffDays(lastPayDate, new Date());//逾期天数
     	plan.put("overdueDays", overdueDays);
       	}//出款信息
     	Set<Integer> outTypes=new HashSet<Integer>();
     	outTypes.add(0);
     	outTypes.add(1);
     	List<BizOutputRecord> outputRecords=bizOutputRecordService.selectList(new EntityWrapper<BizOutputRecord>().eq("business_id", businessId).in("withdraw_type", outTypes).orderBy("fact_output_date", true));
    	Map<String,Object> outputRecord=new HashMap<String,Object>();
    	if(outputRecords!=null&&outputRecords.size()>0) {
    		outputRecord.put("factOutputDate", outputRecords.get(0).getFactOutputDate());
    		outputRecord.put("outputUserName", outputRecords.get(0).getOutputUserName());
    	}
    	
    	List<CarAuction> carAuctions=carAuctionService.selectList(new EntityWrapper<CarAuction>().eq("business_id", businessId).orderBy("create_time",false));
    	CarAuction carAuction=new CarAuction();
    	if(carAuctions!=null&&carAuctions.size()==1) {
    		carAuction=carAuctions.get(0);
    	}
    	Map<String, Object> map=new HashMap<String,Object>();
    	//拍卖登记附件
    	List<DocType> docTypes=docTypeService.selectList(new EntityWrapper<DocType>().eq("type_code", "AfterLoan_Material_CarAuction"));
    	if(docTypes!=null&&docTypes.size()==1) {
    	List<Doc> fileList=docService.selectList(new EntityWrapper<Doc>().eq("doc_type_id",docTypes.get(0).getDocTypeId()).eq("business_id", businessId).orderBy("doc_id"));
    	map.put("returnRegFiles", fileList);
    	}
    	processService.getProcessShowInfo(map, processId, ProcessTypeEnums.Aply_CarAuction);
    	map.put("carBasic", carBasic==null?new CarBasic():carBasic);
    	map.put("business", business==null?new BasicBusiness():business);
    	map.put("drag", (drag==null||drag.size()<=0)?new CarDrag():drag.get(0));
    	map.put("detection", detection);
    	map.put("mortgageDetection",(carDetections==null||carDetections.size()<=0)?new CarDetection():carDetections.get(0) );
    	
    	map.put("repayPlan", plan);
    	map.put("outputRecord", outputRecord);
    	map.put("carAuction", carAuction);
    	map.put("canEdit", process.getStartUserId().equals(process.getApproveUserId()));
    
    	return Result.build("0000", "操作成功", map);
     	}catch (Exception e) {
          	logger.error(e.getMessage());
          	return Result.error("9999", "操作异常");
      	}
      }
    @SuppressWarnings("unchecked")
  	@ApiOperation(value = "拍卖审核")
      @PostMapping("/auctionAudit")
      public Result<Object> auctionAudit( @RequestBody Map<String,Object> params){
      try {
      	CarBasic carBase=JsonUtil.map2obj((Map<String,Object>)params.get("carBasic"), CarBasic.class);
      	CarAuction carAuction=JsonUtil.map2obj((Map<String,Object>)params.get("carAuction"), CarAuction.class);
      	List<FileVo> files=JsonUtil.map2objList(params.get("returnRegFiles"), FileVo.class);
      	AuditVo auditVo=JsonUtil.map2obj((Map<String,Object>)params.get("auditVo"), AuditVo.class);
      	CarDetection carDetection=JsonUtil.map2obj((Map<String,Object>)params.get("detection"), CarDetection.class);
      	AuctionAplyVo vo=new AuctionAplyVo();
      	vo.setCarBase(carBase);
      	vo.setCarAuction(carAuction);
      	vo.setFiles(files);
      	vo.setAuditVo(auditVo);
      	vo.setCarDetection(carDetection);
      	carService.auctionAudit(vo);
      	return Result.build("0000", "操作成功", "");
      }catch (AlmsBaseExcepiton e) {
    	  logger.error(e.getMsg());
    	  return Result.error(e.getCode(), e.getMsg());
	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
      }
    @ApiOperation(value = "获取车辆拍卖/竞价时间")
    @PostMapping("/getAuction")
    public Result<Object> getAuction(@ModelAttribute("businessId") String businessId){
    	try{
    	List<CarAuction>	carAuctions=carAuctionService.selectList(new EntityWrapper<CarAuction>().eq("business_id", businessId).eq("status", AuctionStatusEnums.AUDITED.getKey()));
    	if(carAuctions==null||carAuctions.size()!=1) {
    		logger.error("该车不存在或存在多条拍卖审核完成信息,businessId="+businessId+",status=04");
    		return Result.error("9999", "该车不存在或存在多条拍卖审核完成信息");
    	}
    	Map<String, Object> map=new HashMap<String,Object>();
    	map.put("carAuction", carAuctions.get(0));
    	//map.put("bidder", bidder);
        return Result.build("0000", "操作成功", map);
 	}catch (Exception e) {
      	logger.error(e.getMessage());
      	return Result.error("9999", "操作异常");
  	}
  }
    @ApiOperation(value="延长竞买时间")
    @PostMapping("/updateBuyEndTime")
    public Result<Object> updateBuyEndTime( @RequestBody Map<String,Object> params){
    	try{
    		CarAuction carAuction=JsonUtil.map2obj((Map<String,Object>)params.get("carAuction"), CarAuction.class);
    		if(carAuction==null||StringUtils.isEmpty(carAuction.getAuctionId())) {
    			logger.error("参数为空,"+carAuction==null?"carAuction="+carAuction:"auctionId="+carAuction.getAuctionId());
        		return Result.error("9999", "参数为空");
    		}
    		CarAuction rCarAuction=carAuctionService.selectById(carAuction.getAuctionId());
    		if(rCarAuction==null) {
    			logger.error("拍卖信息不存在,auctionId="+carAuction.getAuctionId());
    			return Result.error("9999", "拍卖信息不存在");
    		}
    		
    		long buyEndTime=rCarAuction.getBuyEndTime().getTime();
    		long currTime=new Date().getTime();
    		if(currTime>buyEndTime) {
    			logger.error("竞买已结束，不允许延长,auctionId="+carAuction.getAuctionId());
    			return Result.error("9999", "竞买已结束，不允许延长");
    		}
    		if(currTime+delayedMin*60*1000<buyEndTime) {
    			logger.error("不在竞买结束前"+delayedMin+"分钟内，不允许延长,auctionId="+carAuction.getAuctionId());
    			return Result.error("9999", "不在竞买结束前"+delayedMin+"分钟内，不允许延长");
    		}
    		rCarAuction.setBuyEndTime(carAuction.getBuyEndTime());
    		rCarAuction.setAuctionEndTime(carAuction.getAuctionEndTime());
    		//rCarAuction.set 增加更新人、时间
    		rCarAuction.setDelayPeriod(carAuction.getDelayPeriod());
    		carAuctionService.updateById(rCarAuction);
    		return Result.build("0000", "操作成功", "");
     	}catch (Exception e) {
          	logger.error(e.getMessage());
          	return Result.error("9999", "操作异常");
      	}
    }
}

