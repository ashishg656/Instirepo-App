package com.instirepo.app.activities;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.extras.ZCircularAnimatorListener;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.floatingactionbutton.FloatingActionButton;
import com.instirepo.app.floatingactionbutton.FloatingActionMenu;
import com.instirepo.app.fragments.CommentsPostsFragment;
import com.instirepo.app.fragments.PostsByStudentsFragment;
import com.instirepo.app.fragments.PostsByTeachersFragment;
import com.instirepo.app.fragments.ProductsCategoriesFragment;
import com.instirepo.app.fragments.SeenByPeopleFragment;
import com.instirepo.app.fragments.SelectPostCategoryFragment;
import com.instirepo.app.fragments.TravelFragment;
import com.instirepo.app.fragments.UserProfileViewedByOtherFragment;
import com.instirepo.app.objects.AllPostCategoriesObject;
import com.instirepo.app.objects.RideShareRequestObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.AppRequestListener;
import com.instirepo.app.serverApi.CustomStringRequest;
import com.instirepo.app.serverApi.ImageRequestManager;
import com.instirepo.app.serverApi.ImageRequestManager.RequestBitmap;
import com.instirepo.app.widgets.CircularImageView;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends BaseActivity implements OnPageChangeListener, AppConstants, OnClickListener, ZUrls,
        ConnectionCallbacks, OnConnectionFailedListener, AppRequestListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    MyPagerAdapter adapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView navigationDrawerUserName, navigationDrawerEmail;
    CircularImageView navigationDrawerImageUser;
    ImageView navigationDrawerImageDefault;
    FrameLayout navigationDrawerHeaderLayout;
    public static final int TRANSLATION_DURATION = 200;
    boolean isToolbarAnimRunning;
    AppBarLayout appBarLayout;

    AlertDialog alertDialog;

    FloatingActionMenu floatingActionMenu;
    FloatingActionButton createPostButton;
    View fabBackground;
    int fabRevealMargin;
    int deviceHeight, deviceWidth;
    boolean isFabAnimRunning;
    int maxFloatingActionButtonTranslation;
    ProgressDialog progressDialog;
    Snackbar snackbar;

    public AllPostCategoriesObject allPostCategoriesObject;
    private GoogleApiClient mGoogleApiClient;

    FrameLayout splashActivityLayout;
    HashMap<Integer, Fragment> fragmentHashMap;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);

        fragmentHashMap = new HashMap<>();

        fabRevealMargin = getResources().getDimensionPixelSize(R.dimen.z_fab_home_dimension_for_reveal);
        deviceHeight = getResources().getDisplayMetrics().heightPixels;
        deviceWidth = getResources().getDisplayMetrics().widthPixels;
        maxFloatingActionButtonTranslation = getResources()
                .getDimensionPixelSize(R.dimen.floating_action_button_height_with_margin_bottom_considered);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API).addApi(LocationServices.API).addScope(new Scope(Scopes.PROFILE)).build();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.pager_launch);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fabmenuy);
        fabBackground = (View) findViewById(R.id.revealviewfab);
        createPostButton = (FloatingActionButton) findViewById(R.id.createpostbutton);

        View header = navigationView.getHeaderView(0);

        navigationDrawerEmail = (TextView) header.findViewById(R.id.navdraweremail);
        navigationDrawerImageUser = (CircularImageView) header.findViewById(R.id.avatar);
        navigationDrawerImageDefault = (ImageView) header.findViewById(R.id.avatardefault);
        navigationDrawerUserName = (TextView) header.findViewById(R.id.navdrawerusername);
        navigationDrawerHeaderLayout = (FrameLayout) header.findViewById(R.id.navigationdrawerheader);

        splashActivityLayout = (FrameLayout) findViewById(R.id.splashactivitycontent);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Instirepo");

        createPostButton.setOnClickListener(this);
        findViewById(R.id.sellproduct).setOnClickListener(this);
        findViewById(R.id.rideshare).setOnClickListener(this);

        floatingActionMenu.setOnMenuButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (fabBackground.getVisibility() == View.GONE) {
                    showFloatingActionMenu();
                } else {
                    hideFloatingActionMenu();
                }
            }
        });

        toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                try {
                    toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } catch (Exception e) {
                    toolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                toolbarHeight = toolbar.getHeight();
            }
        });

        if (ZPreferences.isUserLogIn(this)) {
            navigationDrawerEmail.setText(ZPreferences.getUserEmail(this));
            navigationDrawerUserName.setText(ZPreferences.getUserName(this));
            ImageRequestManager.get(this).requestImage2(this, navigationDrawerImageUser,
                    ZPreferences.getUserImageURL(this), new RequestBitmap() {

                        @Override
                        public void onRequestCompleted(Bitmap bitmap) {
                            navigationDrawerImageUser.setImageBitmap(bitmap);
                            navigationDrawerImageDefault.setVisibility(View.GONE);
                        }
                    }, -1);
        } else {
            navigationDrawerEmail.setText("Please Login");
            navigationDrawerUserName.setText("Instirepo");
            navigationDrawerImageUser.setVisibility(View.GONE);
        }
        navigationDrawerHeaderLayout.setOnClickListener(this);

        setDrawerActionBarToggle();
        setDrawerItemClickListener();
        viewPager.setOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        if (getIntent().hasExtra("showsplash") && getIntent().getExtras().getBoolean("showsplash")) {
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    splashActivityLayout.animate().alpha(0).setDuration(100).setListener(new ZAnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            splashActivityLayout.setVisibility(View.GONE);
                        }
                    });
                }
            }, SplashActivity.splashDuration);
        } else {
            splashActivityLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void hideFloatingActionMenu() {
        if (!isFabAnimRunning) {
            isFabAnimRunning = true;
            SupportAnimator anim = ViewAnimationUtils.createCircularReveal(fabBackground, deviceWidth - fabRevealMargin,
                    deviceHeight - 2 * fabRevealMargin, deviceHeight, 0);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(400);
            anim.addListener(new ZCircularAnimatorListener() {
                @Override
                public void onAnimationEnd() {
                    isFabAnimRunning = false;
                    fabBackground.setVisibility(View.GONE);
                }
            });
            anim.start();
            floatingActionMenu.toggle(true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Z_HOME_ACTIVITY_SLECT_CATEGORY_FRAGMENT_TAG);
        if (fragment != null) {
            onBackPressed();
        }
    }

    protected void showFloatingActionMenu() {
        if (!isFabAnimRunning) {
            isFabAnimRunning = true;
            fabBackground.setVisibility(View.VISIBLE);
            SupportAnimator anim = ViewAnimationUtils.createCircularReveal(fabBackground, deviceWidth - fabRevealMargin,
                    deviceHeight - 2 * fabRevealMargin, 0, deviceHeight);
            anim.setDuration(500);
            anim.addListener(new ZCircularAnimatorListener() {
                @Override
                public void onAnimationEnd() {
                    isFabAnimRunning = false;
                }
            });
            anim.start();
            floatingActionMenu.toggle(true);
        }
    }

    private void setDrawerItemClickListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.logoutfromapp:
                        ZPreferences.setIsUserLogin(HomeActivity.this, false);
                        ZPreferences.setUserProfileID(HomeActivity.this, null);
                        ZPreferences.setDropboxToken(HomeActivity.this, null);
                        if (mGoogleApiClient.isConnected()) {
                            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                            mGoogleApiClient.disconnect();
                        }
                        Intent intent = new Intent(HomeActivity.this, LaunchActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        HomeActivity.this.finish();
                        return true;
                    case R.id.allmessagesactivity:
                        openMessagesListActivity();
                        return true;
                    case R.id.opennotificaton:
                        openNotificationsActivity();
                        return true;
                    case R.id.contactnav:
                        sendEmailIntentUsingToAction(getResources().getString(R.string.instirepo_support_email));
                        return true;
                    case R.id.aboutus:
                        intent = new Intent(HomeActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.shareappplaystore:
                        openPlayStore();
                        return true;
                    case R.id.sharepapintemt:
                        shareApplicationIntent();
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    protected void openPlayStore() {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    void shareApplicationIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Checkout Instirepo - A platform for college students and teachers to discuss and share anything and buy/sell products. Download : http://play.google.com/store/apps/details?id="
                        + this.getPackageName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void setDrawerActionBarToggle() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.z_open_drawer, R.string.z_close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setItemIconTintList(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int arg1, Intent arg2) {
        super.onActivityResult(requestCode, arg1, arg2);
        if (requestCode == TravelFragment.REQUEST_CHECK_SETTINGS) {
            fragmentHashMap.get(3).onActivityResult(requestCode, arg1, arg2);
        }
    }


    class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            Bundle bundle = new Bundle();

            Fragment fragment = null;
            if (pos == 0)
                fragment = PostsByTeachersFragment.newInstance(bundle);
            else if (pos == 1)
                fragment = PostsByStudentsFragment.newInstance(bundle);
            else if (pos == 3)
                fragment = TravelFragment.newInstance(bundle);
            else
                fragment = ProductsCategoriesFragment.newInstance(bundle);

            fragmentHashMap.put(pos, fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Posts By Teachers";
            } else if (position == 1) {
                return "Posts By Students";
            } else if (position == 3) {
                return "Ride Share";
            } else {
                return "Products";
            }
        }
    }

    @SuppressLint("NewApi")
    public void scrollToolbarBy(int dy) {
        float requestedTranslation = appBarLayout.getTranslationY() + dy;
        if (requestedTranslation < -toolbarHeight) {
            requestedTranslation = -toolbarHeight;
            appBarLayout.setTranslationY(requestedTranslation);
        } else if (requestedTranslation > 0) {
            requestedTranslation = 0;
            appBarLayout.setTranslationY(requestedTranslation);
        } else if (requestedTranslation >= -toolbarHeight && requestedTranslation <= 0) {
            appBarLayout.setTranslationY(requestedTranslation);
        }

        scrollFABBy(-dy);
    }

    void scrollFABBy(int dy) {
        float requestedTranslation = floatingActionMenu.getTranslationY() + dy;
        if (requestedTranslation > maxFloatingActionButtonTranslation) {
            requestedTranslation = maxFloatingActionButtonTranslation;
            floatingActionMenu.setTranslationY(requestedTranslation);
        } else if (requestedTranslation < 0) {
            requestedTranslation = 0;
            floatingActionMenu.setTranslationY(requestedTranslation);
        } else if (requestedTranslation <= maxFloatingActionButtonTranslation && requestedTranslation >= 0) {
            floatingActionMenu.setTranslationY(requestedTranslation);
        }
    }

    @SuppressLint("NewApi")
    public void scrollToolbarAfterTouchEnds() {
        float currentTranslation = -appBarLayout.getTranslationY();
        if (currentTranslation > toolbarHeight / 2) {
            animateToolbarLayout(-toolbarHeight);
            animateFABLayout(maxFloatingActionButtonTranslation);
        } else {
            animateToolbarLayout(0);
            animateFABLayout(0);
        }
    }

    public void setToolbarTranslation(View firstChild) {
        if (firstChild.getTop() > appBarLayout.getHeight()) {
            animateToolbarLayout(0);
            animateFABLayout(0);
        } else {
            scrollToolbarAfterTouchEnds();
        }
    }

    public void setToolbarTranslationFromPositionOfTopChildAfterTouchEnd(int pos) {
        if (pos > appBarLayout.getHeight()) {
            animateToolbarLayout(0);
            animateFABLayout(0);
        } else {
            scrollToolbarAfterTouchEnds();
        }
    }

    void animateToolbarLayout(int trans) {
        appBarLayout.animate().translationY(trans).setDuration(TRANSLATION_DURATION)
                .setInterpolator(new DecelerateInterpolator()).setListener(new ZAnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                isToolbarAnimRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isToolbarAnimRunning = false;
            }
        });
    }

    void animateFABLayout(int trans) {
        floatingActionMenu.animate().translationY(trans).setDuration(TRANSLATION_DURATION)
                .setInterpolator(new DecelerateInterpolator()).setListener(new ZAnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (appBarLayout.getTranslationY() != 0 && !isToolbarAnimRunning) {
            animateToolbarLayout(0);
        }
    }

    @Override
    public void onPageSelected(int arg0) {

    }

    public void switchToSeenByPeopleFragment(int postid) {
        Bundle bundle = new Bundle();
        bundle.putInt("postid", postid);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentcontainer, SeenByPeopleFragment.newInstance(bundle)).addToBackStack("tag")
                .commit();
    }

    public void switchToCommentsFragment(int postid) {
        Bundle bundle = new Bundle();
        bundle.putInt("postid", postid);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentcontainer, CommentsPostsFragment.newInstance(bundle),
                        Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG)
                .addToBackStack("Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG").commit();
    }

    void switchToSelectPostCategoryFragment() {
        Bundle bundle = new Bundle();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentcontainer, SelectPostCategoryFragment.newInstance(bundle),
                        Z_HOME_ACTIVITY_SLECT_CATEGORY_FRAGMENT_TAG)
                .addToBackStack(Z_HOME_ACTIVITY_SLECT_CATEGORY_FRAGMENT_TAG).commit();
    }

    public void switchToUserProfileViewedByOtherFragment(int userid, String name, String image) {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putInt("userid", userid);
        bundle.putString("image", image);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentcontainer, UserProfileViewedByOtherFragment.newInstance(bundle),
                        Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG)
                .addToBackStack(Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG).commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragmentUserProfile = getSupportFragmentManager()
                .findFragmentByTag(Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG);
        Fragment fragmentComments = getSupportFragmentManager()
                .findFragmentByTag(Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG);
        Fragment fragmentUserProfileOpenedFromCommentsListAdapter = getSupportFragmentManager()
                .findFragmentByTag(Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG_FROM_COMMENT_LIST_ADAPTER);

        if (fragmentUserProfileOpenedFromCommentsListAdapter != null
                && !((UserProfileViewedByOtherFragment) fragmentUserProfileOpenedFromCommentsListAdapter).fragmentDestroyed) {
            ((UserProfileViewedByOtherFragment) fragmentUserProfileOpenedFromCommentsListAdapter)
                    .dismissScrollViewDownCalledFromActivityBackPressed();
        } else if (fragmentUserProfileOpenedFromCommentsListAdapter != null
                && ((UserProfileViewedByOtherFragment) fragmentUserProfileOpenedFromCommentsListAdapter).fragmentDestroyed) {
            super.onBackPressed();
        } else if (fragmentUserProfile != null
                && !((UserProfileViewedByOtherFragment) fragmentUserProfile).fragmentDestroyed) {
            ((UserProfileViewedByOtherFragment) fragmentUserProfile)
                    .dismissScrollViewDownCalledFromActivityBackPressed();
        } else if (fragmentComments != null) {
            CommentsPostsFragment frg = (CommentsPostsFragment) fragmentComments;
            if (frg.shouldGoBackOnBackButtonPress())
                super.onBackPressed();
        } else if (fabBackground.getVisibility() == View.VISIBLE) {
            hideFloatingActionMenu();
        } else if (drawerLayout.isDrawerVisible(navigationView)) {
            drawerLayout.closeDrawers();
        } else
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createpostbutton:
                requestAllPostCategories();
                break;
            case R.id.navigationdrawerheader:
                if (ZPreferences.isUserLogIn(this)) {
                    Intent intent = new Intent(this, UserProfileActivity.class);
                    startActivity(intent);
                } else {
                    makeToast("Please Login");
                    Intent i = new Intent(this, LaunchActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.sellproduct:
                hideFloatingActionMenu();
                openSellProductActivity();
                break;
            case R.id.rideshare:
                openRideShareDialog();
                break;
            default:
                break;
        }
    }

    private void openRideShareDialog() {
        if (((TravelFragment) fragmentHashMap.get(3)) == null)
            return;

        if (((TravelFragment) fragmentHashMap.get(3)).location == null) {
            ((TravelFragment) fragmentHashMap.get(3)).permissionDeniedCount = 0;
            ((TravelFragment) fragmentHashMap.get(3)).createLocationRequest();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View alertView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.ride_share_request_dialog, null, false);

            final TextView nameOfCar = (TextView) alertView.findViewById(R.id.name);
            final TextView numberOfSeats = (TextView) alertView.findViewById(R.id.seats);

            builder.setView(alertView);
            builder.setPositiveButton("DONE", null);
            builder.setNegativeButton("CANCEL",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button b = ((AlertDialog) dialog)
                            .getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (nameOfCar.getText().toString().trim().length() == 0 || numberOfSeats.getText().toString().trim().length() == 0) {
                                makeToast("Please enter correct information");
                            } else {
                                HashMap<String, String> p = new HashMap<String, String>();
                                p.put("car_name", nameOfCar.getText().toString().trim());
                                p.put("seats", numberOfSeats.getText().toString().trim());
                                p.put("user_id", ZPreferences.getUserProfileID(HomeActivity.this));
                                p.put("lat", Double.toString(((TravelFragment) fragmentHashMap.get(3)).location.getLatitude()));
                                p.put("long", Double.toString(((TravelFragment) fragmentHashMap.get(3)).location.getLongitude()));
                                CustomStringRequest req = new CustomStringRequest(Method.POST, uploadRide, uploadRide, HomeActivity.this, p);
                                ZApplication.getInstance().addToRequestQueue(req, uploadRide);
                            }
                        }
                    });
                }
            });
            alertDialog.show();
        }
    }

    private void requestAllPostCategories() {
        progressDialog = ProgressDialog.show(this, "Loading", "Verifying and getting categories..");

        StringRequest req = new StringRequest(Method.POST, getAllPostCategories, new Listener<String>() {

            @Override
            public void onResponse(String arg0) {
                if (progressDialog != null)
                    progressDialog.dismiss();

                AllPostCategoriesObject obj = new Gson().fromJson(arg0, AllPostCategoriesObject.class);
                HomeActivity.this.allPostCategoriesObject = obj;

                if (obj.isError()) {
                    snackbar = Snackbar.make(findViewById(R.id.coordinatorlayout), obj.getMessage(),
                            Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                    hideFloatingActionMenu();
                } else {
                    hideFloatingActionMenu();
                    switchToSelectPostCategoryFragment();
                }
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                if (progressDialog != null)
                    progressDialog.dismiss();

                showSnackBar("Error..Check Internet Connection");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> p = new HashMap<>();
                p.put("user_id", ZPreferences.getUserProfileID(HomeActivity.this));
                return p;
            }
        };
        ZApplication.getInstance().addToRequestQueue(req, getAllPostCategories);
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {

    }

    @Override
    public void onConnected(Bundle arg0) {

    }

    @Override
    public void onConnectionSuspended(int arg0) {

    }

    @Override
    public void onRequestStarted(String requestTag) {
        if (requestTag.equals(uploadRide)) {
            if (progressDialog != null)
                progressDialog.dismiss();

            progressDialog = ProgressDialog.show(this, "Uploading data", "Please wait", true, false);
        }
    }

    @Override
    public void onRequestFailed(String requestTag, VolleyError error) {
        if (requestTag.equals(uploadRide)) {
            if (progressDialog != null)
                progressDialog.dismiss();

            makeToast("Unable to upload request. Check internet and try again");
        }
    }

    @Override
    public void onRequestCompleted(String requestTag, String response) {
        if (requestTag.equals(uploadRide)) {
            if (progressDialog != null)
                progressDialog.dismiss();

            if (alertDialog != null)
                alertDialog.dismiss();

            RideShareRequestObject obj = new Gson().fromJson(response, RideShareRequestObject.class);

            if (obj.status) {
                makeToast("Successfully uploaded data on server. Thankyou");
            } else {
                makeToast(obj.msg);
            }
        }
    }
}
