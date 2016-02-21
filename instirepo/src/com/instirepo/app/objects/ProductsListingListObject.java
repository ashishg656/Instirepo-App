package com.instirepo.app.objects;

import java.util.List;

public class ProductsListingListObject {

	List<ProductObjectSingle> products;
	Integer next_page;

	public List<ProductObjectSingle> getProducts() {
		return products;
	}

	public void setProducts(List<ProductObjectSingle> products) {
		this.products = products;
	}

	public Integer getNext_page() {
		return next_page;
	}

	public void setNext_page(Integer next_page) {
		this.next_page = next_page;
	}

}
