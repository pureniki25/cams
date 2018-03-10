package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * [省份信息表]
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-26
 */
@ApiModel
@TableName("tb_sys_province")
public class SysProvince extends Model<SysProvince> {

    private static final long serialVersionUID = 1L;

    /**
     * 省份编号
     */
	@ApiModelProperty(required= true,value = "省份编号")
	private Integer id;
    /**
     * 省份名称
     */
	@ApiModelProperty(required= true,value = "省份名称")
	private String name;
    /**
     * 排序
     */
	@ApiModelProperty(required= true,value = "排序")
	private Integer sort;
    /**
     * 备注
     */
	@ApiModelProperty(required= true,value = "备注")
	private String remark;


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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysProvince{" +
			", id=" + id +
			", name=" + name +
			", sort=" + sort +
			", remark=" + remark +
			"}";
	}
}
