package com.example.magdakowalska.climbmap;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;


public class ClimbListActivity extends ListActivity {

    public MyApplication ma;
    private Locations locations = new Locations();
    public ArrayList<HashMap<String, Object>> climbsList = new ArrayList<HashMap<String, Object>>();
    public int cragIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_climb_list);

        Context c = ma.getInstance();

        SharedPreferences prefs = c.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String jsonStringCrags = prefs.getString("cragsStringFromJSON", null);

        locations.jsonToArraylist(jsonStringCrags);
        //defaultCragLocationList = locations.getDefaultCragLocationList();

        Intent intent = this.getIntent();
        if(intent != null) {
            cragIndex = intent.getExtras().getInt("cragIndex");
        }

        climbsList = (ArrayList)locations.getDefaultCragLocationList().get(cragIndex).get("climbs");

        ArrayList<HashMap<String, Object>> climbsListData = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < climbsList.size(); i++) {
            HashMap<String, Object> climb = climbsList.get(i);
            System.out.println("A SINGLE CLIMB : " + climb);
            climbsListData.add(climb);
        }

        //System.out.println(climbsListData.toString());

        // Adding menuItems to ListView
        ListAdapter adapter = new SimpleAdapter(this, climbsListData,
                R.layout.row_climb_layout, new String[] { "name" }, new int[] {R.id.singleClimbRow });

        setListAdapter(adapter);

        // Selecting single ListView item
        ListView lv = getListView();
        // Listening to single item click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Getting item index
                int climbIndex = position;
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), ShowClimbActivity.class);
                // Sending songIndex to PlayerActivity
                in.putExtra("cragIndex", cragIndex);
                in.putExtra("climbIndex", climbIndex);
                startActivity(in);
                // Closing PlayListView
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_climb_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
