package com.hongte.alms.base.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.SysOrg;

public class CompanySortByPINYINUtil {
	/**
     * 按照List中的某个String类型的属性进行排序
     * @param list
     */
    @SuppressWarnings("unchecked")
    public static void sortByPINYIN(List list){
        Collections.sort(list, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
              return ((java.text.RuleBasedCollator)java.text.Collator.getInstance(java.util.Locale.CHINA)).compare(((BasicCompany)o1).getAreaName(), ((BasicCompany)o2).getAreaName());
            }

		    
        });

    }
    @SuppressWarnings("unchecked")
    public static void sortByPINYIN_SysOrg(List list){
        Collections.sort(list, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
              return ((java.text.RuleBasedCollator)java.text.Collator.getInstance(java.util.Locale.CHINA)).compare(((SysOrg)o1).getOrgNameCn(), ((SysOrg)o2).getOrgNameCn());
            }

		    
        });

    }

}
