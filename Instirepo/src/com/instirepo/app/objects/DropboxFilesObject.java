package com.instirepo.app.objects;

import java.util.Date;

public class DropboxFilesObject {

	String fileName, parentPath, path, mimeType, modified, rev, size;
	String fileLink;
	Long bytes;
	Date expires;

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public DropboxFilesObject(String fileName, String parentPath, String path,
			String mimeType, String modified, String rev, String size,
			String fileLink, Long bytes, Date expires) {
		super();
		this.fileName = fileName;
		this.parentPath = parentPath;
		this.path = path;
		this.mimeType = mimeType;
		this.modified = modified;
		this.rev = rev;
		this.size = size;
		this.fileLink = fileLink;
		this.bytes = bytes;
		this.expires = expires;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getRev() {
		return rev;
	}

	public void setRev(String rev) {
		this.rev = rev;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getFileLink() {
		return fileLink;
	}

	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

	public Long getBytes() {
		return bytes;
	}

	public void setBytes(Long bytes) {
		this.bytes = bytes;
	}

}
