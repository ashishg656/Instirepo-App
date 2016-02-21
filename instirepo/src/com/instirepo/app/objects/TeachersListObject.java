package com.instirepo.app.objects;

import java.util.List;

public class TeachersListObject {

	List<TeacherListSingleTeacher> teachers;
	Integer next_page;

	public class TeacherListSingleTeacher {
		int id;
		String name, branch, image;

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

		public String getBranch() {
			return branch;
		}

		public void setBranch(String branch) {
			this.branch = branch;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

	}

	public List<TeacherListSingleTeacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherListSingleTeacher> teachers) {
		this.teachers = teachers;
	}

	public Integer getNext_page() {
		return next_page;
	}

	public void setNext_page(Integer next_page) {
		this.next_page = next_page;
	}

}
