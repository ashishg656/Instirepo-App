package com.instirepo.app.objects;

import java.util.List;

public class PostsListObject {

	List<PostListSinglePostObject> posts;
	Integer next_page;
	Boolean is_by_teacher;

	public Boolean getIs_by_teacher() {
		return is_by_teacher;
	}

	public void setIs_by_teacher(Boolean is_by_teacher) {
		this.is_by_teacher = is_by_teacher;
	}

	public Integer getNext_page() {
		return next_page;
	}

	public void setNext_page(Integer next_page) {
		this.next_page = next_page;
	}

	public List<PostListSinglePostObject> getPosts() {
		return posts;
	}

	public void setPosts(List<PostListSinglePostObject> posts) {
		this.posts = posts;
	}

}
