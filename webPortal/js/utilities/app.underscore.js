// define underscore as an injectible dependency for modules. This will let you use underscore js features, with angular
angular.module("mitraPortal").constant('_', window._);