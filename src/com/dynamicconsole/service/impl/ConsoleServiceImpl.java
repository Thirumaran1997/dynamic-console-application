package com.dynamicconsole.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.utils.cli.CommandLineException;
import org.springframework.stereotype.Service;

import com.dynamicconsole.model.ConsoleDetails;
import com.dynamicconsole.service.ConsoleService;
import com.google.gson.Gson;

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
//				consoleDetail.setClassName(rs.getString(3));
//				consoleDetail.setMethodName(rs.getString(4));
				consoleDetail.setParameter(rs.getString(3));
				consoleDetail.setComments(rs.getString(4));
				consoleDetail.setDeployPath(rs.getString(5));
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
//			String class_name = (String) appDetailMap.get("className");
//			String method_name = (String) appDetailMap.get("methodName").toString();
			String parameters = (String) appDetailMap.get("parameters");
			String deploy_path = (String) appDetailMap.get("deployPath");
			String comments = (String) appDetailMap.get("comments");
			int rs = stmt.executeUpdate("insert into application_details(application_name,parameters,deploy_path,comments) "
					+ "values('"+application_name+"','"+parameters+"','"+deploy_path+"','"+comments+"');");
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
//			String class_name = (String) appDetailMap.get("className");
//			String method_name = (String) appDetailMap.get("methodName").toString();
			String parameters = (String) appDetailMap.get("parameters");
			String deploy_path = (String) appDetailMap.get("deployPath");
			String comments = (String) appDetailMap.get("comments");
			int rs = stmt.executeUpdate("update application_details set application_name='"+application_name+"',parameters='"+parameters+"',deploy_path='"+deploy_path+"',comments='"+comments+"' where application_id='"+appId+"';");
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
	
	@Override
	public Map<String, Object> runApplication(Map appDetailMap, HttpServletRequest request) throws Exception {
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("status","pass");
		String parameters = (String) appDetailMap.get("parameters");
		String deploy_path = (String) appDetailMap.get("deployPath");
		Path path = Paths.get(deploy_path);
		String fileName = path.getFileName().toString();
		Process proc = Runtime.getRuntime().exec("java -jar "+path.toString()+" "+parameters);
	    proc.waitFor();
	    InputStream in = proc.getInputStream();
	    InputStream err = proc.getErrorStream();
	    byte b[]=new byte[in.available()];
	    in.read(b,0,b.length);
	    System.out.println(new String(b));
	    byte c[]=new byte[err.available()];
	    err.read(c,0,c.length);
	    System.out.println(new String(c));
	    paramMap.put("message",new String(b));
	    if(c.length>0){
	    	paramMap.put("message",new String(c));
	    	paramMap.put("status","fail");
	    }
	    return paramMap;
	}

}