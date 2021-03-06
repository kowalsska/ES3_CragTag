package com.example.magdakowalska.climbmap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class AddCragActivity extends ActionBarActivity {

    private String name;
    private String rocktype;
    private String faces;
    private Double latitude;
    private Double longitude;
    private String features;

    private TextView nameView;
    private TextView rocktypeView;
    private Spinner facesSpinner;
    private TextView featuresView;
    private Button confirmButton;

    private String jsonStringNewCrag;
    private SharedPreferences.Editor editor;

    public static MyApplication ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crag);

        //LOADING SHARED PREFERENCES
        Context c = ma.getInstance();
        SharedPreferences prefs = c.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        jsonStringNewCrag = prefs.getString("cragsStringFromJSON", null);
        editor = prefs.edit();
        editor.clear();

        //LOADING FIELDS FROM LAYOUT
        nameView = (TextView) findViewById(R.id.cragName);
        rocktypeView = (TextView) findViewById(R.id.rockType);
        facesSpinner = (Spinner) findViewById(R.id.facesDirectionSpinner);
        featuresView = (TextView) findViewById(R.id.featuresTextView);
        confirmButton = (Button) findViewById(R.id.buttonConfirmCrag);

        //POPULATING THE SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerFaces, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        facesSpinner.setAdapter(adapter);

        //GETTING CURRENT LOCATION (LATITUDE AND LONGITUDE)
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);
        latitude = myLocation.getLatitude();
        longitude = myLocation.getLongitude();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameView.getText().toString();
                rocktype = rocktypeView.getText().toString();
                faces = facesSpinner.getSelectedItem().toString();
                features = featuresView.getText().toString();

                String jsonStringUpdate =  ",{\n" +
                                    " \"name\": \"" + name + "\",\n" +
                                    " \"rocktype\": \"" + rocktype + "\",\n" +
                                    " \"faces\": \"" + faces + "\",\n" +
                                    " \"latitude\": \"" + latitude + "\",\n" +
                                    " \"longitude\": \"" + longitude + "\",\n" +
                                    " \"description\": \"" + features + "\",\n" +
                                    " \"climbs\": []\n}\n]\n}";

                int startIndex = jsonStringNewCrag.lastIndexOf("]");
                jsonStringNewCrag = jsonStringNewCrag.substring(0, startIndex - 1);

                jsonStringNewCrag += jsonStringUpdate;

                editor.putString("cragsStringFromJSON", jsonStringNewCrag);
                editor.apply();

                Toast.makeText(getApplicationContext(), "New crag added!", Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(i, 1);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_crag, menu);
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
