package com.ecfeed.flightbooking;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends Activity {
	
	ArrayAdapter<CharSequence> fAirportsAdapter;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        createAirportsAdapter();
        populateAirportFromSpinner();

        final EditText value1 = (EditText) findViewById(R.id.val1);
        final EditText value2 = (EditText) findViewById(R.id.val2);
        final EditText value3 = (EditText) findViewById(R.id.val3);
        final EditText value4 = (EditText) findViewById(R.id.val4);

        final EditText result = (EditText) findViewById(R.id.result);
        final EditText comment = (EditText) findViewById(R.id.comment);

        Button multiplyButton = (Button)findViewById(R.id.multiplyButton);

        if(multiplyButton != null)
        {
        	multiplyButton.setOnClickListener(new OnClickListener(){

        		@Override
        		public void onClick(View arg0) {
        			if((value1.getText().length() == 0) || (value2.getText().length() == 0) || 
        					(value3.getText().length() == 0) || (value4.getText().length() == 0)){
        				result.setText("ERROR");
        				comment.setText("No input can stay blank");
        			}
        			else{
        				try{
        					int val1 = Integer.parseInt(value1.getText().toString());
        					int val2 = Integer.parseInt(value2.getText().toString());
        					int val3 = Integer.parseInt(value3.getText().toString());
        					int val4 = Integer.parseInt(value4.getText().toString());

        					int res = val1 * val2 * val3 * val4;

        					result.setText(String.valueOf(res));
        					comment.setText("");

        				}catch(NumberFormatException e){
        					result.setText("ERROR");
        					comment.setText("Wrong input format");
        				}
        			}
        		}
        	});
        }
    }
    
    private void createAirportsAdapter() {
    	fAirportsAdapter = ArrayAdapter.createFromResource(this,
    	        R.array.airports_array, android.R.layout.simple_spinner_item);
    	
    	fAirportsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
    
    private void populateAirportFromSpinner() {
    	Spinner spinner = (Spinner) findViewById(R.id.from_spinner);
    	spinner.setAdapter(fAirportsAdapter);
    }
    
    public void setAirportFrom(String airportFrom) {
    	AirportFromSetter airportFromSetter = new AirportFromSetter(airportFrom);
    	airportFromSetter.execute("");
    }

    public String getAirportFrom() {
    	Spinner mySpinner=(Spinner) findViewById(R.id.from_spinner);
    	return mySpinner.getSelectedItem().toString();
    }

    
    private class AirportFromSetter extends AsyncTask<String, Void, Void> {

    	String fAirportFrom;
    	
    	public AirportFromSetter(String airportFrom) {
    		super();
    		fAirportFrom = airportFrom;
    	}
    	
		@Override
		protected Void doInBackground(String... params) {
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
	    	Spinner spinner = (Spinner) findViewById(R.id.from_spinner);
	    	
	    	int index = fAirportsAdapter.getPosition(fAirportFrom);
	    	
	    	if (index == -1) {
	    		Log.d("ecFeed", "Can not select airport from as: " + fAirportFrom);
	    		return;
	    	}
	    	
	    	spinner.setSelection(index);
		}
    	
    }
    
}
