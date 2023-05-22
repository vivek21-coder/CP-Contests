package com.coolapps.cpcontests;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Integer[] res_ID;

    TextView frame;
    private boolean clickable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickable = false;

        frame = findViewById(R.id.blackView);
        res_ID = new Integer[]{
                1,
                2,
                93,
//                73,
                35,
//                12,
                102
        };

        Integer[] imageIDArray = {
                R.drawable.image1,
                R.drawable.image2,
                R.drawable.image93,
//                R.drawable.image73,
                R.drawable.image35,
//                R.drawable.image12,
                R.drawable.image102
        };

        String[] names = {
                "Codeforces",
                "CodeChef",
                "AtCoder",
//                "HackerEarth",
                "Google",
//                "TopCoder",
                "LeetCode"
        };


        List1Adapter adapter = new List1Adapter(this, names, imageIDArray);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (clickable) {
                    MainActivity.this.enableSpinner();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this.getApplicationContext(), ContestListingActivity.class);
                            intent.putExtra("res", res_ID[position]);
                            MainActivity.this.startActivityForResult(intent, 1401);
                        }
                    }, 100);
                }
            }
        });

    }

    private void enableSpinner() {
        frame.setVisibility(View.VISIBLE);
        frame.animate().alpha(1).setDuration(100);
    }

    public void onClick(final View view) {
        enableSpinner();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this.getApplicationContext(), ContestListingActivity.class);
                intent.putExtra("res", -1);
                MainActivity.this.startActivityForResult(intent, 1401);
            }
        }, 100);
    }

    public void enable(final View view) {
        clickable = true;
        findViewById(R.id.listView).setTranslationY(50);
        view.animate().alpha(0).translationY(50).setDuration(300);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.GONE);
                MainActivity.this.findViewById(R.id.listView).animate().translationY(0).alpha(1).setDuration(300);
            }
        }, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1401) {
            disableSpinner();
        }
    }

    private void disableSpinner() {
        frame.setAlpha(0);
        frame.setVisibility(View.GONE);
    }
}
