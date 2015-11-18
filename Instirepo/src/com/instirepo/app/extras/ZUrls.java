package com.instirepo.app.extras;

import com.instirepo.app.application.ZApplication;

public interface ZUrls {

	public String loginUrl = ZApplication.getBaseUrl() + "login_request/";

	public String getAllCollegesAndUniversities = ZApplication.getBaseUrl()
			+ "get_all_colleges_and_universities/";

	public String userRegistrationStep1Url = ZApplication.getBaseUrl()
			+ "get_college_batch_years_list/";

	public String userRegistrationStep2UrlForStudent = ZApplication
			.getBaseUrl() + "register_student_profile_details/";

	public String teacherPostsUrl = ZApplication.getBaseUrl()
			+ "get_teacher_posts/";

	public String studentsPostsUrl = ZApplication.getBaseUrl()
			+ "get_students_posts/";

}
