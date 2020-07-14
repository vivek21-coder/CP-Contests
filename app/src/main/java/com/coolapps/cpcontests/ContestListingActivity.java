package com.coolapps.cpcontests;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.TimeZone;


public class ContestListingActivity extends AppCompatActivity {


    static final String format = "yyyy-MM-dd'T'HH:mm:ss";

    JSONObject jsonObject;

    private String curr_UTC;
    private String last_UTC;
    private int res_id;

    private static final HashSet<Integer> resources = new HashSet<>(Arrays.asList(1, 2, 93, 73, 35, 12, 102));

    private String[] array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_listing);

        array = new String[]{
                "/?username=" + R.string.username + "&api_key=" + R.string.api_key
        };

        Intent intent = getIntent();
        res_id = intent.getIntExtra("res", -1);

        curr_UTC = currDT();
        last_UTC = add1Month(curr_UTC);
        fetchObject();
        initialize_List();
    }

    private String add1Month(String str) {
        int ini_month = Integer.parseInt(str.substring(5, 7));
        if (ini_month == 12) {
            str = (Integer.parseInt(str.substring(0, 4)) + 1) + "-01" + str.substring(7);
        } else {
            ini_month++;
            str = str.substring(0, 5) + ((ini_month / 10 == 0) ? "0" + ini_month : ini_month) + str.substring(7);
        }

        int day = Integer.parseInt(str.substring(8, 10));
        if (day > 28) {
            str = str.substring(0, 8) + 28 + str.substring(10);
        }
        return str;
    }

    private void fetchObject() {
        JSONParseTask task = new JSONParseTask();
        String str;
        String key = array[new Random().nextInt(array.length)];
        if (res_id == -1) {
            str = "https://clist.by/api/v1/contest/" +
                    key +
                    "&limit=200&" +
                    "end__gt=" + curr_UTC +
                    "&end__lt=" + last_UTC +
                    "&order_by=start&format=json";
        } else {
            str = "https://clist.by/api/v1/contest/" +
                    key +
                    "&limit=200&" +
                    "resource__id=" + res_id +
                    "&end__gt=" + curr_UTC +
                    "&end__lt=" + last_UTC +
                    "&order_by=start&format=json";
        }
        try {
            jsonObject = task.execute(str, curr_UTC.substring(0, 16)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String currDT() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }

    /**
     * Converts UTC date time string to current timezone date and time
     *
     * @param str Date and Time string in UTC
     * @return Date and Time string in Local TimeZone
     */
    private String convert(String str) {
        @SuppressLint("SimpleDateFormat")
        DateFormat utcFormat = new SimpleDateFormat(format);
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date;
        try {
            date = utcFormat.parse(str);
        } catch (ParseException e) {
            return format;
        }

        @SuppressLint("SimpleDateFormat")
        DateFormat pstFormat = new SimpleDateFormat(format);
        pstFormat.setTimeZone(TimeZone.getDefault());

        assert date != null;
        return pstFormat.format(date);
    }

    // 0   1   2   3   4   5   6   7   8   9   10  11  12  13  14  15  16  17  18
    // 2   0   1   0   -   1   1   -   1   0   T   0   3   :   0   7   :   4   3
    // y   y   y   y   -   M   M   -   d   d   T   h   h   :   m   m   :   s   s
    private String prettyDT(String str) {
        String ans = "";
        ans += str.substring(8, 10) + " " +
                getStringMnth(Integer.parseInt(str.substring(5, 7))) + " " +
                getAMPMTime(str.substring(11, 16));
        return ans;
    }

    private String getAMPMTime(String str) {
        int hour = Integer.parseInt(str.substring(0, 2));

        if (hour > 12) {
            String nnn = hour - 12 + "";
            if (nnn.length() == 1) {
                nnn = "0" + nnn;
            }
            return nnn + str.substring(2) + "pm";
        }
        return str + "am";
    }

    private String getStringMnth(int n) {
        switch (n) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "Aug";
            case 9:
                return "Sept";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }
        return "";
    }

    private String secToDur(int secs) {
        int days = secs / 86400;
        secs %= 86400;
        int hours = secs / 3600;
        secs %= 3600;
        int mins = secs / 60;
        String ans = "";
        if (days != 0) {
            ans += days + ((days == 1) ? " day " : " days ");
        }
        if (hours != 0) {
            ans += hours + ((hours == 1) ? " hour " : " hours ");
        }
        if (mins != 0) {
            ans += mins + ((mins == 1) ? " minute " : " minutes ");
        }
        return ans;
    }

    private void initialize_List() {

        final ArrayList<JSONObject> objectArrayList = new ArrayList<>();

        try {

            JSONArray jsonArray = jsonObject.getJSONArray("objects");

            if (res_id == -1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (resources.contains(object.getJSONObject("resource").get("id"))
                            && isRated((String) object.get("event"), (Integer) object.getJSONObject("resource").get("id"))) {
                        objectArrayList.add(object);
                    }
                }
            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (resources.contains(object.getJSONObject("resource").get("id"))) {
                        objectArrayList.add(object);
                    }
                }
            }

        } catch (Exception e) {
            return;
        }

        int n = objectArrayList.size();
        if (n == 0) {
            findViewById(R.id.noContestView).setVisibility(View.VISIBLE);
            return;
        }

        headings = new String[n];
        Integer[] images = new Integer[n];
        starts = new String[n];
        ends = new String[n];
        String[] durations = new String[n];
        boolean[] live = new boolean[n];
        href = new String[n];

        for (int i = 0; i < n; i++) {
            try {
                headings[i] = objectArrayList.get(i).get("event").toString();
                images[i] = getImage((Integer) objectArrayList.get(i).getJSONObject("resource").get("id"));
                starts[i] = prettyDT(convert(objectArrayList.get(i).get("start").toString()));
                ends[i] = prettyDT(convert(objectArrayList.get(i).get("end").toString()));
                durations[i] = secToDur((Integer) objectArrayList.get(i).get("duration"));
                live[i] = isLive(objectArrayList.get(i).get("start").toString());
                href[i] = (String) objectArrayList.get(i).get("href");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List2Adapter adapter = new List2Adapter(this, headings, images, starts, ends, durations, live);
        ListView listView = findViewById(R.id.listViewContent);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent browserIntent;
                try {
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(href[i]));
                    ContestListingActivity.this.startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                String text = headings[i] + "\n" + starts[i] + "\nto\n" + ends[i] + "\n" + href[i];
                myClip = ClipData.newPlainText("text", text);
                clipboard.setPrimaryClip(myClip);
                Toast.makeText(ContestListingActivity.this, "Contest details copied to clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    ClipData myClip;
    ClipboardManager clipboard;

    private boolean isRated(String event, int res) {
        switch (res) {
            case 1:
                return event.contains("Codeforces Round");
            case 2:
                return event.contains("Cook-Off") || event.contains("Lunchtime") || event.contains("Challenge");
            case 73:
                return event.contains("Circuits") || event.contains("Easy") || event.contains("Data Structures and Algorithms");
            case 35:
                return false;
            case 12:
                return event.contains("SRM");
        }
        return true;
    }

    private boolean isLive(String start) {
        Timestamp startTS = Timestamp.valueOf(start.replace('T', ' '));
        Timestamp currTS = Timestamp.valueOf(curr_UTC.replace('T', ' '));

        return startTS.before(currTS);

    }

    private Integer getImage(Integer i) {
        switch (i) {
            case 1:
                return R.drawable.image1;
            case 2:
                return R.drawable.image2;
            case 93:
                return R.drawable.image93;
            case 73:
                return R.drawable.image73;
            case 35:
                return R.drawable.image35;
            case 12:
                return R.drawable.image12;
            case 102:
                return R.drawable.image102;
        }
        return R.drawable.ic_launcher_foreground;
    }

    private String[] headings;
    private String[] starts;
    private String[] ends;
    private String[] href;

}
