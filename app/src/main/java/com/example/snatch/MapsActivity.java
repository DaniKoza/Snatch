package com.example.snatch;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private MySharedPreferences msp;
    private GoogleMap mMap;
    private ListView recordsListView;
    private ArrayList<Record> scoreList;
    private ArrayAdapter<Record> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);



        recordsListView = findViewById(R.id.scoresListView);
        recordsListView.setOnItemClickListener(focusOnRecord);
        msp = new MySharedPreferences(this);
        mapFragment.getMapAsync(this);
        setArrayAdapter();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < scoreList.size(); i++) {
            LatLng recordLatLong = new LatLng(scoreList.get(i).getLatitude(), scoreList.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions().position(recordLatLong).title(scoreList.get(i).getName()));
            if (i == 0)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(recordLatLong, 10));
        }
    }

    /**
     * Tap on a record so the camera will focus on it
     */
    AdapterView.OnItemClickListener focusOnRecord = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Record r = (Record) parent.getAdapter().getItem(position);
            LatLng location = new LatLng(r.getLatitude(), r.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    };

    /**
     * Connect the RecordList to the listView
     */
    private void setArrayAdapter() {
        scoreList = msp.getArrayList("scoreList", "na");
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, scoreList);
        recordsListView.setAdapter(adapter);
    }


}
