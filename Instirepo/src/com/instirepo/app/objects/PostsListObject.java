package com.instirepo.app.objects;

import java.util.List;

public class PostsListObject {

	List<PostListSinglePostObject> posts;
	Integer next_page;

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
