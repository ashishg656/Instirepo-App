package com.instirepo.app.objects;

import java.util.List;

public class UserProfileEditProfileObject {

	String name, image, designation, about;
	boolean is_student_coordinator, is_professor, is_senior_professor,
			is_email_shown_to_others, is_mobile_shown_to_others;
	String branch, batch, year, resume, email, phone, enrollment_number;
	int number_of_posts, upvotes, downvotes;
	List<String> images_header_send;

	public List<String> getImages_header_send() {
		return images_header_send;
	}

	public void setImages_header_send(List<String> images_header_send) {
		this.images_header_send = images_header_send;
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

	public boolean isIs_student_coordinator() {
		return is_student_coordinator;
	}

	public void setIs_student_coordinator(boolean is_student_coordinator) {
		this.is_student_coordinator = is_student_coordinator;
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

	public boolean isIs_email_shown_to_others() {
		return is_email_shown_to_others;
	}

	public void setIs_email_shown_to_others(boolean is_email_shown_to_others) {
		this.is_email_shown_to_others = is_email_shown_to_others;
	}

	public boolean isIs_mobile_shown_to_others() {
		return is_mobile_shown_to_others;
	}

	public void setIs_mobile_shown_to_others(boolean is_mobile_shown_to_others) {
		this.is_mobile_shown_to_others = is_mobile_shown_to_others;
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

	public String getEnrollment_number() {
		return enrollment_number;
	}

	public void setEnrollment_number(String enrollment_number) {
		this.enrollment_number = enrollment_number;
	}

	public int getNumber_of_posts() {
		return number_of_posts;
	}

	public void setNumber_of_posts(int number_of_posts) {
		this.number_of_posts = number_of_posts;
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
}
