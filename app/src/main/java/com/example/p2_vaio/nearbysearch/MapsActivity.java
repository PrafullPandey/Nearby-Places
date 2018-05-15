package com.example.p2_vaio.nearbysearch;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SearchView searchView;
    private static final String TAG = "MapsActivity";
    String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=%s&key=%s";
    public ArrayList<Modal_LatLng> location = new ArrayList<>();
    public Modal_LatLng modal_latLng ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        activateToolBar(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)menu.findItem(R.id.app_bar_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "onQueryTextSubmit: "+s);
                String formated_url = String.format(url,s,getResources().getString(R.string.google_key));
                formated_url = formated_url.replaceAll(" ","+");
                Log.d(TAG, "onQueryTextSubmit: "+formated_url);
                searchView.clearFocus();
                GetData getData = new GetData();
                getData.execute(formated_url);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.app_bar_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public class GetData extends AsyncTask<String , Void , Void>{

        @Override
        protected Void doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: ");
            URL url ;
            HttpURLConnection httpURLConnection =null;
            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    total.append(line);
                }
                Log.d(TAG, "doInBackground: "+total);
                parseJSONForLocation(total.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        private void parseJSONForLocation(String s) throws JSONException {

            JSONObject data = new JSONObject(s);
            JSONArray results = data.getJSONArray("results");
            for(int i=0 ; i<results.length();i++){
                try{
                    JSONObject index = results.getJSONObject(i);

                    Double lat = index.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    Double lng = index.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    String name = index.getString("name");
                    modal_latLng = new Modal_LatLng();
                        modal_latLng.setLatitude(lat);
                        modal_latLng.setLongitude(lng);
                        modal_latLng.setName(name);
                        location.add(modal_latLng);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "parseJSONForLocation: "+location.toString());
        }
    }
}
