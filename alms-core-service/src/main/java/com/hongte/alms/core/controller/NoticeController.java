package com.hongte.alms.core.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.Notice;
import com.hongte.alms.base.entity.NoticeFile;
import com.hongte.alms.base.entity.SysOrg;
import com.hongte.alms.base.service.NoticeFileService;
import com.hongte.alms.base.service.NoticeService;
import com.hongte.alms.base.service.SysOrgService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.common.result.Result;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * [通知公告详情表] 前端控制器
 * </p>
 *
 * @author 王继光
 * @since 2018-03-07
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
	private Logger logger = LoggerFactory.getLogger(NoticeController.class);
	
	@Qualifier("NoticeService")
	@Autowired
	NoticeService noticeService ;
	
	@Qualifier("SysOrgService")
	@Autowired
	SysOrgService sysOrgService ;
	
	@Qualifier("SysUserService")
	@Autowired
	SysUserService sysUserService ;
	
	@Qualifier("NoticeFileService")
	@Autowired
	NoticeFileService noticeFileService;
	
	@Autowired
	LoginUserInfoHelper loginUserInfoHelper ;
	/**
	 * 获取通知公告,主要用于 首页-通知公告 
	 * @author shine
	 * @Since 2018年3月7日下午3:24:41
	 * @return
	 */
	@GetMapping("/list")
	@ApiOperation(value = "获取通知公告")
	@ResponseBody
	public Result<List<Notice>> listNotice(){
		try {
			String userId = loginUserInfoHelper.getUserId();
			String orgCode = sysUserService.selectById(userId).getOrgCode();
			List<String> orgCodes = sysOrgService.getParentsOrgs(orgCode);
			if (orgCodes==null) {
				orgCodes = new ArrayList<>();
			}
			orgCodes.add(orgCode);
			EntityWrapper<Notice> ew = new EntityWrapper<Notice>();
			ew.isNull("is_deleted").eq("is_send", 1).in("org_code", orgCodes);
			ew.orderBy("publish_time", false);
			List<Notice> list = noticeService.selectList(ew);
			return Result.success(list);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("500", e.getMessage());
		}
	};
	
	@GetMapping("/get")
	@ApiOperation(value = "获取1条通知公告")
	@ResponseBody
	public Result<Map<String, Object>> getNotice(String noticeId) {
		Notice notice = noticeService.selectOne(new EntityWrapper<Notice>().eq("notice_id", noticeId));
		List<NoticeFile> noticeFiles = noticeFileService
				.selectList(new EntityWrapper<NoticeFile>().eq("notice_id", noticeId));
		Map<String,Object> map = new HashMap<>();
		if (notice==null) {
			return Result.error("500", "notice was not found");
		}
		map.put("notice", notice);
		map.put("fileList", noticeFiles);
		return Result.success(map);

	}
}

