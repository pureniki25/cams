package com.hongte.alms.base.vo.cams;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CamsMessage implements Serializable {
    /**
     * 生成的serialVersionUID
     */
    private static final long serialVersionUID = 5231134212346077681L;

    @NotBlank
    private String messageId;

    @NotBlank
    private String clientId;

    @NotNull
    private Object message;

    private String Version;

    @NotBlank
    private String queueName;

    private String  exchangeName;

	private String hostUrl;
		
	private Integer hostPort;

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public Integer getHostPort() {
        return hostPort;
    }

    public void setHostPort(Integer hostPort) {
        this.hostPort = hostPort;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    @Override
    public String toString() {
        return "CamsMessage{" +
                "messageId='" + messageId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", message='" + message + '\'' +
                ", Version='" + Version + '\'' +
                ", queueName='" + queueName + '\'' +
                ", exchangeName='" + exchangeName + '\'' +
                '}';
    }
}
