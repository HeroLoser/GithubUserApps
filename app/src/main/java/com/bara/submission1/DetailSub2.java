package com.bara.submission1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bara.submission1.adapter.RecyclerAdapter;
import com.bara.submission1.adapter.TabAdapter;
import com.bara.submission1.fragment.AccountFragment;
import com.bara.submission1.models.UserDetail;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DetailSub2 extends AppCompatActivity {

    TextView userName, follower, following, repository;
    ImageView avatar;
    TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sub2);

        userName = findViewById(R.id.NamaDetailSub2);
        follower = findViewById(R.id.countFollowersSub2);
        following = findViewById(R.id.countFollowingSub2);
        repository = findViewById(R.id.countRepoSub2);
        avatar = findViewById(R.id.img_avatar_detail_sub2);

        tabAdapter = new TabAdapter(this, getSupportFragmentManager());
        ViewPager viewPager2 = findViewById(R.id.viewPagerDetail);
        viewPager2.setAdapter(tabAdapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager2);

        if (getIntent() != null){
            String nameExtra = getIntent().getStringExtra("name");
            tabAdapter.getName(nameExtra);
            getDetail(nameExtra);
        }
    }

    public void backButton(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finishAffinity();
    }

    private void getDetail(String nama){
        String url = "https://api.github.com/users/" + nama;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "878ad115c123a3817f8034f04707fd7f2ab37461");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(result);

                    UserDetail userDetail = new UserDetail();
                    userDetail.setName(jsonObject.getString("name"));
                    userDetail.setAvatar(jsonObject.getString("avatar_url"));
                    userDetail.setRepository(jsonObject.getString("public_repos"));
                    userDetail.setFollowers(jsonObject.getString("followers"));
                    userDetail.setFollowing(jsonObject.getString("following"));
                    setComponent(userDetail);
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Failure", error.getMessage());
            }
        });
    }

    private void setComponent(UserDetail userDetail){
        userName.setText(userDetail.getName());
        follower.setText(userDetail.getFollowers());
        following.setText(userDetail.getFollowing());
        repository.setText(userDetail.getRepository());
        Glide.with(this)
                .load(userDetail.getAvatar())
                .into(avatar);
    }


}