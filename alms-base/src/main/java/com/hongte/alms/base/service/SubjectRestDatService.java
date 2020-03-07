package com.hongte.alms.base.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SubjectRestDat;
import com.hongte.alms.base.vo.cams.BalanceVo;
import com.hongte.alms.base.vo.cams.SubjectRestVo;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 科目余额表 服务类
 * </p>
 *
 * @author czs
 * @since 2020-01-19
 */
public interface SubjectRestDatService extends BaseService<SubjectRestDat> {
	
	/**
	 * 同步所有凭证表的数据到一张科目余额表里
	 */
	void syncPingZhengData();
	
	/**
	 * 删除科目余额表数据
	 */
	void deleteSubjectRest();
	
	void downLoadFile(HttpServletResponse response,String tempFile,Map<String,Object> beans,String companyName) throws  Exception;
	/**
	 * 查询科目余额表
	 * @param vo
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	
	public Page<SubjectRestVo> selectSubjectRest(SubjectRestVo vo) throws InstantiationException, IllegalAccessException;
	
	
	public Page<BalanceVo> selectBalance(SubjectRestVo vo) throws Exception;
	
	public Page<BalanceVo> selectProfit(SubjectRestVo vo) throws Exception;
	
	public Double getBalaceAmount(String subject,String type,Map<String, List<SubjectRestVo>> map);
}
