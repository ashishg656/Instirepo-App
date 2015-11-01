package com.instirepo.app.objects;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class PostCategoriesListObject implements Parcelable {

	public PostCategoriesListObject() {

	}

	public PostCategoriesListObject(Parcel in) {
		categories = new ArrayList<>();
		in.readList(categories,
				PostCategorySinglePostCategory.class.getClassLoader());
	}

	List<PostCategorySinglePostCategory> categories;

	public List<PostCategorySinglePostCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<PostCategorySinglePostCategory> categories) {
		this.categories = categories;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(categories);
	}

	public static final Creator<PostCategoriesListObject> CREATOR = new Creator<PostCategoriesListObject>() {
		@Override
		public PostCategoriesListObject createFromParcel(Parcel in) {
			return new PostCategoriesListObject(in);
		}

		@Override
		public PostCategoriesListObject[] newArray(int size) {
			return new PostCategoriesListObject[size];
		}
	};

}
