package com.instirepo.app.objects;

public class PostListSinglePostObject {

	int id;
	String description;
	String heading;
	String image;
	String time;
	String user_image;
	String user_name;
	int upvotes;
	int downvotes;
	boolean has_upvoted;
	boolean has_downvoted;
	int comment;
	int seens;
	String category;
	String category_color;
	int saves;
	boolean is_saved, is_following;
	int user_id;

	public boolean isIs_following() {
		return is_following;
	}

	public void setIs_following(boolean is_following) {
		this.is_following = is_following;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getSaves() {
		return saves;
	}

	public void setSaves(int saves) {
		this.saves = saves;
	}

	public boolean isIs_saved() {
		return is_saved;
	}

	public void setIs_saved(boolean is_saved) {
		this.is_saved = is_saved;
	}

	public int getSeens() {
		return seens;
	}

	public void setSeens(int seens) {
		this.seens = seens;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory_color() {
		return category_color;
	}

	public void setCategory_color(String category_color) {
		this.category_color = category_color;
	}

	public int getUpvotes() {
		return upvotes;
	}

	public void setUpvotes(int upvotes) {
		this.upvotes = upvotes;
	}

	public int getDownvotes() {
		return downvotes;
	}

	public void setDownvotes(int downvotes) {
		this.downvotes = downvotes;
	}

	public boolean isHas_upvoted() {
		return has_upvoted;
	}

	public void setHas_upvoted(boolean has_upvoted) {
		this.has_upvoted = has_upvoted;
	}

	public boolean isHas_downvoted() {
		return has_downvoted;
	}

	public void setHas_downvoted(boolean has_downvoted) {
		this.has_downvoted = has_downvoted;
	}

	public int getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUser_image() {
		return user_image;
	}

	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

}
