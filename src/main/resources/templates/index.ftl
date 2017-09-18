<!DOCTYPE html>
<html lang="en">
<head>
    <title>Car Insurance</title>
    <link href="./css/bootstrap.css" rel="stylesheet"/>
    <link href="./css/app.css" rel="stylesheet"/>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
    <script type="text/javascript" src="./js/lib/angular-ui-router.min.js"></script>
    <script type="text/javascript" src="./js/app/app.js"></script>
    <script type="text/javascript" src="./js/app/ModelController.js"></script>
    <#--<script type="text/javascript" src="js/app/BrandController.js"></script>-->
</head>
<body>

<div ng-app="kiApp" ng-controller="myCtrl">
    <#--<p>Select a car:</p>-->

    <#--<select ng-model="selectedCar">-->
        <#--<option ng-repeat="x in cars" value="{{x.model}}">{{x.model}}</option>-->
    <#--</select>-->

    <#--<h1>You selected: {{selectedCar}}</h1>-->

        <div ng-repeat="(id,value) in brands">{{text}}</div>
        <select ng-model="brand.id" ng-options="brand.id as brand.value for brand in brands" ng-change="loadYearsByBrand(brand.id)" style="width: 150px;"></select>

        {{brand.id}}

        <div ng-repeat="(id,value) in years">{{text}}</div>
        <select ng-model="year.id" ng-options="year.id as year.value for year in years" ng-change="loadReference(brand.id, year.id)" style="width: 150px;"></select>

        {{year.id}}

        <div ng-repeat="(id,value) in references">{{text}}</div>
        <select ng-model="reference.id" ng-options="reference.id as reference.value for reference in references" style="width: 500px;"></select>

        {{reference.id}}
</div>

</body>
</html>