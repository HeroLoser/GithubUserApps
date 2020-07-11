package com.bara.submission1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.bara.submission1.adapter.RecyclerAdapter;
import com.bara.submission1.models.Data;
import com.bara.submission1.models.Users;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private ProgressBar progressBar;
    RecyclerAdapter adapter;
//    private ArrayList<Data> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.searchViewMain);
        progressBar = findViewById(R.id.progressBarMain);

//        mData.add(new Data("Jake Wharton", "JakeWharton", "Google, Inc.", "Pittsburgh, PA, USA", "102", "56995", "12", R.drawable.user1));
//        mData.add(new Data("Amit Shekhar", "amitshekhariitbhu", "MindOrksOpenSource", "New Delhi, India", "37", "5153", "2", R.drawable.user2));
//        mData.add(new Data("Romain Guy", "romainguy", "Google", "California", "9", "7972", "0", R.drawable.user3));
//        mData.add(new Data("Chris Banes", "chrisbanes", "Google working on @android", "Sydney, Australia", "30", "14729", "1", R.drawable.user4));
//        mData.add(new Data("David", "tipsy", "Working Group Two", "Trondheim, Norway", "56", "788", "0", R.drawable.user5));
//        mData.add(new Data("Ravi Tamada", "ravi8x", "AndroidHive | Droid5", "India", "28", "18628", "3", R.drawable.user6));
//        mData.add(new Data("Deny Prasetyo", "jasoet", "gojek-engineering", "Kotagede, Yogyakarta, Indonesia", "44", "277", "39", R.drawable.user7));
//        mData.add(new Data("Budi Oktaviyan", "budioktaviyan", "KotlinID", "Jakarta, Indonesia", "110", "178", "23", R.drawable.user8));
//        mData.add(new Data("Hendi Santika", "hendisantika", "JVMDeveloperID @KotlinID @IDDevOps", "Bojongsoang - Bandung Jawa Bara", "1064", "428", "61", R.drawable.user9));
//        mData.add(new Data("Sidiq Permana", "sidiqpermana", "Nusantara Beta Studio", "Jakarta Indonesia", "65", "465", "10", R.drawable.user10));

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new RecyclerAdapter();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        loading(false);
        searching();
    }

    public void loading(Boolean bool){
        if (bool){
            progressBar.setVisibility(View.VISIBLE);
        }
        else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void searching(){
        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        if (searchManager != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (query != null){
                        loading(true);
                        setUsersSearch(query);
                    }return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    private void setUsersSearch(final String name){
        final ArrayList<Users> listUser= new ArrayList<>();

        String url = "https://api.github.com/search/users?q=" + name;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "878ad115c123a3817f8034f04707fd7f2ab37461");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject reJsonObject = new JSONObject(result);
                    JSONArray list = reJsonObject.getJSONArray("items");

                    for (int i = 0; i<list.length(); i++){
                        JSONObject user = list.getJSONObject(i);
                        Users usersNew = new Users();
                        usersNew.setName(user.getString("login"));
                        usersNew.setType(user.getString("type"));
                        usersNew.setAvatar(user.getString("avatar_url"));
                        listUser.add(usersNew);
                    }
                    adapter.setmData(listUser);
                    loading(false);

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                    Toast.makeText(MainActivity.this, "eror", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Error", error.getMessage());
                Toast.makeText(MainActivity.this, "gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
