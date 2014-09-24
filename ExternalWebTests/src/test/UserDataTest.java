package test;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openqa.selenium.By;

import test.enums.State;
import tools.DBUtils;
import tools.PageAddress;

public class UserDataTest extends ParentTest{

	/*
	 * @return id of user added or -1 if failed; Use that id with cleanUpUser to remove all related data after test
	 */
	protected long insertCustomer(String email, String password, String firstName, String lastName){
		long id = DBUtils.getNextMaxValue("PUBLIC.blc_customer", "CUSTOMER_ID");
		connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE USER_NAME='" + DBUtils.escapeString(email) + "';");
		int outcome = connection.tryUpdate("INSERT INTO BLC_CUSTOMER VALUES(" + id + "," + id
						+ ",'2014-08-20 11:22:49.263000',NULL,NULL,NULL,FALSE,'" + DBUtils.escapeString(email) + "','"
						+ DBUtils.escapeString(firstName) + "','" + DBUtils.escapeString(lastName) + "','" + DBUtils.escapeString(password)
						+ "{" + id + "}',FALSE,NULL,TRUE,TRUE,NULL,'" + DBUtils.escapeString(email) + "',NULL,NULL)");

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
				connection.tryUpdate("INSERT INTO BLC_CUSTOMER_ADDRESS VALUES(" + addrNumber + ",'" + addrName + "'," + addressId + ","
						+ customerId + ");");

		String is_default = (isDefault ? "TRUE" : "FALSE");

		if(outcome != -1){
			outcome =
					connection.tryUpdate("INSERT INTO BLC_ADDRESS VALUES(" + addressId + ",'" + DBUtils.escapeString(line1) + "','"
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

		int outcome = connection.tryUpdate("INSERT INTO BLC_PHONE VALUES(" + phoneId + "," + is_active + ","
						+ is_default + ",'" + DBUtils.escapeString(phone) + "');");

		if(outcome == -1){
			return false;
		}
		return true;
	}

	protected void cleanUpUserTable(String email){
		try{
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE USER_NAME='" + DBUtils.escapeString(email) + "';");
		} catch(Exception e){
			throw new Error("Database connection failed");
		}
	}

	/*
	 * @param id id of user to be completely wiped from database, along with all
	 * correlated data. This method will be complemented with each added module;
	 */
	protected void cleanUpUser(long id){
		try{
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE CUSTOMER_ID=" + id + ";");
			ArrayList<Long> addrList = new ArrayList<>();
			connection.tryQuery("SELECT * FROM PUBLIC.blc_customer_address WHERE CUSTOMER_ID=" + id + ";");

			ResultSet rs = connection.result;

			while(rs.next()){
				addrList.add(rs.getLong(3));
			}
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer_address WHERE CUSTOMER_ID=" + id + ";");

			for(long l : addrList){
				connection.tryQuery("SELECT * FROM PUBLIC.blc_address WHERE ADDRESS_ID=" + l + ";");
				rs = connection.result;
				while(rs.next()){
					long phoneId = rs.getLong(24);
					connection.tryUpdate("DELETE FROM PUBLIC.blc_phone WHERE PHONE_ID=" + phoneId + ";");
				}
				connection.tryUpdate("DELETE FROM PUBLIC.blc_address WHERE ADDRESS_ID=" + l + ";");
			}

		} catch(Exception e){
			e.printStackTrace();
			throw new Error("Database connection failed");
		}
	}

	protected void login(String email, String password){
		driver.get(PageAddress.Login);
		driver.findElement(By.name("j_username")).sendKeys(email);
		driver.findElement(By.name("j_password")).clear();
		driver.findElement(By.name("j_password")).sendKeys(password);
		driver.findElement(By.xpath("//input[@value='Login']")).click();
	}

}
