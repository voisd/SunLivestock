package com.voisd.sun.wxapi;

/**
 * 图片实体类
 */
public class ImagesEntry {
	public String bucketName;
	public int bucketId;
	public String bucketUrl = null;
	
	public int bucketSize = 0;

	public ImagesEntry(int id, String name, String url) {
		bucketId = id;
		bucketName = ensureNotNull(name);
		bucketUrl = url;
	}
	
	public int getBucketSize() {
		return bucketSize;
	}

	public void setBucketSize(int bucketSize) {
		this.bucketSize = bucketSize;
	}

	@Override
	public int hashCode() {
		return bucketId;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ImagesEntry)) return false;
		ImagesEntry entry = (ImagesEntry) object;
		return bucketId == entry.bucketId;
	}

	public static String ensureNotNull(String value) {
		return value == null ? "" : value;
	}
}
