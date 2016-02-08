package com.instirepo.app.extras;

import com.instirepo.app.application.ZApplication;

/**
 * @author Ashish Goel
 * 
 */
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

	public String getSavedPostVisibilities = ZApplication.getBaseUrl()
			+ "get_saved_post_visibilities/";

	public String getPostsPostedByUser = ZApplication.getBaseUrl()
			+ "get_posts_posted_by_user/";

	public String getAllMessagesList = ZApplication.getBaseUrl()
			+ "get_all_messages_list/";

	public String getMessagesForOneChat = ZApplication.getBaseUrl()
			+ "get_messages_for_one_user/";

	public String addMessageToChats = ZApplication.getBaseUrl()
			+ "add_message_to_chats/";

	public String addGCMTokenToServer = ZApplication.getBaseUrl()
			+ "add_gcm_token_for_user/";

	public String blockUserRequestUrl = ZApplication.getBaseUrl()
			+ "block_user_request/";

	public String flagCommentOnPost = ZApplication.getBaseUrl()
			+ "flag_comment_on_post/";

	public String getNotificationsForUser = ZApplication.getBaseUrl()
			+ "get_notifications_for_user/";

	public String followPostRequest = ZApplication.getBaseUrl()
			+ "follow_post/";

	public String reportPostUrl = ZApplication.getBaseUrl() + "report_post/";

	public String uploadPostUrl = ZApplication.getBaseUrl() + "upload_post/";

	public String userProfileViewedByHimself = ZApplication.getBaseUrl()
			+ "user_profile_viewed_by_himself/";

	public String changeEmailVisibility = ZApplication.getBaseUrl()
			+ "change_email_visibility/";

	public String changePhoneVisibility = ZApplication.getBaseUrl()
			+ "change_phone_visibility/";

	public String deleteResumeRequest = ZApplication.getBaseUrl()
			+ "delete_resume/";

	public String uploadResumeRequest = ZApplication.getBaseUrl()
			+ "upload_resume_request/";

	public String getFavouritePosts = ZApplication.getBaseUrl()
			+ "get_posts_marked_important_by_user/";

	public String editAboutUrl = ZApplication.getBaseUrl()
			+ "change_about_for_user/";

	public String editPhoneUrl = ZApplication.getBaseUrl()
			+ "change_mobile_number_for_user/";

	public String postDescriptionPage = ZApplication.getBaseUrl()
			+ "post_detail_request/";

	/**
	 * ecommerce urls
	 * 
	 */

	public String productCategoriesAndTrendingProducts = ZApplication
			.getEcommerceUrl()
			+ "get_all_product_categories_and_trending_and_recent_products/?";

	public String productListingByCategory = ZApplication.getEcommerceUrl()
			+ "get_products_from_category/?";

}
