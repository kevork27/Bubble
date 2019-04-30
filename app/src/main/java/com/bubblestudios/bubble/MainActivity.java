package com.bubblestudios.bubble;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements CardsFragment.OnFragmentInteractionListener, UserProfileFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ImageView profileIcon, logo;
    private Uri photoUrl;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instantiate toolbar, disable title
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar = getSupportActionBar();
        //enable toolbar click listener for up arrow to change viewpager pages
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        //get current user object from firebase
        user = FirebaseAuth.getInstance().getCurrentUser();

        //set user profile icon in toolbar
        photoUrl = user.getPhotoUrl();
        profileIcon = findViewById(R.id.profile_icon);
        logo = findViewById(R.id.bubble_logo);
        Glide.with(profileIcon).load(photoUrl).into(profileIcon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });

        //create viewpager to load both cardsfragment and userprofilefragment
        viewPager = findViewById(R.id.main_viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        //listen for viewpager page changes
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch(i) {
                    case 0:
                        //if on first page, disable toolbar up arrow and make profile icon and logo visible
                        actionBar.setDisplayHomeAsUpEnabled(false);
                        profileIcon.setVisibility(View.VISIBLE);
                        logo.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        //if on second page, enable up/back arrow and disable profile icon and logo in toolbar
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        profileIcon.setVisibility(View.GONE);
                        logo.setVisibility(View.GONE);
                        break;
                }
                //reset menu items on page change
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        //get viewpager page number
        int pageNum = (viewPager.getCurrentItem());
        //if on second page, enable search button, else disable
        if ( pageNum== 1) {
            menu.findItem(R.id.menu_search).setVisible(true);

        } else {
            menu.findItem(R.id.menu_search).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_settings_button:
                //settings menu - nothing here yet
                return true;
            case R.id.menu_upload_snippet_button:
                //launch upload snippet activity
                Intent intent = new Intent(this, UploadSnippetActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_upload_artist_button:
                //launch upload artist activity
                Intent intent2 = new Intent(this, UploadArtistActivity.class);
                startActivity(intent2);
                return true;
            case R.id.menu_logout_button:
                //logout
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });
                return true;
            case R.id.menu_clear_likes:
                //clear out all likes for user - just for testing purposes, would not be in a release version
                FirebaseFirestore.getInstance().collection("snippets").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                                documentSnapshot.getReference().update("liked_users", FieldValue.arrayRemove(user.getUid()));
                                documentSnapshot.getReference().update("disliked_users", FieldValue.arrayRemove(user.getUid()));
                            }
                        }
                    }
                });
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //custom pager adapter to initialize fragments
    public static class PagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return CardsFragment.newInstance();
                case 1:
                    return UserProfileFragment.newInstance();
                default:
                    return null;
            }
        }
    }


}
