package com.instirepo.app.objects;

public class CreatePostDataToSendToServer {

	String heading, description, companyName, coverPicImage;
	int categoryId;
	int typeOfPostVisibilty;

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
}
