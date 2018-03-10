package com.hongte.alms.base.assets.car.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.vo.CarReq;
import com.hongte.alms.base.assets.car.vo.CarVo;

public interface CarService {

	public Page<CarVo> selectCarPage(CarReq carReq);
	public List<CarVo> selectCarList(CarReq carReq);
}
