<!DOCTYPE html>

<html lang="en" ng-app="kiApp">
<head>
    <title>Car Insurance</title>
    <link href="css/bootstrap.css" rel="stylesheet"/>
    <link href="css/app.css" rel="stylesheet"/>
</head>
<body>

<div ui-view></div>
<script type="text/javascript" src="js/lib/angular.min.js"></script>
<script type="text/javascript" src="js/lib/angular-ui-router.min.js"></script>
<script type="text/javascript" src="js/app/app.js"></script>
<script type="text/javascript" src="js/app/ModelController.js"></script>
<script type="text/javascript" src="js/app/BrandController.js"></script>

<h1>Hello World</h1>

<div ng-controller="ModelController">
    <form name="myForm">

        <select name="brand" id="brand"
                ng-options="option.name for option in data.availableOptions track by option.id"
                ng-model="data.selectedOption"
                class="ng-pristine ng-scope ng-invalid ng-invalid-required ng-touched"></select>


        <select name="modelYear" id="modelYear"
                ng-options="option.name for option in data.availableOptions track by option.id"
                ng-model="data.selectedOption"
                class="ng-pristine ng-scope ng-invalid ng-invalid-required ng-touched"></select>


        <select ng-model="model.id" convert-to-number>
            <option value="0">Zero</option>
            <option value="1">One</option>
            <option value="2">Two</option>
        </select>
        {{ model }}
    </form>
    <hr>
    <code>option = {{data.selectedOption}}</code><br/>
</div>

</body>
</html>