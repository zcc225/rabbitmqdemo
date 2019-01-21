package com.daixinlian.entity;

public class FileMap {

	private int id;
	private String fileId;
	private int tripType;
	private String createTime;
	private String fileMd5;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public int getTripType() {
		return tripType;
	}
	public void setTripType(int tripType) {
		this.tripType = tripType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getFileMd5() {
		return fileMd5;
	}
	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}
	
}
