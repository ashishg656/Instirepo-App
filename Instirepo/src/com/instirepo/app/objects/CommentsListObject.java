package com.instirepo.app.objects;

import java.util.List;

public class CommentsListObject {

	List<CommentObject> comments;
	int count;
	Integer next_page;

	public class CommentObject {
		int id;
		String comment, time, user_name, user_image;
		boolean is_by_user, is_different_color;

		public boolean isIs_different_color() {
			return is_different_color;
		}

		public void setIs_different_color(boolean is_different_color) {
			this.is_different_color = is_different_color;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getUser_image() {
			return user_image;
		}

		public void setUser_image(String user_image) {
			this.user_image = user_image;
		}

		public boolean isIs_by_user() {
			return is_by_user;
		}

		public void setIs_by_user(boolean is_by_user) {
			this.is_by_user = is_by_user;
		}

	}

	public List<CommentObject> getComments() {
		return comments;
	}

	public void setComments(List<CommentObject> comments) {
		this.comments = comments;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Integer getNext_page() {
		return next_page;
	}

	public void setNext_page(Integer next_page) {
		this.next_page = next_page;
	}

}
