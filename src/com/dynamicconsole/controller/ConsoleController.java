package com.dynamicconsole.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dynamicconsole.model.Message;
import com.dynamicconsole.model.OutputMessage;
import com.dynamicconsole.service.ConsoleService;
import com.google.gson.Gson;

@Controller
public class ConsoleController {
	
	@Autowired
	private ConsoleService consoleService;
	
	@RequestMapping(value = "/getDetails", method = RequestMethod.GET)
	public ResponseEntity getAppDetails() throws ClassNotFoundException {
        
		List appDetails = consoleService.getAppDetails();
		return new ResponseEntity(appDetails, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity insert(@RequestBody String appDetails,HttpServletRequest request) throws ClassNotFoundException {
        Map appDetailsMap = new Gson().fromJson(appDetails, Map.class);
		Map<String,Object> returnMap = consoleService.insertAppDetails(appDetailsMap);
		return new ResponseEntity(returnMap, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity update(@RequestBody String appDetails,HttpServletRequest request) throws ClassNotFoundException {
        Map appDetailsMap = new Gson().fromJson(appDetails, Map.class);
		Map<String,Object> returnMap = consoleService.updateAppDetails(appDetailsMap);
		return new ResponseEntity(returnMap, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity delete(@RequestBody String appDetails,HttpServletRequest request) throws ClassNotFoundException {
        Map appDetailsMap = new Gson().fromJson(appDetails, Map.class);
		Map<String,Object> returnMap = consoleService.deleteAppDetails(appDetailsMap);
		return new ResponseEntity(returnMap, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/runApplication", method = RequestMethod.POST)
	public ResponseEntity runApplication(@RequestBody String appDetails,HttpServletRequest request) throws Exception {
        Map appDetailsMap = new Gson().fromJson(appDetails, Map.class);
		Map<String,Object> returnMap = consoleService.runApplication(appDetailsMap);
		return new ResponseEntity(returnMap, HttpStatus.OK);
	}
}