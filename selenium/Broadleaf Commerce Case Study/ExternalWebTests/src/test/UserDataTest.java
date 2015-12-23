package test;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openqa.selenium.By;

import pages.LoginPage;
import pages.MainPage;
import test.enums.State;
import tools.DBUtils;
import tools.PageAddress;

public class UserDataTest extends ParentTest{

	/*
	 * @return id of user added or -1 if failed; Use that id with cleanUpUser to remove all related data after test
	 */
	protected long insertCustomer(String email, String password, String firstName, String lastName){
		long id = DBUtils.getNextMaxValue("PUBLIC.blc_customer", "CUSTOMER_ID");
		fConnectionInstance.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE USER_NAME='" + DBUtils.escapeString(email) + "';");

		int outcome = fConnectionInstance.tryUpdate("INSERT INTO BLC_CUSTOMER VALUES(" + id + "," + id
				+ ",'2014-08-20 11:22:49.263000',NULL,NULL,NULL,FALSE,'" + DBUtils.escapeString(email) + "','"
				+ DBUtils.escapeString(firstName) + "','" + DBUtils.escapeString(lastName) + "','" + DBUtils.escapeString(password)
				+ "',FALSE,NULL,TRUE,TRUE,NULL,'"
				+ DBUtils.escapeString(email) + "',NULL,NULL)");

		if(outcome == -1){
			return -1;
		}
		return id;
	}

	protected boolean insertAddress(long customerId, String line1, String line2, String city, String postal, String addrName, State state,
			String firstName, String lastName, String phoneNumber, boolean isDefault){

		long addrNumber = DBUtils.getNextMaxValue("BLC_CUSTOMER_ADDRESS", "CUSTOMER_ADDRESS_ID");
		long addressId = DBUtils.getNextMaxValue("BLC_ADDRESS", "ADDRESS_ID");
		long phoneId = DBUtils.getNextMaxValue("BLC_PHONE", "PHONE_ID");
		int outcome =
				fConnectionInstance.tryUpdate("INSERT INTO BLC_CUSTOMER_ADDRESS VALUES(" + addrNumber + ",'" + addrName + "'," + addressId + ","
						+ customerId + ");");

		String is_default = (isDefault ? "TRUE" : "FALSE");

		if(outcome != -1){
			outcome =
					fConnectionInstance.tryUpdate("INSERT INTO BLC_ADDRESS VALUES(" + addressId + ",'" + DBUtils.escapeString(line1) + "','"
							+ DBUtils.escapeString(line2) + "',NULL,'" + DBUtils.escapeString(city) + "',NULL,NULL,NULL,NULL,'"
							+ DBUtils.escapeString(firstName) + "',TRUE,FALSE," + is_default + ",'" + lastName + "','" + postal
							+ "',NULL,NULL,FALSE,NULL,NULL,NULL,'US',NULL," + phoneId + ",NULL,'" + state.name() + "');");
		}
		insertPhone(phoneId, true, isDefault, phoneNumber);

		if(outcome == -1){
			return false;
		}
		return true;
	}

	protected boolean insertPhone(long phoneId, boolean isActive, boolean isDefault, String phone){

		String is_default = (isDefault ? "TRUE" : "FALSE");
		String is_active = (isActive ? "TRUE" : "FALSE");

		int outcome = fConnectionInstance.tryUpdate("INSERT INTO BLC_PHONE VALUES(" + phoneId + "," + is_active + ","
				+ is_default + ",'" + DBUtils.escapeString(phone) + "');");

		if(outcome == -1){
			return false;
		}
		return true;
	}

	protected void cleanUpUserTable(){
		try{
			fConnectionInstance.tryUpdate("DELETE FROM PUBLIC.blc_customer;");
		} catch(Exception e){
			throw new Error("Database connection failed");
		}
	}
	
	protected void cleanUpUserTable(String email){
		try{
			fConnectionInstance.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE USER_NAME='" + DBUtils.escapeString(email) + "';");
		} catch(Exception e){
			throw new Error("Database connection failed");
		}
	}
	
	protected void cleanUpUserTableSafe(String email) {
		try{
			cleanUpUserTable(email);
		} catch(Throwable ex){
			System.out.println(ex.getMessage());
		}
	}	

	/*
	 * @param id id of user to be completely wiped from database, along with all
	 * correlated data. This method will be complemented with each added module;
	 */
	protected void cleanUpUser(long id){
		try{
			fConnectionInstance.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE CUSTOMER_ID=" + id + ";");
			ArrayList<Long> addrList = new ArrayList<>();
			fConnectionInstance.tryQuery("SELECT * FROM PUBLIC.blc_customer_address WHERE CUSTOMER_ID=" + id + ";");

			ResultSet rs = fConnectionInstance.fResult;

			while(rs.next()){
				addrList.add(rs.getLong(3));
			}
			fConnectionInstance.tryUpdate("DELETE FROM PUBLIC.blc_customer_address WHERE CUSTOMER_ID=" + id + ";");

			for(long l : addrList){
				fConnectionInstance.tryQuery("SELECT * FROM PUBLIC.blc_address WHERE ADDRESS_ID=" + l + ";");
				rs = fConnectionInstance.fResult;
				while(rs.next()){
					long phoneId = rs.getLong(24);
					fConnectionInstance.tryUpdate("DELETE FROM PUBLIC.blc_phone WHERE PHONE_ID=" + phoneId + ";");
				}
				fConnectionInstance.tryUpdate("DELETE FROM PUBLIC.blc_address WHERE ADDRESS_ID=" + l + ";");
			}

		} catch(Exception e){
			e.printStackTrace();
			throw new Error("Database connection failed");
		}
	}
	
	protected void cleanUpUserSafe(long id){
		try {
			cleanUpUser(id);
		}
		catch (Throwable ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void tryLogin(String email, String password) {
		fDriver.get(PageAddress.LOGIN);
		fDriver.findElement(By.name("j_username")).sendKeys(email);
		fDriver.findElement(By.name("j_password")).clear();
		fDriver.findElement(By.name("j_password")).sendKeys(password);
		fDriver.findElement(By.xpath("//input[@value='Login']")).click();		
	}

	protected void login(String email, String password){
		final int MAX_ATTEMPTS = 3;

		for (int cnt = 0; cnt < MAX_ATTEMPTS; cnt++) {
			tryLogin(email, password);

			if (isElementPresent(By.linkText("Logout"))) {
				break;
			}
		}
	}

	protected MainPage loginExpectSuccess(String email, String password, LoginPage loginPage){
		loginPage.fillUsername(email);
		loginPage.fillPassword(password);
		return loginPage.loginExpectSuccess();
	}

	protected LoginPage loginExpectFailure(String email, String password, LoginPage loginPage){
		loginPage.fillUsername(email);
		loginPage.fillPassword(password);
		return loginPage.loginExpectFailure();
	}

}
