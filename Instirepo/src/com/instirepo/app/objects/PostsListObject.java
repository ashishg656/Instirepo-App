package com.instirepo.app.objects;

import java.util.List;

public class PostsListObject {

	List<PostListSinglePostObject> posts;
	List<PostCategorySinglePostCategory> categories;

	public List<PostCategorySinglePostCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<PostCategorySinglePostCategory> categories) {
		this.categories = categories;
	}

	public List<PostListSinglePostObject> getPosts() {
		return posts;
	}

	public void setPosts(List<PostListSinglePostObject> posts) {
		this.posts = posts;
	}

}
