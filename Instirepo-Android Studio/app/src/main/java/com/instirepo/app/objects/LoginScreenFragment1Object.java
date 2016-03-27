package com.instirepo.app.objects;

import java.util.List;

public class LoginScreenFragment1Object {

	List<Universities> university_list;
	List<Colleges> colleges_list;

	public class Universities {
		String name;
		int id;
		String location;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

	}

	public class Colleges {
		int university_id;
		String name;
		String location;
		int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getUniversity_id() {
			return university_id;
		}

		public void setUniversity_id(int university_id) {
			this.university_id = university_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

	}

	public List<Universities> getUniversity_list() {
		return university_list;
	}

	public void setUniversity_list(List<Universities> university_list) {
		this.university_list = university_list;
	}

	public List<Colleges> getColleges_list() {
		return colleges_list;
	}

	public void setColleges_list(List<Colleges> colleges_list) {
		this.colleges_list = colleges_list;
	}

}
