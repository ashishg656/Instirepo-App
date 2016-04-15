package com.instirepo.app.objects;

import java.util.List;

public class MessageListObject {

	List<SingleMessage> messages;
	Integer next_page;

	public class SingleMessage {
		String time, message;
		boolean is_by_user;
		boolean not_delivered;
		Integer local_id;
		Integer server_id;

		public Integer getLocal_id() {
			return local_id;
		}

		public void setLocal_id(Integer local_id) {
			this.local_id = local_id;
		}

		public Integer getServer_id() {
			return server_id;
		}

		public void setServer_id(Integer server_id) {
			this.server_id = server_id;
		}

		public boolean isNot_delivered() {
			return not_delivered;
		}

		public void setNot_delivered(boolean not_delivered) {
			this.not_delivered = not_delivered;
		}

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
