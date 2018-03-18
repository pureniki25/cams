package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;

public class LitigationResponseData implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean importSuccess;
	private String message;

	public boolean isImportSuccess() {
		return importSuccess;
	}

	public void setImportSuccess(boolean importSuccess) {
		this.importSuccess = importSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "LitigationResponseData [importSuccess=" + importSuccess + ", message=" + message + "]";
	}

}
