package com.instirepo.app.extras;

import com.instirepo.app.application.ZApplication;

public interface ZUrls {

	public String loginUrl = ZApplication.getBaseUrl() + "login_request/";
	public String userRegistrationStep1Url = ZApplication.getBaseUrl()
			+ "get_college_batch_years_list/";
	public String userRegistrationStep2Url = ZApplication.getBaseUrl() + "";
	public String getAllCollegesAndUniversities = ZApplication.getBaseUrl()
			+ "get_all_colleges_and_universities/";

}
