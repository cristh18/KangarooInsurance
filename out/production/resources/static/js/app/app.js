var app = angular.module('kiApp',['ui.router']);

// app.config(['$stateProvider', '$urlRouterProvider',
//     function($stateProvider, $urlRouterProvider) {
//
//         $stateProvider
//             .state('home', {
//                 url: '/',
//                 templateUrl: 'partials/list',
//                 controller:'UserController',
//                 controllerAs:'ctrl',
//                 resolve: {
//                     users: function ($q, UserService) {
//                         console.log('Load all users');
//                         var deferred = $q.defer();
//                         UserService.loadAllUsers().then(deferred.resolve, deferred.resolve);
//                         return deferred.promise;
//                     }
//                 }
//             });
//         $urlRouterProvider.otherwise('/');
//     }]);