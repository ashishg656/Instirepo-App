package com.instirepo.app.objects;

import java.util.List;

public class NotificationsListObject {

	List<NotificationsSingleObj> notifications;

	public List<NotificationsSingleObj> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<NotificationsSingleObj> notifications) {
		this.notifications = notifications;
	}

	public class NotificationsSingleObj {
		String image, image_url, text, web_url, time;
		boolean general_notification, is_author_of_post;

		int post_id, uploader_id;
		String post_name, post_image, uploader_name;

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getImage_url() {
			return image_url;
		}

		public void setImage_url(String image_url) {
			this.image_url = image_url;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getWeb_url() {
			return web_url;
		}

		public void setWeb_url(String web_url) {
			this.web_url = web_url;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public boolean isGeneral_notification() {
			return general_notification;
		}

		public void setGeneral_notification(boolean general_notification) {
			this.general_notification = general_notification;
		}

		public boolean isIs_author_of_post() {
			return is_author_of_post;
		}

		public void setIs_author_of_post(boolean is_author_of_post) {
			this.is_author_of_post = is_author_of_post;
		}

		public int getPost_id() {
			return post_id;
		}

		public void setPost_id(int post_id) {
			this.post_id = post_id;
		}

		public int getUploader_id() {
			return uploader_id;
		}

		public void setUploader_id(int uploader_id) {
			this.uploader_id = uploader_id;
		}

		public String getPost_name() {
			return post_name;
		}

		public void setPost_name(String post_name) {
			this.post_name = post_name;
		}

		public String getPost_image() {
			return post_image;
		}

		public void setPost_image(String post_image) {
			this.post_image = post_image;
		}

		public String getUploader_name() {
			return uploader_name;
		}

		public void setUploader_name(String uploader_name) {
			this.uploader_name = uploader_name;
		}

	}

}
