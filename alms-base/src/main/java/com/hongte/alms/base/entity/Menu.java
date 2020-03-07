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
 * 菜单表
 * </p>
 *
 * @author wjg
 * @since 2018-12-31
 */
@ApiModel
@TableName("tb_menu")
public class Menu extends Model<Menu> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单编号
     */
	@ApiModelProperty(required= true,value = "菜单编号")
	private String id;
    /**
     * 菜单图标
     */
	@ApiModelProperty(required= true,value = "菜单图标")
	private String icon;
    /**
     * 顺序
     */
	@ApiModelProperty(required= true,value = "顺序")
	private Integer index;
    /**
     * 菜单名称
     */
	@ApiModelProperty(required= true,value = "菜单名称")
	private String title;
    /**
     * 菜单连接
     */
	@ApiModelProperty(required= true,value = "菜单连接")
	private String url;
    /**
     * 父级ID
     */
	@TableField("parent_id")
	@ApiModelProperty(required= true,value = "父级ID")
	private String parentId;
    /**
     * 是否展开
     */
	@ApiModelProperty(required= true,value = "是否展开")
	private Integer spread;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getSpread() {
		return spread;
	}

	public void setSpread(Integer spread) {
		this.spread = spread;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Menu{" +
			", id=" + id +
			", icon=" + icon +
			", index=" + index +
			", title=" + title +
			", url=" + url +
			", parentId=" + parentId +
			", spread=" + spread +
			"}";
	}
}
