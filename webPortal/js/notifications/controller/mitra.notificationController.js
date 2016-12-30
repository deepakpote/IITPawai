webPortal.controller("loginController", function($scope) {
   $scope.student = {
      phoneno: "Mahesh",
      passkey: "Parashar",
      utoken:"user token",

      authenticateUser: function() {
         //This will validate if the user token exists
      }
   };
});