package com.dynamicconsole.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.dynamicconsole.model.ConsoleDetails;
import com.dynamicconsole.service.ConsoleService;

@Service
public class ConsoleServiceImpl implements ConsoleService {

	public List getAppDetails() throws ClassNotFoundException {
		List appList = new ArrayList<>();
		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/consoleAppDb","root","Thiru6497@");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("Select * from application_details");
			while(rs.next()){
				ConsoleDetails consoleDetail = new ConsoleDetails();
				consoleDetail.setId(rs.getInt(1));
				consoleDetail.setApplicationName(rs.getString(2));
				consoleDetail.setClassName(rs.getString(3));
				consoleDetail.setMethodName(rs.getString(4));
				consoleDetail.setParameter(rs.getString(5));
				consoleDetail.setComments(rs.getString(6));
				consoleDetail.setDeployPath(rs.getString(7));
				appList.add(consoleDetail);
			}  
				conn.close();  
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return appList;
	}

	@Override
	public Map<String, Object> insertAppDetails(Map appDetailMap) throws ClassNotFoundException {
		Map<String,Object> returnMap = new HashMap<>();
		returnMap.put("status", "pass");
		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/consoleAppDb","root","Thiru6497@");
			Statement stmt = conn.createStatement();
			String application_name = (String) appDetailMap.get("applicationName");
			String class_name = (String) appDetailMap.get("className");
			String method_name = (String) appDetailMap.get("methodName").toString();
			String parameters = (String) appDetailMap.get("parameters");
			String deploy_path = (String) appDetailMap.get("deployPath");
			String comments = (String) appDetailMap.get("comments");
			int rs = stmt.executeUpdate("insert into application_details(application_name,class_name,method_name,parameters,deploy_path,comments) "
					+ "values('"+application_name+"','"+class_name+"','"+method_name+"','"+parameters+"','"+deploy_path+"','"+comments+"');");
		}catch(SQLException e){
			returnMap.put("status", "fail");
			e.printStackTrace();
		}
		return returnMap;
	}

	@Override
	public Map<String, Object> updateAppDetails(Map appDetailMap) throws ClassNotFoundException {
		Map<String,Object> returnMap = new HashMap<>();
		returnMap.put("status", "pass");
		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/consoleAppDb","root","Thiru6497@");
			Statement stmt = conn.createStatement();
			int appId = (int) Float.parseFloat(appDetailMap.get("applicationId").toString());
			String application_name = (String) appDetailMap.get("applicationName");
			String class_name = (String) appDetailMap.get("className");
			String method_name = (String) appDetailMap.get("methodName").toString();
			String parameters = (String) appDetailMap.get("parameters");
			String deploy_path = (String) appDetailMap.get("deployPath");
			String comments = (String) appDetailMap.get("comments");
			int rs = stmt.executeUpdate("update application_details set application_name='"+application_name+"',class_name='"+class_name+"',"
					+ "method_name='"+method_name+"',parameters='"+parameters+"',deploy_path='"+deploy_path+"',comments='"+comments+"' where application_id='"+appId+"';");
		}catch(SQLException e){
			returnMap.put("status", "fail");
			e.printStackTrace();
		}
		return returnMap;
	}

	@Override
	public Map<String, Object> deleteAppDetails(Map appDetailMap) throws ClassNotFoundException {
		Map<String,Object> returnMap = new HashMap<>();
		returnMap.put("status", "pass");
		try {
			Class.forName("com.mysql.jdbc.Driver"); 
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/consoleAppDb","root","Thiru6497@");
			Statement stmt = conn.createStatement();
			int appId = (int) Float.parseFloat(appDetailMap.get("applicationId").toString());
			int rs = stmt.executeUpdate("delete from application_details where application_id='"+appId+"';");
		}catch(SQLException e){
			returnMap.put("status", "fail");
			e.printStackTrace();
		}
		return returnMap;
	}

}