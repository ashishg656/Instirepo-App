package com.instirepo.app.objects;

import java.util.List;

import com.instirepo.app.objects.AllPostCategoriesObject.PostCategorySingle;

public class ProductCategoriesListObject {

	List<ProductObjectSingle> trending_products;
	List<ProductObjectSingle> recently_viewed;
	List<ProductObjectSingle> books;
	List<PostCategorySingle> categories;

	public List<ProductObjectSingle> getTrending_products() {
		return trending_products;
	}

	public void setTrending_products(List<ProductObjectSingle> trending_products) {
		this.trending_products = trending_products;
	}

	public List<ProductObjectSingle> getRecently_viewed() {
		return recently_viewed;
	}

	public void setRecently_viewed(List<ProductObjectSingle> recently_viewed) {
		this.recently_viewed = recently_viewed;
	}

	public List<ProductObjectSingle> getBooks() {
		return books;
	}

	public void setBooks(List<ProductObjectSingle> books) {
		this.books = books;
	}

	public List<PostCategorySingle> getCategories() {
		return categories;
	}

	public void setCategories(List<PostCategorySingle> categories) {
		this.categories = categories;
	}

}
