package com.ecfeed.flightbooking;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class MainActivity extends Activity {
	
	ArrayAdapter<CharSequence> fAirportsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        createAirportsAdapter();
        setSpinnerAdapter(R.id.from_spinner);
        setSpinnerAdapter(R.id.to_spinner);
    }
    
    private void createAirportsAdapter() {
    	fAirportsAdapter = ArrayAdapter.createFromResource(this,
    	        R.array.airports_array, android.R.layout.simple_spinner_item);
    	
    	fAirportsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
    
    public ArrayAdapter<CharSequence> getAirportsAdapter() {
    	return fAirportsAdapter;
    }
    
    private void setSpinnerAdapter(int id) {
    	Spinner spinner = (Spinner) findViewById(id);
    	spinner.setAdapter(fAirportsAdapter);
    }
    
}
