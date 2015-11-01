package com.instirepo.app.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class PostCategorySinglePostCategory implements Parcelable {

	int type;
	String name;
	String color;

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(color);
		dest.writeInt(type);
	}

	public PostCategorySinglePostCategory(Parcel in) {
		name = in.readString();
		color = in.readString();
		type = in.readInt();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public PostCategorySinglePostCategory() {
	}

	public static final Creator<PostCategorySinglePostCategory> CREATOR = new Creator<PostCategorySinglePostCategory>() {
		@Override
		public PostCategorySinglePostCategory createFromParcel(Parcel in) {
			return new PostCategorySinglePostCategory(in);
		}

		@Override
		public PostCategorySinglePostCategory[] newArray(int size) {
			return new PostCategorySinglePostCategory[size];
		}
	};

}