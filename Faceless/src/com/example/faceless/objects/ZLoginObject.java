package com.example.faceless.objects;

public class ZLoginObject {

	boolean error;
	boolean password_change_required;
	int id;
	boolean is_admin;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isPassword_change_required() {
		return password_change_required;
	}

	public void setPassword_change_required(boolean password_change_required) {
		this.password_change_required = password_change_required;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isIs_admin() {
		return is_admin;
	}

	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}

}
