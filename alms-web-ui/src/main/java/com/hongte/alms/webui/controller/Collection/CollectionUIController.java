package com.hongte.alms.webui.controller.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: 曾坤
 * @Date: 2018/1/24
 */
@Controller
@RequestMapping("/collectionUI")
public class CollectionUIController {
/*    @RequestMapping("setPhoneStaffUI")
    public String index(){
        return "/Collection/setPhoneStaff";
    }*/

    //贷后管理首页
    @RequestMapping("index")
    public String index(ModelMap map){
//        map.addAttribute("staffType","phoneStaff");
        return "/Collection/index";
    }


    //设置电催
    @RequestMapping("setPhoneStaffUI")
    public String setPhoneStaff(
            @RequestParam("crpIds") String crpIds,
            ModelMap map){
        map.addAttribute("staffType","phoneStaff");
        map.addAttribute("crpIds",crpIds);

        return "/Collection/setPhoneStaff";
    }

    //设置上门催收
   @RequestMapping("setVisitStaffUI")
    public String setVisitStaff(
           @RequestParam("crpIds") String crpIds,
            ModelMap map){
       map.addAttribute("staffType","visitStaff");
       map.addAttribute("crpIds",crpIds);
        return "/Collection/setPhoneStaff";
    }

    //催交跟进记录
   @RequestMapping("staffTrackRecordUI")
    public String staffTrackRecord(
           @RequestParam("businessId") String businessId,
            @RequestParam("crpId") String crpId,
            ModelMap map){
        map.addAttribute("businessId",businessId);
        map.addAttribute("crpId",crpId);
        return "/Collection/staffFollowRecord";
    }

    /////////   减免 开始 /////////////////

    //减免申请界面
   @RequestMapping("applyDerateUI")
    public String applyDerate(
           @RequestParam("businessId") String businessId,
            @RequestParam("crpId") String crpId,
            @RequestParam("processStatus") String processStatus,
            @RequestParam(value="processId",required=false) String processId,
           ModelMap map
            ){
        map.addAttribute("businessId",businessId);
        map.addAttribute("crpId",crpId);
        map.addAttribute("processStatus",processStatus);
        map.addAttribute("processId",processId);
        return "/Collection/ApplyDerate";
    }

    //减免管理界面
    @RequestMapping("applyDerateListUI")
    public String applyDerateList(
//            @RequestParam("businessId") String businessId,
//            @RequestParam("crpId") String crpId,
//            @RequestParam("processStatus") String processStatus,
//            @RequestParam(value="processId",required=false) String processId,
//            ModelMap map
    ){
//        map.addAttribute("businessId",businessId);
//        map.addAttribute("crpId",crpId);
//        map.addAttribute("processStatus",processStatus);
//        map.addAttribute("processId",processId);
        return "/Collection/ApplyDerateList";
    }
    
    //执行代扣界面
    @RequestMapping("deductionUI")
    public String deduction(
    	    @RequestParam("planListId") String planListId,
    	
    	    ModelMap map
          
    	    
    ){
    	  map.addAttribute("planListId",planListId);
    
    	  
        return "/Collection/deduction";
    }
    
    //执行代扣界面
    @RequestMapping("repaymentLogUI")
    public String repaymentLog(){
    
    	  
        return "/Collection/repaymentLog";
    }
    


    /////////   减免 结束 /////////////////

//    Model model
    @RequestMapping("checkFundPool")
    public String checkFundPool() {
    	return "/Collection/checkFundPool" ;
    }
    
    @RequestMapping("repaymentRegister")
    public String repaymentRegister() {
    	return "/Collection/repaymentRegister" ;
    }
    
    
    //短信详情界面
   @RequestMapping("repaymentLogDetailUI")
    public String repaymentLogDetail(
           @RequestParam("platformName") String platformName,
            @RequestParam("thirdOrderId") String thirdOrderId,
            @RequestParam("merchOrderId") String merchOrderId,
            @RequestParam(value="currentAmount") String currentAmount,
            @RequestParam(value="repayStatus") String repayStatus,
            @RequestParam(value="createTime") String createTime,
            @RequestParam(value="remark") String remark,
           ModelMap map
            )
   {
        map.addAttribute("platformName",platformName);
        map.addAttribute("thirdOrderId",thirdOrderId);
        map.addAttribute("merchOrderId",merchOrderId);
        map.addAttribute("currentAmount",currentAmount);
        map.addAttribute("repayStatus",repayStatus);
        map.addAttribute("createTime",createTime);
        map.addAttribute("remark",remark);
        return "/Collection/repayLogDetail";
    }
   
   //执行代扣界面
   @RequestMapping("bankLimitUI")
   public String bankLimit(){
       return "/Collection/bankLimit";
   }
   
   
 //执行管理界面
   @RequestMapping("repaymentManage")
   public String repaymentManage(){
       return "/Collection/repaymentManage";
   }
   
   
   
 //贷后详情界面
   @RequestMapping("afterDetail")
   public String afterDetail(){
       return "/Collection/afterDetail";
   }

    //代扣渠道列表
    @RequestMapping("withholdChannel")
    public String withholdChannel(){
        return "/Collection/withholdChannelList";
    }

    //代扣渠道列表
    @RequestMapping("withholdLimit")
    public String withholdLimit(){
        return "/Collection/withholdChannelList";
    }
}
