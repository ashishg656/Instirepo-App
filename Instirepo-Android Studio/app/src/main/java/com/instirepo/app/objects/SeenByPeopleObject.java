package com.instirepo.app.objects;

import java.util.List;

public class SeenByPeopleObject {

	List<PeopleSeenPost> seens;
	Integer next_page;

	public class PeopleSeenPost {
		String time, image, name;
		int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public List<PeopleSeenPost> getSeens() {
		return seens;
	}

	public void setSeens(List<PeopleSeenPost> seens) {
		this.seens = seens;
	}

	public Integer getNext_page() {
		return next_page;
	}

	public void setNext_page(Integer next_page) {
		this.next_page = next_page;
	}

}
