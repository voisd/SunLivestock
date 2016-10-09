package com.voisd.sun.wxapi;

/**
 * 图片选择状态
 */
public class ImagesModel {

	public String url = null;
	public boolean status = false;

	public ImagesModel(String url, boolean status) {
		this.url = url;
		this.status = status;
	}
}
