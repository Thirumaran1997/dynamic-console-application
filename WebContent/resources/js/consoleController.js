'use strict';
App.controller('consoleController', ['$scope', '$rootScope', 'consoleService','$http',
		function($scope, $rootScope, consoleService, $http) {
			$scope.showUserDetails = false;
			$scope.showLogin = true;
			$scope.userDetails = [];
        	$scope.detail ={};
        	$scope.isPortalPage = true;
        	$scope.isDetailPage = false;
        	$scope.isInsertMode = true;
        	$scope.isExecutionMode = false;
               				
			$scope.initialize = function(){
				$scope.detail['applicationName'] ="";
//	        	$scope.detail['className'] ="";
//	        	$scope.detail['methodName'] ="";
	        	$scope.detail['parameters'] ="";
	        	$scope.detail['deployPath'] ="";
	        	$scope.detail['comments'] ="";
	        	$scope.detail['executionOut']="Your code execution output comes here...";
			}
			
			$scope.getUserDetails = function() {
				consoleService.getUserDetails().then(
						function(d) {
							$scope.userDetails = d;
							$scope.inputUserDetails = false;
						});
			}
			
			$scope.createNewApplication = function(){
				$scope.initialize();
				$scope.isDetailPage = true;
				$scope.isPortalPage = false;
				$scope.isInsertMode = true;
				$scope.isUpdateMode = false;
	        	$scope.isExecutionMode = false;
			}
			
			$scope.insertRecords = function(){
				consoleService.insertRecords($scope.detail).then(
						function(response){
							if(response['data']['status']==='pass'){
								alert('Success');
								$scope.initialize();
							}else
								alert('An error occurred!');
						});
			}
			
			$scope.onCancel = function(){
				$scope.isPortalPage = true;
	        	$scope.isDetailPage = false;
				$scope.getUserDetails();
			}
			
			$scope.editRecord = function(){
				$scope.isPortalPage = false;
	        	$scope.isDetailPage = true;
	        	$scope.isExecutionMode = false;
				$scope.isInsertMode = false;
				$scope.isUpdateMode = true;
				var row  = event.currentTarget.closest('tr');
				$scope.detail['applicationId'] = row.cells[0].textContent;
				$scope.detail['applicationName'] = row.cells[1].textContent;
//	        	$scope.detail['className'] =row.cells[2].textContent;
//	        	$scope.detail['methodName'] =row.cells[3].textContent;
	        	$scope.detail['parameters'] =row.cells[2].textContent;
	        	$scope.detail['deployPath'] =row.cells[3].textContent;
	        	$scope.detail['comments'] =row.cells[4].textContent;
			}
			
			$scope.updateRecords = function(){
				consoleService.updateRecords($scope.detail).then(
						function(response){
							if(response['data']['status']==='pass'){
								alert('Success');
								$scope.showUserDetails = true;
								$scope.inputUserDetails = false;
								$scope.getUserDetails();
							}else
								alert('An error occurred!');
						});
			}
			
			$scope.deleteRecord = function(){
				var row  = event.currentTarget.closest('tr');
				$scope.detail['applicationId'] = row.cells[0].textContent;
				consoleService.deleteRecord($scope.detail).then(
						function(response){
							if(response['data']['status']==='pass'){
								alert('Success');
								$scope.showUserDetails = true;
								$scope.inputUserDetails = false;
								$scope.getUserDetails();
							}else
								alert('An error occurred!');
						});
			}
			
			$scope.runApplication = function(){
				$scope.isPortalPage = false;
	        	$scope.isDetailPage = true;
	        	$scope.isExecutionMode = true;
				$scope.isInsertMode = false;
				$scope.isUpdateMode = false;
				var row  = event.currentTarget.closest('tr');
//				$scope.detail['className'] =row.cells[2].textContent;
//	        	$scope.detail['methodName'] =row.cells[3].textContent;
	        	$scope.detail['parameters'] =row.cells[2].textContent;
	        	$scope.detail['deployPath'] =row.cells[3].textContent;
				consoleService.runApplication($scope.detail).then(
						function(response){
							if(response['data']['status']==='pass'){
								$scope.showUserDetails = true;
								$scope.inputUserDetails = false;
								$scope.detail['executionOut'] = response['data']['message'];
							}else if(response['data']['status']==='fail'){
								alert('An error occurred!');
								$scope.detail['executionOut'] = response['data']['message'];
							}
						});
			}
			
			$scope.uploadFiles = function(){
				var uploadUrl="localhost:8080/DynamicConsoleApplication/upload";
				var formData=new FormData();
				formData.append("file",file.files[0]);
				$http.post('upload',formData,{
		            transformRequest: angular.identity,
		            headers: {'Content-Type': undefined}
		        })
	        	.then(function(response){
	        		return response;
	        	});
			}
			
		} ]);