package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
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
@TableName("tb_btn")
public class Btn extends Model<Btn> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单编号
     */
    @TableId("res_code")
	@ApiModelProperty(required= true,value = "菜单编号")
	private String resCode;
    /**
     * 菜单名称
     */
	@TableField("res_name_cn")
	@ApiModelProperty(required= true,value = "菜单名称")
	private String resNameCn;
    /**
     * 顺序
     */
	@ApiModelProperty(required= true,value = "顺序")
	private Integer sequence;
    /**
     * 类型
     */
	@TableField("res_type")
	@ApiModelProperty(required= true,value = "类型")
	private String resType;
    /**
     * 父菜单编号
     */
	@TableField("res_parent")
	@ApiModelProperty(required= true,value = "父菜单编号")
	private String resParent;
    /**
     * 菜单说明
     */
	@TableField("res_content")
	@ApiModelProperty(required= true,value = "菜单说明")
	private String resContent;
    /**
     * 图标
     */
	@TableField("font_icon")
	@ApiModelProperty(required= true,value = "图标")
	private String fontIcon;
    /**
     * 状态
     */
	@ApiModelProperty(required= true,value = "状态")
	private String status;
    /**
     * 角色数量
     */
	@TableField("rule_num")
	@ApiModelProperty(required= true,value = "角色数量")
	private String ruleNum;
    /**
     * 角色名称
     */
	@TableField("rule_num_name")
	@ApiModelProperty(required= true,value = "角色名称")
	private String ruleNumName;
    /**
     * 角色内容
     */
	@TableField("rule_content")
	@ApiModelProperty(required= true,value = "角色内容")
	private String ruleContent;


	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResNameCn() {
		return resNameCn;
	}

	public void setResNameCn(String resNameCn) {
		this.resNameCn = resNameCn;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getResParent() {
		return resParent;
	}

	public void setResParent(String resParent) {
		this.resParent = resParent;
	}

	public String getResContent() {
		return resContent;
	}

	public void setResContent(String resContent) {
		this.resContent = resContent;
	}

	public String getFontIcon() {
		return fontIcon;
	}

	public void setFontIcon(String fontIcon) {
		this.fontIcon = fontIcon;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRuleNum() {
		return ruleNum;
	}

	public void setRuleNum(String ruleNum) {
		this.ruleNum = ruleNum;
	}

	public String getRuleNumName() {
		return ruleNumName;
	}

	public void setRuleNumName(String ruleNumName) {
		this.ruleNumName = ruleNumName;
	}

	public String getRuleContent() {
		return ruleContent;
	}

	public void setRuleContent(String ruleContent) {
		this.ruleContent = ruleContent;
	}

	@Override
	protected Serializable pkVal() {
		return this.resCode;
	}

	@Override
	public String toString() {
		return "Btn{" +
			", resCode=" + resCode +
			", resNameCn=" + resNameCn +
			", sequence=" + sequence +
			", resType=" + resType +
			", resParent=" + resParent +
			", resContent=" + resContent +
			", fontIcon=" + fontIcon +
			", status=" + status +
			", ruleNum=" + ruleNum +
			", ruleNumName=" + ruleNumName +
			", ruleContent=" + ruleContent +
			"}";
	}
}
