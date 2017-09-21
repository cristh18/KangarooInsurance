var myApp = angular.module('kiApp', []);
myApp.controller('myCtrl', function ($scope, $http, $window) {
    $scope.brands = [];
    $scope.years = [];
    $scope.references = [];

    loadPage();

    function loadPage() {
        $http.get('/brands')
            .then(function (response) {
                $scope.brands = response.data; //For multiple row
                //$scope.persons[0] = response.data; //For single row
                console.log("status:" + response.status);
            }).catch(function (response) {
            console.error('Error occurred:', response.status, response.data);
        }).finally(function () {
            console.log("Task Finished.");
        });
    }

    $scope.loadYearsByBrand = function (id) {
        if (id !== null) {
            loadYearsByBrand();
        }
    };

    function loadYearsByBrand() {
        $http.get('/year/' + $scope.brand.id)
            .then(function (response) {
                $scope.years = response.data; //For multiple row
                //$scope.persons[0] = response.data; //For single row
                console.log("status:" + response.status);
            }).catch(function (response) {
            console.error('Error occurred:', response.status, response.data);
        }).finally(function () {
            console.log("Task Finished.");
        });
    }

    $scope.loadReference = function (brandId, yearId) {
        if (brandId !== null && yearId !== null) {
            loadReference();
        }
    };

    function loadReference() {
        $http.get('/reference/' + $scope.brand.id + '/' + $scope.year.id)
            .then(function (response) {
                $scope.references = response.data; //For multiple row
                //$scope.persons[0] = response.data; //For single row
                console.log("status:" + response.status);
            }).catch(function (response) {
            console.error('Error occurred:', response.status, response.data);
        }).finally(function () {
            console.log("Task Finished.");
        });
    }

    function sendVehicleData() {
        $http({
            method: 'POST',
            url: '/vehicle',
            data: {
                referrer: 'Direct',
                platform: 'computer',
                brand: 'aleko',
                modelYear: '1993',
                model: '102345'
            }
        }).then(function (response) {
                console.log("status:" + response.status);
            }).catch(function (response) {
            console.error('Error occurred:', response.status, response.data);
        }).finally(function () {
            console.log("Task Finished.");
        });
    }

    $scope.submit = function () {
        sendVehicleData();
        $window.location.href = 'kiapp/list.html';
    }
});