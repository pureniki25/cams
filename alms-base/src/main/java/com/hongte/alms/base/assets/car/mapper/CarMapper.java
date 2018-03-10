package com.hongte.alms.base.assets.car.mapper;

import java.util.List;
import com.hongte.alms.base.assets.car.vo.CarReq;
import com.hongte.alms.base.assets.car.vo.CarVo;

/**
 * 车辆管理
 * @since 2018-01-25
 */
public interface CarMapper  {

	List<CarVo> selectCarPage(CarReq carReq);
	int selectCarPageCount(CarReq carReq);
}
