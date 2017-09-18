var myApp = angular.module('kiApp', []);
myApp.controller('myCtrl', function ($scope, $http) {
    $scope.cars = [];
    $http.get('/')
        .then(function (response) {
            $scope.cars = response.data; //For multiple row
            //$scope.persons[0] = response.data; //For single row
            console.log("status:" + response.status);
        }).catch(function (response) {
        console.error('Error occurred:', response.status, response.data);
    }).finally(function () {
        console.log("Task Finished.");
    });
});