package com.instirepo.app.objects;

import java.util.List;

public class MessageListObject {

	List<SingleMessage> messages;
	Integer next_page;

	public class SingleMessage {
		String time, message;
		boolean is_by_user;

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public boolean isIs_by_user() {
			return is_by_user;
		}

		public void setIs_by_user(boolean is_by_user) {
			this.is_by_user = is_by_user;
		}

	}

	public List<SingleMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<SingleMessage> messages) {
		this.messages = messages;
	}

	public Integer getNext_page() {
		return next_page;
	}

	public void setNext_page(Integer next_page) {
		this.next_page = next_page;
	}

}
