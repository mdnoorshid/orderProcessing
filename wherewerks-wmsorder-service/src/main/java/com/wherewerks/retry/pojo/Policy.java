package com.wherewerks.retry.pojo;

public class Policy {

	private String retryPolicyId;
	private int retryIntrvl;
	private int retryCount;

	public String getRetryPolicyId() {
		return retryPolicyId;
	}

	public void setRetryPolicyId(String retryPolicyId) {
		this.retryPolicyId = retryPolicyId;
	}

	public int getRetryIntrvl() {
		return retryIntrvl;
	}

	public void setRetryIntrvl(int retryIntrvl) {
		this.retryIntrvl = retryIntrvl;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	@Override
	public String toString() {
		return "Policy [retryPolicyId=" + retryPolicyId + ", retryIntrvl=" + retryIntrvl + ", retryCount=" + retryCount
				+ "]";
	}

}
