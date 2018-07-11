package com.hongte.alms.base.vo.finance;


import com.hongte.alms.base.entity.DepartmentBank;
import lombok.Data;

import java.util.List;

/**
 * Created by 张贵宏 on 2018/7/10 14:34
 */
@Data
public class DepartmentBankVO extends DepartmentBank {
    /**
     * 多个分公司id
     */
    private List<String> deptIds;
}
