package com.hongte.alms.webui.controller.Litigation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/transferOfLitigation")
public class TransferOfLitigationController {

	@RequestMapping("/carLoan")
	public String indexCarLoan(@RequestParam String businessId,
			@RequestParam(value = "processId", required = false) String processId, ModelMap modelMap,
			@RequestParam("processStatus") String processStatus,
			@RequestParam(value = "crpId", required = false) String crpId) {
		modelMap.put("businessId", businessId);
		modelMap.put("processId", processId);
		modelMap.put("processStatus", processStatus);
		modelMap.put("crpId", crpId);
		return "/Litigation/carLoan";
	}

	@RequestMapping("/houseLoan")
	public String indexHouseLoan(@RequestParam String businessId,
			@RequestParam(value = "processId", required = false) String processId, ModelMap modelMap,
			@RequestParam("processStatus") String processStatus,
			@RequestParam(value = "crpId", required = false) String crpId) {
		modelMap.put("businessId", businessId);
		modelMap.put("processId", processId);
		modelMap.put("processStatus", processStatus);
		modelMap.put("crpId", crpId);
		return "/Litigation/houseLoan";
	}
}
