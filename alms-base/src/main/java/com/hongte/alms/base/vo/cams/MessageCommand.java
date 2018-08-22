package com.hongte.alms.base.vo.cams;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class MessageCommand {

    public String getLogId() {
        return clientId+"-"+ messageId;
    }


    @NotBlank
    protected String clientId;

    @NotBlank
    protected String messageId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    protected Date createTime;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        if(clientId!=null) clientId=clientId.toUpperCase();

        this.clientId = clientId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
