package com.instirepo.app.objects;

public class UserProfileViewedByOtherObject {

	String name, image, designation, about, branch, batch, year, resume, email,
			phone;
	int number_of_posts;
	boolean is_student_coordinator, can_message, is_professor,
			is_senior_professor;

	int upvotes;
	int downvotes;
	boolean has_upvoted;
	boolean has_downvoted, is_blocked;

	public boolean isIs_blocked() {
		return is_blocked;
	}

	public void setIs_blocked(boolean is_blocked) {
		this.is_blocked = is_blocked;
	}

	public boolean isIs_professor() {
		return is_professor;
	}

	public void setIs_professor(boolean is_professor) {
		this.is_professor = is_professor;
	}

	public boolean isIs_senior_professor() {
		return is_senior_professor;
	}

	public void setIs_senior_professor(boolean is_senior_professor) {
		this.is_senior_professor = is_senior_professor;
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

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getNumber_of_posts() {
		return number_of_posts;
	}

	public void setNumber_of_posts(int number_of_posts) {
		this.number_of_posts = number_of_posts;
	}

	public boolean isIs_student_coordinator() {
		return is_student_coordinator;
	}

	public void setIs_student_coordinator(boolean is_student_coordinator) {
		this.is_student_coordinator = is_student_coordinator;
	}

	public boolean isCan_message() {
		return can_message;
	}

	public void setCan_message(boolean can_message) {
		this.can_message = can_message;
	}

}
