var myApp = angular.module('myApp', []);

myApp.controller('MyAppController', function ($scope, $http) {
    /**
     *
     * Object save my profile
     *
     **/
    $scope.myProfile = {
        "name": "My Name",
        "email": "myEmail@gmail.com",
        "point": "110",
        "img_url": "images/player2.jpg"
    };
    /**
     *
     * Object save opponent profile
     *
     **/
    $scope.opponent =null;
    /**
     *
     * Show popup list user online when starting
     *
     **/
    $scope.listUserOnline = [
        {
            "name": "Fuck you",
            "email": "fuckyou@gmail.com",
            "point": "110",
            "img_url": "images/player1.jpg"
        }
    ];
//    console.log($scope.userOnline);
//    $('#modalListUser').modal("show");
    /**
     *
     * Arrays saved all message
     *
     **/
    $scope.messages = [];
    $scope.yourMessage = "";

    /**
     *
     * Title of chat conversation. Example : "TO : John"
     *
     **/
    $scope.titleOfChatConversation = "TO : ";

    /**
     *
     * Count down 15 second
     *
     **/
    $scope.countDown = 15;

    /**
     *
     * Play sound when event click happen
     *
     **/
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

    /**
     *    The code below is basic function to receive data from game's server
     *    @onOpen
     *    @onMessage
     *    @onClose
     *    0: OK ----- 1: ERROR
     **/
    var ws = new WebSocket("ws://localhost:8080/cotuong/game");
    ws.onopen = function (message) {
        var id_player = 1;
        ws.send("REG-" + id_player);
    };
    ws.onmessage = function (message) {
        var data = message.data.split("-|-");
        switch (data[0]) {
            case "REQHANDSHAKE":
                var id_requestHandShake = data[1];

                break;
            case "ACCEPT_HANDSHAKE":
                // đóng lại

                break;
            case "PAUSE":
                var id_requestPause = 1;

                break;
            case "ACCEPT_PAUSE":
                var accept = data[1];
                if (accept == "1") {
                    console.log("continue");
                }
                break;
            case "CHAT":
                console.log(data[1]);
                //CHAT-|-TEXT"
                var text = data[1].replace("CHAT-|-", ""); // cut 'CHAT-|-' out data[1]
                $scope.messages.push({'text': text, 'yours': false});
                soundForClick.play();
                break;
            case "LOGIN":
                break;
            case "LOGOUT":
                break;
        }
    }
    ws.onclose = function (message) {
        ws.close();
    }
    /**
     *
     *
     *
     **/
    function acceptHandShake() {
        var id_requestHandshake = 1;
        ws.send("HANDSHAKE-0-" + id_requestHandshake);
    }

    function declineHandShake() {
        var id_requestHandshake = 1;
        ws.send("HANDSHAKE-1-" + id_requestHandshake);
    }

    function requestPause() {
        var id_requestPause = 1;
        ws.send("PAUSE-1-" + id_requestHandshake);
    }

    function acceptLose() {

    }

    function requestDrawGame() {

    }


    /**
     *
     * GET list user online from server
     *
     **/
//    $scope.userOnline = [];
    $.getJSON("http://localhost:8080/cotuong/rest/online", function (result) {
        $scope.listUserOnline = result;
    });

    /**
     *
     * SHOW list user online
     *
     **/
    $scope.showListUser = function () {
        soundForClick.play();
        $('#modalListUser').modal("show");
    };

    /**
     *
     * SEND message from CHAT conversation to server
     *
     **/
    $scope.sendMessage = function () {
        if (window.event.keyCode == 13) {
            if ($scope.yourMessage != null & $scope.yourMessage != "") {
                $scope.messages.push({'text': $scope.yourMessage, 'yours': true});
                /**
                 *
                 */
                var to_client_id = 2;
                ws.send("CHAT-" + to_client_id + "-" + $scope.yourMessage);

                /**
                 *
                 */
                soundForClick.play();
                $scope.yourMessage = "";
                document.getElementById("talks").scrollTop = document.getElementById("talks").scrollHeight;
            }
        }
    };

    /**
     *
     * CHALLENGE user in list user online
     *
     **/
    $scope.challengeUser = function (opponent) {
        soundForClick.play();
        $scope.opponent=opponent;
        console.log(opponent);
        $scope.$apply();
        //
        //User A wanna play with B, send request handshake to server
        //
        ws.send("REQHANDSHAKE-"+opponent.email);

        $('#modalListUser').modal("hide");
        $('#modalWaitingAcceptChallenge').modal("show");
        //
        //Processing countdown
        //
        $scope.countDown=15;
        var timeCountDown = setInterval(function(){
            if($scope.countDown>0){
                $scope.countDown--;
                $scope.$apply();
            }else{
                clearInterval(timeCountDown);
                $('#modalWaitingAcceptChallenge').modal("hide");
            }
        },1000);

    };
    /**
     *
     * User wanna pause game with opponent
     *
     **/
    $scope.requestPause = function (opponent) {
        soundForClick.play();
        //
        //User A wanna pause game with opponent B, send request pause to server
        //
        ws.send("REQPAUSE-"+$scope.opponent.email);
    };
    /**
     *
     * User wanna end game with opponent
     *
     **/
    $scope.requestPause = function (opponent) {
        soundForClick.play();
        //
        //User A wanna end game with opponent B, send request end to server
        //
        ws.send("END-"+opponent.email);
    };

    /**
     *
     * ADD friend with user in list user online
     *
     **/
    $scope.addFriend = function () {
        soundForClick.play();
    };

    /**
     *
     * VALIDATE code
     *
     **/
    $scope.validateYourCode = function () {

    }
});