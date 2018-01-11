package com.wherewerks.retry.pojo;

import java.sql.Timestamp;

public class FailedRetryLogs {

	private String uniqueId;
	private String originalMsg;
	private String processstage;
	private String retryPolicyId;
	private int retryIndex;
	private String lasterrMsg;
	private String manualRetryFlag;
	private String retyrStatus;
	private Timestamp recordTime;
	private Timestamp updatedTime;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getOriginalMsg() {
		return originalMsg;
	}

	public void setOriginalMsg(String originalMsg) {
		this.originalMsg = originalMsg;
	}

	public String getProcessstage() {
		return processstage;
	}

	public void setProcessstage(String processstage) {
		this.processstage = processstage;
	}

	public int getRetryIndex() {
		return retryIndex;
	}

	public void setRetryIndex(int retryIndex) {
		this.retryIndex = retryIndex;
	}

	public String getLasterrMsg() {
		return lasterrMsg;
	}

	public void setLasterrMsg(String lasterrMsg) {
		this.lasterrMsg = lasterrMsg;
	}

	public String isManualRetryFlag() {
		return manualRetryFlag;
	}

	public void setManualRetryFlag(String manualRetryFlag) {
		this.manualRetryFlag = manualRetryFlag;
	}

	public String getRetyrStatus() {
		return retyrStatus;
	}

	public void setRetyrStatus(String retyrStatus) {
		this.retyrStatus = retyrStatus;
	}

	public String getRetryPolicyId() {
		return retryPolicyId;
	}

	public void setRetryPolicyId(String retryPolicyId) {
		this.retryPolicyId = retryPolicyId;
	}

	public Timestamp getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Timestamp recordTime) {
		this.recordTime = recordTime;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getManualRetryFlag() {
		return manualRetryFlag;
	}

	@Override
	public String toString() {
		return "FailedRetryLogs [uniqueId=" + uniqueId + ", originalMsg=" + originalMsg + ", processstage="
				+ processstage + ", retryPolicyId=" + retryPolicyId + ", retryIndex=" + retryIndex + ", lasterrMsg="
				+ lasterrMsg + ", manualRetryFlag=" + manualRetryFlag + ", retyrStatus=" + retyrStatus + ", recordTime="
				+ recordTime + ", updatedTime=" + updatedTime + "]";
	}

}
