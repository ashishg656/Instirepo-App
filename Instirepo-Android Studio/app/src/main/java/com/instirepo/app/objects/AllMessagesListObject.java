package com.instirepo.app.objects;

import java.util.List;

public class AllMessagesListObject {

	List<SingleMessageListObj> names;

	public List<SingleMessageListObj> getNames() {
		return names;
	}

	public void setNames(List<SingleMessageListObj> names) {
		this.names = names;
	}

	public class SingleMessageListObj {

		int personid;
		String name, image, time, lastmessage;

		public int getPersonid() {
			return personid;
		}

		public void setPersonid(int personid) {
			this.personid = personid;
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

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getLastmessage() {
			return lastmessage;
		}

		public void setLastmessage(String lastmessage) {
			this.lastmessage = lastmessage;
		}
	}

}
