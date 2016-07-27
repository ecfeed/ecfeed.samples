package com.ecfeed.flightbooking;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;


public class MainActivity extends Activity {

	private ArrayAdapter<CharSequence> fAirportsAdapter;
	private RadioGroup fRadioGroup;
	private EditText fFlyOutDate;
	private EditText fReturnDate;
	private LinearLayout fLayoutReturnDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		configureFromToSpinners();
		configureRadioGroupOneWayReturn();
		configureFlyOutReturnDates();
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
							setReturnDateLayoutVisibility(false);
						}
						if (checkedId == R.id.radioReturn) {
							setReturnDateLayoutVisibility(true);
						}							
					}
				});
	}	

	private void configureFlyOutReturnDates() {
		configureFlyOutDate();
		configureReturnDate();
	}

	private void configureFlyOutDate() {
		fFlyOutDate = (EditText)findViewById(R.id.editTextFlyOut);
		fFlyOutDate.setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						onClickDateField(fFlyOutDate);
					}
				});
	}

	private void configureReturnDate() {
		fReturnDate = (EditText)findViewById(R.id.editTextReturn);
		fReturnDate.setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						onClickDateField(fReturnDate);
					}
				});
	}
	
	private void onClickDateField(final EditText editText) {
		Calendar newCalendar = Calendar.getInstance();
		DatePickerDialog flyOutDatePickerDialog = 
				new DatePickerDialog(
						this, 
						new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
								setDateInField(editText, year, monthOfYear, dayOfMonth);
							}
						},
						newCalendar.get(Calendar.YEAR), 
						newCalendar.get(Calendar.MONTH), 
						newCalendar.get(Calendar.DAY_OF_MONTH));

		flyOutDatePickerDialog.show();
	}

	private void setDateInField(EditText editText, int year, int monthOfYear, int dayOfMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, monthOfYear, dayOfMonth);
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		editText.setText(dateFormatter.format(calendar.getTime()));
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
