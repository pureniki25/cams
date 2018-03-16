package com.hongte.alms.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class test1 {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) throws Exception {
		String first = "2018-3-3";
		String second = "2018-3-3";
		Date firstdate = format.parse(first);
		Date seconddate = format.parse(second);
		int cnt = longOfTwoDate(firstdate, seconddate);
		System.out.println(cnt);
	}

	public static int longOfTwoDate(Date first, Date second) throws ParseException {
		if (!second.after(first)) {
			return 0;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(first);
		int cnt = 0;
		while (calendar.getTime().compareTo(second) != 0) {
			calendar.add(Calendar.DATE, 1);
			cnt++;
		}
		return cnt;
	}
}
