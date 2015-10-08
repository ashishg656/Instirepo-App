package com.example.faceless.objects;

import java.util.List;

public class ZChatObject {

	List<ChatItem> chats;
	Integer next_page;

	public class ChatItem {

		String text;
		String time;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
	}

	public List<ChatItem> getChats() {
		return chats;
	}

	public void setChats(List<ChatItem> chats) {
		this.chats = chats;
	}

	public Integer getNext_page() {
		return next_page;
	}

	public void setNext_page(Integer next_page) {
		this.next_page = next_page;
	}

}
