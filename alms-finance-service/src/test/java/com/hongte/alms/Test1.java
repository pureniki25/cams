package com.hongte.alms;

import java.util.Date;

import org.junit.Test;

import com.hongte.alms.common.util.DateUtil;

public class Test1 {

	private Date fiterSegmentationDate = DateUtil.getDate("2016-03-11",DateUtil.DEFAULT_FORMAT_DATE);
	
	@Test
	public void test1() {
		
		System.out.println(fiterSegmentationDate.compareTo(new Date()));
	}
	
}
