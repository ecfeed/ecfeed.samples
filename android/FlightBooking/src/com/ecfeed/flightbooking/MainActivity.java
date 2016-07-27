package com.ecfeed.flightbooking;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;


public class MainActivity extends Activity {

	private ArrayAdapter<CharSequence> fAirportsAdapter;
	private RadioGroup fRadioGroup;
	private LinearLayout fLayoutReturnDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		configureFromToSpinners();
		configureRadioGroupOneWayReturn();
		configureReturnDateLayout();
	}
	
	private void configureFromToSpinners() {
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
	
	private void configureRadioGroupOneWayReturn() {
		fRadioGroup = (RadioGroup) findViewById(R.id.radioGroupOneWayReturn);

		fRadioGroup.setOnCheckedChangeListener(
				new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.radioOneWay) {
							Log.d("ecFeed", "One way checked");
							setReturnDateLayoutVisibility(false);
						}
						if (checkedId == R.id.radioReturn) {
							Log.d("ecFeed", "Retrun checked");
							setReturnDateLayoutVisibility(true);
						}							
					}
				});
	}	
	
	private void configureReturnDateLayout() {
		fLayoutReturnDate = (LinearLayout)findViewById(R.id.linearLayoutReturnDate);
		setReturnDateLayoutVisibility(false);
	}

	private void setReturnDateLayoutVisibility(boolean isVisible) {
		if (isVisible) {
			fLayoutReturnDate.setVisibility(View.VISIBLE);
		} else {
			fLayoutReturnDate.setVisibility(View.GONE);
		}

	}

}
