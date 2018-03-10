package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * [城市]
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-26
 */
@ApiModel
@TableName("tb_sys_city")
public class SysCity extends Model<SysCity> {

    private static final long serialVersionUID = 1L;

    /**
     * [城市ID]
     */
	@ApiModelProperty(required= true,value = "[城市ID]")
	private Integer id;
    /**
     * [城市名称]
     */
	@ApiModelProperty(required= true,value = "[城市名称]")
	private String name;
    /**
     * [省份ID]
     */
	@TableField("province_id")
	@ApiModelProperty(required= true,value = "[省份ID]")
	private Integer provinceId;
    /**
     * [排序]
     */
	@ApiModelProperty(required= true,value = "[排序]")
	private Integer sort;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysCity{" +
			", id=" + id +
			", name=" + name +
			", provinceId=" + provinceId +
			", sort=" + sort +
			"}";
	}
}
