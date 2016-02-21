package com.instirepo.app.objects;

import java.util.List;

public class AllPostCategoriesObject {

	public static final String categoryOther = "other";
	public static final String categoryEvent = "event";
	public static final String categoryPlacement = "placement";
	public static final String categoryPoll = "poll";

	List<PostCategorySingle> categories;
	boolean error;
	String message;

	public void setCategories(List<PostCategorySingle> categories) {
		this.categories = categories;
	}

	public class PostCategorySingle {
		int id;
		String name, image, type, color;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<PostCategorySingle> getCategories() {
		return categories;
	}

}
