package com.hongte.alms.base.feignClient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hongte.alms.base.vo.litigation.LitigationResponse;
import com.hongte.alms.base.vo.litigation.TransferOfLitigationVO;
import com.hongte.alms.common.result.Result;


@FeignClient(value = "litigation-service")
public interface LitigationFeignClient {

	/**
	 * 根据businessId判断是否已经移交过诉讼系统
	 * @param businessId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/api/isImportLitigation", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.GET)
	Result isImportLitigation(@RequestParam("businessId") String businessId);
	
	@RequestMapping(value = "/api/importLitigation", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	LitigationResponse importLitigation(@RequestBody TransferOfLitigationVO vo);
}
