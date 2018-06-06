package com.hongte.alms.finance.req;

import java.io.Serializable;

/**
 * @author zengkun
 * @since 2018/6/6
 */
public class WithHoldLogReq implements Serializable {
    private String identifyCard;
    private Integer page;
    private Integer size;


    public String getIdentifyCard() {
        return identifyCard;
    }

    public void setIdentifyCard(String identifyCard) {
        this.identifyCard = identifyCard;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
