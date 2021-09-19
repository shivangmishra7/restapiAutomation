package com.createNeOrder.test;
import com.restAssured.auth.*;

import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.restAssured.auth.GetSecurityToken;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class TestSuiteApi  {
	
	GetSecurityToken gst = new GetSecurityToken();
	String password,userName,authUrl,createOrderUrl; 
	
	@BeforeMethod
	public void setup() throws IOException {
		Properties prop = new Properties();
		String userdir = System.getProperty("user.dir");
		
		//please update the username and password in testdata.properties file in resources
		FileInputStream instream= new FileInputStream(userdir+"\\src\\test\\resources\\testdata.properties");
		prop.load(instream);
		
		userName= prop.getProperty("userame");
		password= prop.getProperty("password");
		authUrl = prop.getProperty("authApiUrl");
		createOrderUrl = prop.getProperty("createOrderApiUrl");
		//RestAssured.authentication = RestAssured.preemptive().basic(userName, password);
		
		
		int code = RestAssured.given().
				post(authUrl)
				.getStatusCode();
		Response response = RestAssured.given().post(authUrl)
				.then().contentType(ContentType.JSON).extract().response();
		String token=response.jsonPath().getString("token");
		
		gst.setToken(token);

		/*
		 * System.out.println("Status Code ->" + code);
		 * System.out.println("Response:\n"+gst.getToken());
		 */
		
	}

	@Test(description="Check Url Access",priority=1)
	public void test3(){
		
		
		String code = String.valueOf(RestAssured.given()
				.post(createOrderUrl)
				.getStatusCode());
		String sts = RestAssured.given()
				.post(createOrderUrl)
				.getStatusLine();
		String codeS= code.substring(0, 1);
		assertEquals(codeS, "2", sts);
		
		
	}
	
	@Test(description="Create Order Check -- Status Code",priority=2)
	public void test2(){
		
		//Change the string s with error to get proper responses
		String s="{\r\n"
				+ "    \"automation\": true,\r\n"
				+ "    \"orders\": [\r\n"
				+ "        {\r\n"
				+ "            \"channel_id\": \"manual\",\r\n"
				+ "            \"channel_order_id\": \"-BG2dAmouX25\",\r\n"
				+ "            \"order_amount\": 100,\r\n"
				+ "            \"paymentType\": \"COD\",\r\n"
				+ "            \"billing_address\": {\r\n"
				+ "                \"city\": \"New York\",\r\n"
				+ "                \"name\": \"Yash Sharma\",\r\n"
				+ "                \"email\": \"saurabhsinha209@gmail.com\",\r\n"
				+ "                \"msisdn\": \"8377086507\",\r\n"
				+ "                \"country\": \"India\",\r\n"
				+ "                \"pinCode\": 141006,\r\n"
				+ "                \"address1\": \"Baker Street\",\r\n"
				+ "                \"address2\": \" \",\r\n"
				+ "                \"province\": \"Uttar Pradesh\",\r\n"
				+ "                \"last_name\": \"Sharma\",\r\n"
				+ "                \"first_name\": \"Yash\"\r\n"
				+ "            },\r\n"
				+ "            \"shipping_address\": {\r\n"
				+ "                \"city\": \"New York\",\r\n"
				+ "                \"name\": \"Yash Sharma\",\r\n"
				+ "                \"email\": \"yash@growsimplee.com\",\r\n"
				+ "                \"msisdn\": \"8377086507\",\r\n"
				+ "                \"country\": \"India\",\r\n"
				+ "                \"pinCode\": 141006,\r\n"
				+ "                \"address1\": \"Baker Street\",\r\n"
				+ "                \"address2\": \" \",\r\n"
				+ "                \"province\": \"Uttar Pradesh\",\r\n"
				+ "                \"last_name\": \"Sharma\",\r\n"
				+ "                \"first_name\": \"Yash\"\r\n"
				+ "            },\r\n"
				+ "            \"suborders\": [\r\n"
				+ "                {\r\n"
				+ "                    \"channel_listing_id\": \"CH1712\",\r\n"
				+ "                    \"length\" : 10,\r\n"
				+ "                    \"breadth\" : 20,\r\n"
				+ "                    \"height\" : 90,\r\n"
				+ "                    \"dead_weight\" : 12,\r\n"
				+ "                    \"channel_suborder_id\": \"Abc\",\r\n"
				+ "                    \"quantity\" :1,\r\n"
				+ "                    \"codAmount\": \"10\",\r\n"
				+ "                    \"suborder_value\":\"200\",\r\n"
				+ "                    \"items\" : [\r\n"
				+ "                        {\r\n"
				+ "                            \"name\":\"SONY XB10\",\r\n"
				+ "                            \"description\" : \"Mini Speaker\",\r\n"
				+ "                            \"quantity\": 3,\r\n"
				+ "                            \"item_id\": \"123456\",\r\n"
				+ "                            \"item_value\" : \"10.00\"\r\n"
				+ "                        }\r\n"
				+ "                    ]\r\n"
				+ "                }\r\n"
				+ "            ]\r\n"
				+ "        }\r\n"
				+ "    ]\r\n"
				+ "}"
				;
				
		
		
		//System.out.println(gst.getToken());
		
		int code = RestAssured.given()
				.header("Authorization",gst.getToken())
				.body(s)
				.post(createOrderUrl)
				.getStatusCode();
		String sts = RestAssured.given()
				.header("Authorization",gst.getToken())
				.body(s)
				.post(createOrderUrl)
				.getStatusLine();
		Response response = RestAssured.given().post("https://3hatbykz3i.execute-api.ap-south-1.amazonaws.com/v1/orin/api/orders/")
				.then().contentType(ContentType.JSON).extract().response();

		HashMap<String, String> errorCodes= new HashMap<String, String>();
		errorCodes.put("41001","Invalid shipping_address: Pincode");
		errorCodes.put("41002","Invalid shipping_address: Phone Number");
		errorCodes.put("41003","Invalid shipping_address: Address1");
		errorCodes.put("41004","Invalid suborders: channel_listing_id");
		errorCodes.put("41005","Invalid suborders: channel_suborder_id");
		errorCodes.put("41006","Invalid suborders: quantity");
		errorCodes.put("41008","Invalid suborders: cost");
		errorCodes.put("41009","Invalid channel_id");
		errorCodes.put("41010","Invalid channel_order_id");
		errorCodes.put("41011","Invalid Order_amount");
		errorCodes.put("41012","Invalid Payment Type");
		errorCodes.put("41013","Invalid suborders: codAmount");
		errorCodes.put("21013","Order Processed");
		errorCodes.put("21014","SubOrder Processed");
		
		String status = response.jsonPath().getString("status");
		
		assertEquals(status, "success", "test case failed as the error status mentioned:"+sts);
		Response innerResponse = response.jsonPath().getJsonObject("response");
		String error= innerResponse.jsonPath().getString("error");
		String code1= innerResponse.jsonPath().getString("code");
		String errorResponse;
		if(status!=null)
		{
			 errorResponse = errorCodes.get(code1);	
			 assertEquals(errorResponse, error);
		}
		else
		{
			System.out.println("API Error Code :"+code+ "\n Status Message: "+sts );
		}
		
		
	}
	
}
