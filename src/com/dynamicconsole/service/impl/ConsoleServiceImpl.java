package com.dynamicconsole.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
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
	
	private void executeMavenProcess(String filePath) throws Exception {
		File tempPomFile = File.createTempFile("pom", ".xml");
		File currentPomFile = new File(System.getProperty("user.dir")+"/pom.xml");
		FileUtils.copyFile(currentPomFile,tempPomFile);
		Path path = Paths.get(filePath); 
	    String fileName = path.getFileName().toString(); 
		try{
		    FileReader fileReader = new FileReader(currentPomFile);
		    String currentLine;
		    String totalStr = "";
		    try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
		        while ((currentLine = bufferedReader.readLine()) != null) {
		            totalStr += currentLine;
		        }
				totalStr = totalStr.replaceAll("</dependencies>", "<dependency>"
		        		+ "<groupId>"+fileName+"</groupId>"
		        		+ "<artifactId>"+fileName+"</artifactId>"
		        		+ "<version>1.0</version></dependency>"
		        		+ "<scope>system</scope>"
		        		+ "<systemPath>"+filePath+"</systemPath>"
		        		+"</dependency>"
		        		+ "</dependencies>");
		        FileWriter fileWriter = new FileWriter(tempPomFile);
		        fileWriter.write(totalStr);
		        fileWriter.close();
		    }
		}catch(Exception e){
		    e.printStackTrace();
		}
		
	    InvocationRequest mavenRequest = new DefaultInvocationRequest();
	    mavenRequest.setPomFile(tempPomFile);
	    mavenRequest.setBaseDirectory(tempPomFile.getParentFile());
//	    mavenRequest.setLocalRepositoryDirectory(repoDir);
	    mavenRequest.setGoals(Arrays.asList("clean", "install"));
	    mavenRequest.setMavenOpts("-Dfile="+filePath+" -DgroupId="+fileName+" -DartifactId="+fileName+" -Dversion=1.0 -Dpackaging=jar");

	    Invoker invoker = new DefaultInvoker();
	    try {
	        InvocationResult result = invoker.execute(mavenRequest);
	        if (result.getExitCode() != 0) {
	            throw result.getExecutionException() != null ? result.getExecutionException()
	                    : new IllegalStateException("Build failure: " + result.getExitCode());
	        }
	    } catch (Exception e) {
	    	System.out.println(e.getMessage());
	    }
	}

}