package com.javaeditor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView noCodeImgView;
    private TextView noCodeTextView;

    private ArrayList<UserCode> userCodes;
    private UserCodeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noCodeImgView = findViewById(R.id.empty_code_image);
        noCodeTextView = findViewById(R.id.empty_code_text);

        initToolbar();
        initRecyclerView();
        goToCodeEditorActivity();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.main_activity_toolbar);

        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> super.onBackPressed());

        toolbar.inflateMenu(R.menu.main_activity_menu);
        toolbar.setOnMenuItemClickListener(this::performSearch);
    }

    private boolean performSearch(MenuItem item) {
        if (userCodes != null) {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_search) {
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setQueryHint(Html.fromHtml("<font color = #818795><small>" + getResources().getString(R.string.search) + "</small></font>"));
                searchView.setIconifiedByDefault(false);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filter(newText);
                        return false;
                    }
                });
            }
        } else {
            Toast.makeText(this, "You have no codes!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView_user_codes);

        CodeDatabase db = new CodeDatabase(this);
        DbHelper dbHelper = new DbHelper(db);

        if (dbHelper.isEmptyDb()) {
            noCodeImgView.setVisibility(View.VISIBLE);
            noCodeTextView.setVisibility(View.VISIBLE);
        } else {
            noCodeImgView.setVisibility(View.GONE);
            noCodeTextView.setVisibility(View.GONE);

            userCodes = dbHelper.getUserCodes();
            adapter = new UserCodeAdapter(this, this, userCodes);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void goToCodeEditorActivity() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, CodeEditorActivity.class));
            finish();
        });
    }

    // filters the search query
    private void filter(String codeTitle) {
        ArrayList<UserCode> filteredList = new ArrayList<>();
        for (UserCode title : userCodes) {
            if (title.getTitle().toLowerCase().trim().contains(codeTitle.toLowerCase().trim())) {
                filteredList.add(title);
            }
        }
        adapter.setFilter(filteredList);
    }

}