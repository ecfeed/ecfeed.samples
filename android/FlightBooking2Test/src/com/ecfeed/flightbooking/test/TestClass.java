package com.ecfeed.flightbooking.test;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.ecfeed.flightbooking.MainActivity;
import com.ecfeed.flightbooking.R;
import com.ecfeed.flightbooking.test.ecfeed.android.EcFeedTest;

public class TestClass extends EcFeedTest {

	MainActivity fMainActivity;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		fMainActivity = getActivity();
	}

	public void testMethod() {
		android.util.Log.d("ecFeed", "testMethod()");
		testMethodWithParams("ATL", "JFK", "2016-06-25", "2016-06-25");
	}

	public void testMethodWithParams(String airportFrom, String airportTo, String flyOutDate, String returnDate) {
		setAirportFrom(airportFrom);
		setAirportTo(airportTo);
		checkReturnRadioButton();
		setFlyOutDate(flyOutDate);
		setReturnDate(returnDate);

		sleep(2000);          

		assertEquals(airportFrom, getAirportFrom());
		assertEquals(airportTo, getAirportTo());
		assertEquals(flyOutDate, getFlyOutDate());
		assertEquals(returnDate, getReturnDate());
		
		checkReturnRadioButtonChecked();
	}

	private void sleep(int milliseconds) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void setAirportFrom(String airport) {
		setAirport(airport, R.id.from_spinner);
	}

	private void setAirportTo(String airport) {
		setAirport(airport, R.id.to_spinner);
	}

	private void setAirport(String airport, int spinnerId) {
		AirportSetter airportFromSetter = new AirportSetter(airport, spinnerId);
		airportFromSetter.execute("");
	}

	private String getAirportFrom() {
		return getAirport(R.id.from_spinner);
	}

	private String getAirportTo() {
		return getAirport(R.id.to_spinner);
	}

	private String getAirport(int spinnerId) {
		Spinner mySpinner=(Spinner)fMainActivity.findViewById(spinnerId);
		return mySpinner.getSelectedItem().toString();
	}

	private class AirportSetter extends AsyncTask<String, Void, Void> {

		String fAirport;
		int fId;

		public AirportSetter(String airportFrom, int id) {
			super();
			fAirport = airportFrom;
			fId = id;
		}

		@Override
		protected Void doInBackground(String... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Spinner spinner = (Spinner)fMainActivity.findViewById(fId);

			if (spinner == null) {
				Log.d("ecFeed", "Invalid spinner id.");
				return;				
			}

			int index = fMainActivity.getAirportsAdapter().getPosition(fAirport);

			if (index == -1) {
				Log.d("ecFeed", "Can not select airport from as: " + fAirport);
				return;
			}

			spinner.setSelection(index);
		}

	}

	private void checkReturnRadioButton() {
		RadioButtonChecker checker = new RadioButtonChecker(R.id.radioReturn);
		checker.execute("");
	}
	
	private void checkReturnRadioButtonChecked() {
		RadioButton radioButton = (RadioButton)fMainActivity.findViewById(R.id.radioReturn);
		if (!radioButton.isChecked()) {
			fail("Radio button Return should be checked.");
		}
	}

	private class RadioButtonChecker extends AsyncTask<String, Void, Void> {

		int fId;

		public RadioButtonChecker(int id) {
			super();
			fId = id;
		}

		@Override
		protected Void doInBackground(String... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			RadioButton radioButton = (RadioButton)fMainActivity.findViewById(fId);
			radioButton.setChecked(true);
		}

	}    

	private void setFlyOutDate(String date) {
		DateSetter airportFromSetter = new DateSetter(date, R.id.editTextFlyOut);
		airportFromSetter.execute("");
	}

	private void setReturnDate(String date) {
		DateSetter airportFromSetter = new DateSetter(date, R.id.editTextReturn);
		airportFromSetter.execute("");
	}    

	private String getFlyOutDate() {
		EditText editText = (EditText)fMainActivity.findViewById(R.id.editTextFlyOut);
		return editText.getText().toString();
	}

	private String getReturnDate() {
		EditText editText = (EditText)fMainActivity.findViewById(R.id.editTextReturn);
		return editText.getText().toString();
	}    

	private class DateSetter extends AsyncTask<String, Void, Void> {

		String fDate;
		int fId;

		public DateSetter(String date, int id) {
			super();
			fDate = date;
			fId = id;
		}

		@Override
		protected Void doInBackground(String... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			EditText editText = (EditText)fMainActivity.findViewById(fId);
			editText.setText(fDate);
		}

	}    

}