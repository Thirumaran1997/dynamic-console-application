package com.dynamicconsole.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ConsoleService {

	List getAppDetails() throws ClassNotFoundException;

	Map<String, Object> insertAppDetails(Map appDetailsMap) throws ClassNotFoundException;

	Map<String, Object> updateAppDetails(Map appDetailsMap) throws ClassNotFoundException;

	Map<String, Object> deleteAppDetails(Map appDetailsMap) throws ClassNotFoundException;

	Map<String, Object> runApplication(Map appDetailsMap) throws Exception;


}
