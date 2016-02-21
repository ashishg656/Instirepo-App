package com.instirepo.app.objects;

public class UpvotePostObject {

	String message;
	int upvotes, downvotes;
	boolean has_upvoted, has_downvoted;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

}
