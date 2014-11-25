var myApp = angular.module('myApp', []);

myApp.controller('MyAppController', function ($scope) {

    $scope.messages = ["a", "b"];
    $scope.yourMessage = "";

    $scope.users = [
        {
            "name": "Nguyen Van Teo",
            "score": "100",
            "image": "http://i.imgur.com/euNuShD.jpg"
        },
        {
            "name": "Nguyen Van A",
            "score": "80",
            "image": "http://i.imgur.com/4llobMm.jpg"
        },
        {
            "name": "Nguyen Van B",
            "score": "80",
            "image": "http://i.imgur.com/FMlQpFn.jpg"
        },
        {
            "name": "Nguyen Van C",
            "score": "80",
            "image": "images/player2.jpg"
        },
        {
            "name": "Nguyen Van D",
            "score": "80",
            "image": "images/player1.jpg"
        },
        {
            "name": "Nguyen Van E",
            "score": "80",
            "image": "images/player2.jpg"
        },
        {
            "name": "Nguyen Van C",
            "score": "80",
            "image": "images/player2.jpg"
        },
        {
            "name": "Nguyen Van D",
            "score": "80",
            "image": "images/player1.jpg"
        },
        {
            "name": "Nguyen Van E",
            "score": "80",
            "image": "images/player2.jpg"
        },
        {
            "name": "Nguyen Van C",
            "score": "80",
            "image": "images/player2.jpg"
        },
        {
            "name": "Nguyen Van D",
            "score": "80",
            "image": "images/player1.jpg"
        },
        {
            "name": "Nguyen Van E",
            "score": "80",
            "image": "images/player2.jpg"
        }
    ];
    /*----------Get list user from an URL-------------- */
    $.getJSON("http://localhost:8080/cotuong/rest/online",function(result){
        var data = [];
       	$.each(result, function() {
       		var element = [];
       		console.log(this);
       	});
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
