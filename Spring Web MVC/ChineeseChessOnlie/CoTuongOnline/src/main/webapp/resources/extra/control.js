var myApp = angular.module('myApp', []);

myApp.controller('MyAppController', function ($scope) {

    $scope.messages = ["a", "b"];
    $scope.yourMessage = "";

 
    /*----------Get list user from an URL-------------- */
    $scope.userOnline=null; // arrays of user online
    $.getJSON("http://localhost:8080/cotuong/rest/online",function(result){
    	 $scope.userOnline = result;
    });
    /*------------------------------------------------*/
    var soundForClick = null;
    soundManager.setup({
        onready: function () {
            soundForClick = soundManager.createSound({
                url: 'resources/extra/sounds/click-button.mp3'
            });
        },
        ontimeout: function () {

        }
    });


    $scope.sendMessage = function () {
        if (window.event.keyCode == 13) {
            if ($scope.yourMessage != null & $scope.yourMessage != "") {
                $scope.messages.push($scope.yourMessage);
                soundForClick.play();
                $scope.yourMessage = "";
                postMessage();
                document.getElementById("talks").scrollTop = document.getElementById("talks").scrollHeight;
            }
        }
    };
    $scope.showListUser = function () {
        soundForClick.play();
        $('#modalListUser').modal("show");
    };

    $scope.challengeUser = function () {
        soundForClick.play();
    };

    $scope.addFriend = function () {
        soundForClick.play();
    };

    $scope.validateYourCode=function(){

    }
});
