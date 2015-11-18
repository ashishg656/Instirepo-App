package com.instirepo.app.objects;

import java.util.List;

public class LoginScreenFragment2Object {

	List<Branches> branches_list;
	List<Years> years_list;
	List<Batches> batches_list;

	public class Branches {
		String branch_name;
		int branch_id;

		public String getBranch_name() {
			return branch_name;
		}

		public void setBranch_name(String branch_name) {
			this.branch_name = branch_name;
		}

		public int getBranch_id() {
			return branch_id;
		}

		public void setBranch_id(int branch_id) {
			this.branch_id = branch_id;
		}

	}

	public class Years {
		int branch_id;
		String year_name;
		long admission_year;
		long passout_year;
		boolean has_passed_out;
		int year_id;

		public int getBranch_id() {
			return branch_id;
		}

		public void setBranch_id(int branch_id) {
			this.branch_id = branch_id;
		}

		public String getYear_name() {
			return year_name;
		}

		public void setYear_name(String year_name) {
			this.year_name = year_name;
		}

		public long getAdmission_year() {
			return admission_year;
		}

		public void setAdmission_year(long admission_year) {
			this.admission_year = admission_year;
		}

		public long getPassout_year() {
			return passout_year;
		}

		public void setPassout_year(long passout_year) {
			this.passout_year = passout_year;
		}

		public boolean isHas_passed_out() {
			return has_passed_out;
		}

		public void setHas_passed_out(boolean has_passed_out) {
			this.has_passed_out = has_passed_out;
		}

		public int getYear_id() {
			return year_id;
		}

		public void setYear_id(int year_id) {
			this.year_id = year_id;
		}

	}

	public class Batches {
		int year_id;
		String batch_name;
		int batch_id;

		public int getYear_id() {
			return year_id;
		}

		public void setYear_id(int year_id) {
			this.year_id = year_id;
		}

		public String getBatch_name() {
			return batch_name;
		}

		public void setBatch_name(String batch_name) {
			this.batch_name = batch_name;
		}

		public int getBatch_id() {
			return batch_id;
		}

		public void setBatch_id(int batch_id) {
			this.batch_id = batch_id;
		}

	}

	public List<Branches> getBranches_list() {
		return branches_list;
	}

	public void setBranches_list(List<Branches> branches_list) {
		this.branches_list = branches_list;
	}

	public List<Years> getYears_list() {
		return years_list;
	}

	public void setYears_list(List<Years> years_list) {
		this.years_list = years_list;
	}

	public List<Batches> getBatches_list() {
		return batches_list;
	}

	public void setBatches_list(List<Batches> batches_list) {
		this.batches_list = batches_list;
	}

}
