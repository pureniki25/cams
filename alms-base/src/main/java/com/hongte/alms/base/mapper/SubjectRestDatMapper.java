package com.hongte.alms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.controller.SubjectRestDatController;
import com.hongte.alms.base.entity.SubjectRestDat;
import com.hongte.alms.base.vo.cams.RestProductVo;
import com.hongte.alms.base.vo.cams.SubjectRestVo;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 科目余额表 Mapper 接口
 * </p>
 *
 * @author czs
 * @since 2020-01-19
 */
public interface SubjectRestDatMapper extends SuperMapper<SubjectRestDat> {
	
	/**
	 * 同步所有凭证表的数据到一张科目余额表里
	 */
	void syncPingZhengData();
	
	/**
	 * 删除科目余额表数据
	 */
	void deleteSubjectRest();
	
	
	/**
	 * 科目余额表查询
	 * 
	 * @param pages
	 * @param vo
	 * @return
	 */
	List<SubjectRestVo> selectSubjectRestList(Pagination pages, SubjectRestVo vo);

}
