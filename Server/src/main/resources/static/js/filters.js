'use strict';

/* Filters */


define([
   	  'angular' 
  	
  	], function (angular) {//加载依赖js,
	
	

return angular.module('app.filters', []).
  filter('interpolate', ['version', function(version) {
    return function(text) {
      return String(text).replace(/\%VERSION\%/mg, version);
    };
  }]);
});