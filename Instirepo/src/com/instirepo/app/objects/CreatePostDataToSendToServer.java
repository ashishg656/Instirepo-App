package com.instirepo.app.objects;

public class CreatePostDataToSendToServer {

	String heading, description, companyName, coverPicImage;
	int categoryId;
	int typeOfPostVisibilty;

	int savedCollectionId;

	public CreatePostDataToSendToServer(String heading, String description,
			String companyName, String coverPicImage, int categoryId,
			int typeOfPostVisibilty) {
		super();
		this.heading = heading;
		this.description = description;
		this.companyName = companyName;
		this.coverPicImage = coverPicImage;
		this.categoryId = categoryId;
		this.typeOfPostVisibilty = typeOfPostVisibilty;
	}

	public String getHeading() {
		return heading;
	}

	public int getSavedCollectionId() {
		return savedCollectionId;
	}

	public void setSavedCollectionId(int savedCollectionId) {
		this.savedCollectionId = savedCollectionId;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCoverPicImage() {
		return coverPicImage;
	}

	public void setCoverPicImage(String coverPicImage) {
		this.coverPicImage = coverPicImage;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getTypeOfPostVisibilty() {
		return typeOfPostVisibilty;
	}

	public void setTypeOfPostVisibilty(int typeOfPostVisibilty) {
		this.typeOfPostVisibilty = typeOfPostVisibilty;
	}

}
