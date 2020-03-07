package com.hongte.alms.base.mapper;

import java.util.List;

import com.hongte.alms.base.entity.BankIncomeDat;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 银收 Mapper 接口
 * </p>
 *
 * @author czs
 * @since 2019-03-11
 */
public interface BankIncomeDatMapper extends SuperMapper<BankIncomeDat> {
	List<BankIncomeDat> selectExportList(BankIncomeDat  dat);
}
