package com.hongte.alms.base.mapper;

import com.hongte.alms.base.entity.DocType;
import com.hongte.alms.base.vo.module.doc.DocItem;
import com.hongte.alms.base.vo.module.doc.DocTypeItem;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文档类型 Mapper 接口
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-28
 */
public interface DocTypeMapper extends SuperMapper<DocType> {
    /**
     * 根据顶层类别节点及业务编码，返回相关分类节点数据（暂时只做2层）
     * @author 伦惠峰
     * @Date 2018/1/25 17:25
     */
    List<DocTypeItem> getDocTypeItemList(@Param(value="typeCode")String typeCode);

    /**
     * 返回某个业务单号，已上传的文档列表
     * @author 伦惠峰
     * @Date 2018/1/25 17:25
     */
    List<DocItem> getDocItemList(@Param(value="typeCode")String typeCode, @Param(value="businessID") String businessID);
    /**
     * 通过文件类型编码查询
     * @param typeCode
     * @return
     */
    List<DocTypeItem> getDocTypeByTypeCode(@Param(value="typeCode")String typeCode);

}
