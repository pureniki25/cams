package com.hongte.alms.webui.controller.assets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequestMapping("/assets/car")
public class CarController {
	    //资产管理-车辆管理
	    @RequestMapping("carList")
	    public String carList(ModelMap map){
	        return "/assets/car/carList";
	    }
	    @RequestMapping("carDetail")
	    public String carDetail(ModelMap map,@RequestParam("businessId") String businessId) {
	    	 map.addAttribute("businessId",businessId);
	        return "/assets/car/carDetail";
	    }
	    @RequestMapping("carAuction")
	    public String carAuction(ModelMap map,@RequestParam("businessId") String businessId) {
	    	 map.addAttribute("businessId",businessId);
	        return "/assets/car/carAuction";
	    }
	    @RequestMapping("convBusAply")
	    public String convBusAply(ModelMap map,@RequestParam("businessId") String businessId) {
	    	 map.addAttribute("businessId",businessId);
	        return "/assets/car/convBusAply";
	    }

	    @RequestMapping("returnReg")
	    public String returnReg(ModelMap map,@RequestParam("businessId") String businessId) {
	    	 map.addAttribute("businessId",businessId);
	        return "/assets/car/returnReg";
	    }
	    @RequestMapping("againAssess")
	    public String againAssess(ModelMap map,@RequestParam("businessId") String businessId) {
	    	 map.addAttribute("businessId",businessId);
	        return "/assets/car/againAssess";
	    }
	    @RequestMapping("viewAttachment")
	    public String viewAttachment(ModelMap map,@RequestParam("businessId") String businessId) {
	    	 map.addAttribute("businessId",businessId);
	        return "/assets/car/viewAttachment";
	    }
	    @RequestMapping("uploadAttachment")
	    public String uploadAttachment(ModelMap map,@RequestParam("businessId") String businessId) {
	    	 map.addAttribute("businessId",businessId);
	        return "/assets/car/uploadAttachment";
	    }
	    @RequestMapping("auctionRegList")
	    public String auctionRegList(ModelMap map,@RequestParam("businessId") String businessId) {
	    	 map.addAttribute("businessId",businessId);
	        return "/assets/car/auctionRegList";
	    }
	    @RequestMapping("auctionReg")
	    public String auctionReg(ModelMap map,@RequestParam("regId") String regId) {
	    	 map.addAttribute("regId",regId);
	        return "/assets/car/auctionReg";
	    }
	    @RequestMapping("carAuctionAudit")
	    public String carAuctionAudit(ModelMap map,@RequestParam("businessId") String businessId,@RequestParam("processStatus") String processStatus,@RequestParam("processId") String processId) {
	    	 map.addAttribute("businessId",businessId);
	    	 map.addAttribute("processStatus",processStatus);
	    	 map.addAttribute("processId",processId);
	        return "/assets/car/carAuctionAudit";
	    }
	    @RequestMapping("delayed")
	    public String delayed(ModelMap map,@RequestParam("businessId") String businessId) {
	    	 map.addAttribute("businessId",businessId);
	        return "/assets/car/auctionDelayed";
	    }
}
