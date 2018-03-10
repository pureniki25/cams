package com.hongte.alms.base.assets.car.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.assets.car.mapper.CarMapper;
import com.hongte.alms.base.assets.car.service.CarService;
import com.hongte.alms.base.assets.car.vo.CarReq;
import com.hongte.alms.base.assets.car.vo.CarVo;

@Service("CarService")
public class CarServiceImpl  implements CarService {
	
	@Autowired
	private CarMapper carMapper;

	public Page<CarVo> selectCarPage(CarReq carReq) {
			Page<CarVo> pages = new Page<CarVo>();
		   int count=carMapper.selectCarPageCount(carReq);
		   if(count<=0) {
			   return pages;
		   }
		   List<CarVo> list=carMapper.selectCarPage(carReq);
		   pages.setTotal(count);
		   pages.setRecords(list);
		return pages;
	}

	public List<CarVo> selectCarList(CarReq carReq){
		  List<CarVo> list=carMapper.selectCarPage(carReq);
		  return list;
	}
	
}
