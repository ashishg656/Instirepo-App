package com.instirepo.app.objects;

import java.util.List;

public class SavedPostVisibilityCollectionObject {

	List<SavedPostVisibilityObj> visibilities;

	public List<SavedPostVisibilityObj> getVisibilities() {
		return visibilities;
	}

	public void setVisibilities(List<SavedPostVisibilityObj> visibilities) {
		this.visibilities = visibilities;
	}

	public class SavedPostVisibilityObj {
		int id;
		String name;

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

	}

}
