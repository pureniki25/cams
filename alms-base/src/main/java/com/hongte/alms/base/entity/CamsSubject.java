package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 科目表
 * </p>
 *
 * @author czs
 * @since 2019-03-03
 */
@ApiModel
@TableName("tb_cams_subject")
public class CamsSubject extends Model<CamsSubject> {

    private static final long serialVersionUID = 1L;

    /**
     * 科目id
     */
	@ApiModelProperty(required= true,value = "科目id")
	private String id;
    /**
     * 科目名称
     */
	@TableField("subject_name")
	@ApiModelProperty(required= true,value = "科目名称")
	private String subjectName;
	@ApiModelProperty(required= true,value = "")
	private String value;
    /**
     * 父级ID
     */
	@TableField("parent_id")
	@ApiModelProperty(required= true,value = "父级ID")
	private String parentId;
	
    /**
     * 模板
     */
	@TableField("temp")
	@ApiModelProperty(required= true,value = "模板")
	private String temp;
	
	
    /**
     * 出入类别
     */
	@TableField("type")
	@ApiModelProperty(required= true,value = "出入类别")
	private String type;
    /**
     * 是否展开
     */
	@ApiModelProperty(required= true,value = "是否展开")
	private Integer spread;

	
	@TableField("direction")
    private String direction;
	
	@TableField(exist = false)
    private String jine;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
    
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
    
	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getJine() {
		return jine;
	}

	public void setJine(String jine) {
		this.jine = jine;
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
		return "CamsSubject{" +
			", id=" + id +
			", subjectName=" + subjectName +
			", value=" + value +
			", parentId=" + parentId +
			", spread=" + spread +
			",direction="+direction+
			"}";
	}
}
