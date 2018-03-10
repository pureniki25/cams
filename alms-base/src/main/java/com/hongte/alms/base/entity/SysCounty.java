package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * [县级地名表]
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-26
 */
@ApiModel
@TableName("tb_sys_county")
public class SysCounty extends Model<SysCounty> {

    private static final long serialVersionUID = 1L;

    /**
     * [编号]
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "[编号]")
	private Integer id;
    /**
     * [县名]
     */
	@ApiModelProperty(required= true,value = "[县名]")
	private String name;
    /**
     * [城市编号]
     */
	@TableField("city_id")
	@ApiModelProperty(required= true,value = "[城市编号]")
	private Integer cityId;


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

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysCounty{" +
			", id=" + id +
			", name=" + name +
			", cityId=" + cityId +
			"}";
	}
}
