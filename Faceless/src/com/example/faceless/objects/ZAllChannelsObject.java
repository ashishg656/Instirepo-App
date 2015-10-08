package com.example.faceless.objects;

import java.util.List;

public class ZAllChannelsObject {

	List<ChannelObject> channels;

	public class ChannelObject {
		String name;
		int id;

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
	}

	public List<ChannelObject> getChannels() {
		return channels;
	}

	public void setChannels(List<ChannelObject> channels) {
		this.channels = channels;
	}

}
