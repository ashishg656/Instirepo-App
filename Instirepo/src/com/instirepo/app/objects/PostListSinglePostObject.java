package com.instirepo.app.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class PostListSinglePostObject implements Parcelable {

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
	boolean is_saved;
	boolean is_following;
	boolean is_reported;
	int user_id;

	public PostListSinglePostObject() {

	}

	protected PostListSinglePostObject(Parcel in) {
		id = in.readInt();
		description = in.readString();
		heading = in.readString();
		image = in.readString();
		time = in.readString();
		user_image = in.readString();
		user_name = in.readString();
		upvotes = in.readInt();
		downvotes = in.readInt();
		has_upvoted = in.readByte() != 0x00;
		has_downvoted = in.readByte() != 0x00;
		comment = in.readInt();
		seens = in.readInt();
		category = in.readString();
		category_color = in.readString();
		saves = in.readInt();
		is_saved = in.readByte() != 0x00;
		is_following = in.readByte() != 0x00;
		is_reported = in.readByte() != 0x00;
		user_id = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(description);
		dest.writeString(heading);
		dest.writeString(image);
		dest.writeString(time);
		dest.writeString(user_image);
		dest.writeString(user_name);
		dest.writeInt(upvotes);
		dest.writeInt(downvotes);
		dest.writeByte((byte) (has_upvoted ? 0x01 : 0x00));
		dest.writeByte((byte) (has_downvoted ? 0x01 : 0x00));
		dest.writeInt(comment);
		dest.writeInt(seens);
		dest.writeString(category);
		dest.writeString(category_color);
		dest.writeInt(saves);
		dest.writeByte((byte) (is_saved ? 0x01 : 0x00));
		dest.writeByte((byte) (is_following ? 0x01 : 0x00));
		dest.writeByte((byte) (is_reported ? 0x01 : 0x00));
		dest.writeInt(user_id);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<PostListSinglePostObject> CREATOR = new Parcelable.Creator<PostListSinglePostObject>() {
		@Override
		public PostListSinglePostObject createFromParcel(Parcel in) {
			return new PostListSinglePostObject(in);
		}

		@Override
		public PostListSinglePostObject[] newArray(int size) {
			return new PostListSinglePostObject[size];
		}
	};

	public boolean isIs_reported() {
		return is_reported;
	}

	public void setIs_reported(boolean is_reported) {
		this.is_reported = is_reported;
	}

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
