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

        <div ng-repeat="(id,value) in cars">{{text}}</div>
        <select ng-model="model.id" ng-options="car.id as car.value for car in cars" style="width: 150px;"></select>

        {{model.id}}
</div>

</body>
</html>