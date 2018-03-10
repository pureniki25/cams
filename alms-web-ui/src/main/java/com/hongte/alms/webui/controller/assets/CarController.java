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
	    @RequestMapping("auctionReg")
	    public String auctionReg(ModelMap map,@RequestParam("businessId") String businessId) {
	    	 map.addAttribute("businessId",businessId);
	        return "/assets/car/auctionReg";
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
}
