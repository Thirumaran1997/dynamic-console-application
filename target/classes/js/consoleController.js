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
               				
			$scope.initialize = function(){
				$scope.detail['applicationName'] ="";
	        	$scope.detail['className'] ="";
	        	$scope.detail['methodName'] ="";
	        	$scope.detail['parameters'] ="";
	        	$scope.detail['deployPath'] ="";
	        	$scope.detail['comments'] ="";
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
				$scope.isInsertMode = false;
				$scope.isUpdateMode = true;
				var row  = event.currentTarget.closest('tr');
				$scope.detail['applicationId'] = row.cells[0].textContent;
				$scope.detail['applicationName'] = row.cells[1].textContent;
	        	$scope.detail['className'] =row.cells[2].textContent;
	        	$scope.detail['methodName'] =row.cells[3].textContent;
	        	$scope.detail['parameters'] =row.cells[4].textContent;
	        	$scope.detail['deployPath'] =row.cells[5].textContent;
	        	$scope.detail['comments'] =row.cells[6].textContent;
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
			
		} ]);