var myApp = angular.module('myApp', []);

myApp.controller('MyAppController', function ($scope, $http) {

    $scope.messages = ["a", "b"];
    $scope.yourMessage = "";

    $scope.users = [
        {
            "name": "Nguyen Van Teo",
            "score": "80",
            "image": "images/player1.jpg"
        },
        {
            "name": "Nguyen Van A",
            "score": "80",
            "image": "images/player2.jpg"
        },
        {
            "name": "Nguyen Van B",
            "score": "80",
            "image": "images/player1.jpg"
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
    $scope.userOnline=null; // arrays of user online
    $http.get("http://www.w3schools.com//website/Customers_JSON.php")
        .success(function(data) {
            $scope.userOnline = data;
            console.log(data);
        });
    /*------------------------------------------------*/

    var soundForClick = null;
    soundManager.setup({
        onready: function () {
            soundForClick = soundManager.createSound({
                url: 'sounds/click-button.mp3'
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


                $("#talks").animate({ scrollTop: $(document).height() }, "slow");
//                $("#talks").scrollTop = $("#talks").height();
                return false;
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

    $scope.validateYourCode = function () {

    }
});
