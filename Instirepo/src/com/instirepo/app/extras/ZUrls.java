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

	public String studentPostsUrl = ZApplication.getBaseUrl()
			+ "get_students_posts/";

	public String getPeopleWhoSawPost = ZApplication.getBaseUrl()
			+ "get_people_who_saw_post/";

	public String getCommentsOnPost = ZApplication.getBaseUrl()
			+ "get_comments_on_post/";

	public String addCommentOnPost = ZApplication.getBaseUrl()
			+ "add_comment_on_post/";

	public String markPostAsImportant = ZApplication.getBaseUrl()
			+ "save_post_for_later/";

	public String upvotePost = ZApplication.getBaseUrl()
			+ "upvote_or_downvote_post/";

	public String userProfileViewedByOther = ZApplication.getBaseUrl()
			+ "user_profile_viewed_by_other/";

	public String upvoteUser = ZApplication.getBaseUrl()
			+ "upvote_or_downvote_user/";

	public String getAllPostCategories = ZApplication.getBaseUrl()
			+ "get_all_post_categories/";

	public String getAllTeachersList = ZApplication.getBaseUrl()
			+ "get_all_teachers_list/";

	public String savePostVisibilities = ZApplication.getBaseUrl()
			+ "save_post_visibility/";

}
