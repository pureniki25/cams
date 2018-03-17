package com.hongte.alms.core.controller;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.entity.Notice;
import com.hongte.alms.base.entity.NoticeFile;
import com.hongte.alms.base.entity.SysOrg;
import com.hongte.alms.base.service.DocService;
import com.hongte.alms.base.service.NoticeFileService;
import com.hongte.alms.base.service.NoticeService;
import com.hongte.alms.base.service.SysOrgService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.vo.module.NoticeDTO;
import com.hongte.alms.base.vo.module.doc.UpLoadResult;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.vo.PageResult;
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
	
	@Qualifier("DocService")
	@Autowired
	DocService docService ;
	
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
	
	@GetMapping("/page")
	@ApiOperation(value = "分页获取通知公告")
	@ResponseBody
	public PageResult<List<Notice>> page(Integer page,Integer limit,String title,Date startDate,Date endDate){
		String userId = loginUserInfoHelper.getUserId();
		logger.info("userId:"+userId);
		String orgCode = sysUserService.selectById(userId).getOrgCode();
		List<String> orgCodes = sysOrgService.getParentsOrgs(orgCode);
		if (orgCodes==null) {
			orgCodes = new ArrayList<>();
		}
		orgCodes.add(orgCode);
		EntityWrapper<Notice> ew = new EntityWrapper<Notice>();
		ew.eq("create_user_id", userId) ;
		ew.isNull("is_deleted");
		if (title!=null) {
			ew.like("notice_title", title);
		}
		
		if (startDate!=null) {
			ew.gt("publish_time", startDate);
		}
		if (endDate!=null) {
			ew.lt("publish_time", endDate);
		}
		Page<Notice> page2 = noticeService.selectPage(new Page<Notice>(page, limit), ew);
		return PageResult.success(page2.getRecords(), page2.getTotal());
		
	}
	
	@PostMapping("/save")
	@ApiOperation(value = "新增/更新通知公告")
	@ResponseBody
	public Result save(@RequestBody NoticeDTO notice) {
		logger.info(JSONObject.toJSONString(notice));
		String userId = loginUserInfoHelper.getUserId();
		if (notice==null) {
			return Result.error("500", "notice不能为空");
		}
		
		Notice notice2 = new Notice() ;
		
		notice2.setHasRead(Integer.valueOf(notice.getHasRead()));
		notice2.setHasReadTime(Integer.valueOf(notice.getHasReadTime()));
		notice2.setHasReadTimeUnit(Integer.valueOf(notice.getHasReadTimeUnit()));
		notice2.setNoticeContent(notice.getNoticeContent());
		notice2.setNoticeId(notice.getNoticeId()==null||notice.getNoticeId().equals("")?null:Integer.valueOf(notice.getNoticeId()));
		notice2.setNoticeTitle(notice.getNoticeTitle());
		notice2.setOrgCode(notice.getOrgCode());
		notice2.setPublishChannel(notice.getPublishChannel());
		notice2.setPublishTime(DateUtil.getDate(notice.getPublishTime()));
		if (notice2.getNoticeId()==null) {
			notice2.setCreateTime(new Date());
			notice2.setCreateUserId(userId);
			notice2.setIsSend(0);
			notice2.insert();
		}else {
			notice2.setUpdateTime(new Date());
			notice2.updateById();
		}
		
		List<NoticeFile> list = notice.getFiles();
		if (list!=null&&list.size()>0) {
			for (NoticeFile noticeFile : list) {
				noticeFile.setNoticeId(notice2.getNoticeId());
				noticeFile.setUserId(userId);
				if (noticeFile.getNoticeFileId()==null) {
					noticeFile.setCreateTime(new Date());
					noticeFile.insert();
				}else {
					noticeFile.updateById();
				}
			}
			
		}
		return Result.success(notice);
		
	}
	
	/**
     * 文件上传具体实现方法（单文件上传）
     */
	@ApiOperation(value = "上传附件")
	@PostMapping("/uploadAttachment")
    public UpLoadResult upload(FileVo fileVo,String uploadItemId) throws FileNotFoundException {
		String userId = loginUserInfoHelper.getUserId() ;
		UpLoadResult upLoadResult = new UpLoadResult() ;
		if (userId==null) {
			upLoadResult.setUploaded(false);
			upLoadResult.setMessage("userId can't be null");
			return upLoadResult;
		}
		upLoadResult = noticeFileService.upload(fileVo,userId);
		upLoadResult.setMessage(uploadItemId);
        return upLoadResult;
    }
	@ApiOperation(value = "(逻辑)删除公告")
	@GetMapping("/del")
	@ResponseBody
	public Result del(String noticeId) {
		Notice notice = noticeService.selectById(noticeId);
		if (notice==null) {
			return Result.error("500", "notice 不存在");
		}
		notice.setDeleteTime(new Date());
		notice.setDeleteUserId(loginUserInfoHelper.getUserId());
		notice.setIsDeleted(1);
		notice.updateById();
		return Result.success(true);
	}
}

