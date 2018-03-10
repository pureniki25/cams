package com.hongte.alms.base.collection.dto;

import com.hongte.alms.base.collection.entity.CollectionStatus;

/**
 * @author zengkun
 * @since 2018/3/7
 */
public class CollectionStatusCountDto extends CollectionStatus {
    private  Integer counts;

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }
}
