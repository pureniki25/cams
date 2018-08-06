package com.hongte.alms.base.vo.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class MyPermissionIdsInfo implements Serializable{

	private static final long serialVersionUID = -8868722644067435604L;

	/**
	 * 其他页面权限
	 */
	private List<String> page0List = new ArrayList<>();
	/**
	 * 贷后管理列表
	 */
	private List<String> page1List = new ArrayList<>();
	
	/**
	 * 车辆管理列表
	 */
	private List<String> page2List = new ArrayList<>();
	
	/**
	 * 减免管理列表
	 */
	private List<String> page3List = new ArrayList<>();
	
	/**
	 * 代扣管理列表
	 */
	private List<String> page4List = new ArrayList<>();
	
	/**
	 * 代扣查询列表
	 */
	private List<String> page5List = new ArrayList<>();
	
	/**
	 * 审批查询列表
	 */
	private List<String> page6List = new ArrayList<>();
	
	/**
	 * 展期管理列表
	 */
	private List<String> page7List = new ArrayList<>();
	
	/**
	 * 消息管理列表
	 */
	private List<String> page8List = new ArrayList<>();
	
	/**
	 * 财务管理列表
	 */
	private List<String> page9List = new ArrayList<>();

	public void updatePagePermissionList(List<String> tempBizs, JSONObject pagePermission) {
		for (String key : pagePermission.keySet()) {
			switch (key) {
			case "page0":
				if(pagePermission.getBooleanValue("page0")) {
					page0List.addAll(tempBizs);
				}
				break;
			case "page1":
				if(pagePermission.getBooleanValue("page1")) {
					page1List.addAll(tempBizs);
				}
				break;
			case "page2":
				if(pagePermission.getBooleanValue("page2")) {
					page2List.addAll(tempBizs);
				}
				break;
			case "page3":
				if(pagePermission.getBooleanValue("page3")) {
					page3List.addAll(tempBizs);
				}
				break;
			case "page4":
				if(pagePermission.getBooleanValue("page4")) {
					page4List.addAll(tempBizs);
				}
				break;
			case "page5":
				if(pagePermission.getBooleanValue("page5")) {
					page5List.addAll(tempBizs);
				}
				break;
			case "page6":
				if(pagePermission.getBooleanValue("page6")) {
					page6List.addAll(tempBizs);
				}
				break;
			case "page7":
				if(pagePermission.getBooleanValue("page7")) {
					page7List.addAll(tempBizs);
				}
				break;
			case "page8":
				if(pagePermission.getBooleanValue("page8")) {
					page8List.addAll(tempBizs);
				}
				break;
			case "page9":
				if(pagePermission.getBooleanValue("page9")) {
					page9List.addAll(tempBizs);
				}
				break;
			default:
				break;
			}
		}
	}
	
	public List<String> getPage1List() {
		return page1List;
	}


	public void setPage1List(List<String> page1List) {
		this.page1List = page1List;
	}

	public List<String> getPage2List() {
		return page2List;
	}

	public void setPage2List(List<String> page2List) {
		this.page2List = page2List;
	}

	public List<String> getPage3List() {
		return page3List;
	}

	public void setPage3List(List<String> page3List) {
		this.page3List = page3List;
	}

	public List<String> getPage4List() {
		return page4List;
	}

	public void setPage4List(List<String> page4List) {
		this.page4List = page4List;
	}

	public List<String> getPage5List() {
		return page5List;
	}

	public void setPage5List(List<String> page5List) {
		this.page5List = page5List;
	}

	public List<String> getPage6List() {
		return page6List;
	}

	public void setPage6List(List<String> page6List) {
		this.page6List = page6List;
	}

	public List<String> getPage7List() {
		return page7List;
	}

	public void setPage7List(List<String> page7List) {
		this.page7List = page7List;
	}

	public List<String> getPage8List() {
		return page8List;
	}

	public void setPage8List(List<String> page8List) {
		this.page8List = page8List;
	}

	public List<String> getPage9List() {
		return page9List;
	}

	public void setPage9List(List<String> page9List) {
		this.page9List = page9List;
	}

	public List<String> getPage0List() {
		return page0List;
	}

	public void setPage0List(List<String> page0List) {
		this.page0List = page0List;
	}
	
}
