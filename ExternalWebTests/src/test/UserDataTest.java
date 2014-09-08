package test;

import org.openqa.selenium.By;

import tools.DBUtils;
import tools.PageAddress;

public class UserDataTest extends ParentTest{

	protected boolean insertCustomer(int id, String email, String password, String firstName, String lastName){
		connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE CUSTOMER_ID="+id+";");
		connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE USER_NAME='"+DBUtils.escapeString(email)+"';");
		int outcome = connection.tryUpdate("INSERT INTO BLC_CUSTOMER VALUES("+id+","+id+",'2014-08-20 11:22:49.263000',NULL,NULL,NULL,FALSE,'"+
				DBUtils.escapeString(email) +"','" + DBUtils.escapeString(firstName)+"','"+DBUtils.escapeString(lastName)+"','" +
				DBUtils.escapeString(password) +"{"+id+"}',FALSE,NULL,TRUE,TRUE,NULL,'" + DBUtils.escapeString(email) + "',NULL,NULL)");
		
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
	 * @param id id of user to be completely wiped from database, along with all correlated data. This method will be complemented with each added module;
	 */
	protected void cleanUpUser(int id){
		try{
			connection.tryUpdate("DELETE FROM PUBLIC.blc_customer WHERE USER_ID=" + id + ";");
		} catch(Exception e){
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
