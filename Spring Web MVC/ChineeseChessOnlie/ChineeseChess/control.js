var myApp = angular.module('myApp', []);
var ws = new WebSocket("ws://localhost:8080/game");
ws.onopen = function (message) {
    var id_player = user_data.email;
    console.log(id_player);
    ws.send("REG-" + id_player);
};
ws.onmessage = function (message) {
    var data = message.data.split("-|-");
    switch (data[0]) {
        case "REQHANDSHAKE":
            var id_requestHandShake = data[1];

            break;
        case "REPHANDSHAKE":
            var accept = data[1];
            break;
        case "REQPAUSE":
            var id_requestPause = 1;

            break;
        case "REPPAUSE":
            var accept = data[1];
            if (accept == "1") {
                console.log("continue");
            }
            break;
        case "CHAT":
            console.log(data);
            var text = data[1].replace("CHAT-|-", ""); // cut 'CHAT-|-' out data[1]
            //myApp.$scope.messages.push({'text': text, 'yours': true});
            document.getElementById("talks").scrollTop = document.getElementById("talks").scrollHeight;
            soundForClick.play();
            break;
        case "REQNEWGAME":
            break;
        case "REPNEWGAME":
            break;
        case "LOSE":
            break;
        case "PLAY":
            console.log(data[1]);
            break;

    }
};
ws.onclose =function(message){ ws.close();};

/**
 *
 *
 *
 **/
function acceptHandShake() {
    ws.send("REPHANDSHAKE-0-" + getEmailCurrentPlayer());
}

function declineHandShake() {
    ws.send("REPHANDSHAKE-1-" + getEmailCurrentPlayer());
}

function requestPause() {
    ws.send("REQPAUSE-" + getEmailCurrentPlayer());
}
function acceptPause(){
    var id_requestPause = 1;
    ws.send("REQPAUSE-" + getEmailCurrentPlayer());
}
function acceptLose() {
    ws.send("LOSE-" + getEmailCurrentPlayer());
}
function requestDrawGame() {
    ws.send("REQDRAW-" + getEmailCurrentPlayer());
}
function repDrawGame(){
    var response=0; // or 1
    ws.send("REPDRAW-"+response+"-"+ getEmailCurrentPlayer());
}
function getEmailCurrentPlayer(){
    var id="11520616@gm.uit.edu.vn";
    return id;
}
function playerMove(data){
    console.log(data);
    ws.send("PLAY-"+getEmailCurrentPlayer()+"-"+data);
}
function sendChat(data){
    ws.send(data);
}
myApp.controller('MyAppController', function ($scope, $http) {
    /**
     *
     * Show popup list user online when starting
     *
     **/
    $scope.userOnline = [];
    $scope.myProfile =user_data;
    /**
     *
     * Arrays saved all message
     *
     **/
    $scope.messages = [];
    $scope.yourMessage = "";

    /**
     * Title of chat conversation. Example : "TO : John"
     **/
    $scope.titleOfChatConversation = "TO : ";

    /**
     * Count down 15 second
     **/
    $scope.countDown = 15;


    /**
     * Profile of opponent
     **/
    $scope.opponent = {};

    /**
     * Play sound when event click happen
     **/
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

    /**
     *    The code below is basic function to receive data from game's server
     *    @onOpen
     *    @onMessage
     *    @onClose
     *    0: OK ----- 1: ERROR
     **/


    /**
     *
     * GET list user online from server
     *
     **/
    $.getJSON("http://localhost:8080/rest/online", function (result) {
        result = result
               .filter(function (el) {
                        return el.name != $scope.myProfile.name;
                       });
        $scope.userOnline = result;
    });

    /**
     *
     * SHOW list user online
     *
     **/
    $scope.showListUser = function () {
        $.getJSON("http://localhost:8080/rest/online", function (result) {
            $scope.userOnline = result;
        });
        console.log($scope.userOnline);
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
                $scope.messages.push({'text': $scope.yourMessage, 'yours': false});
                /**
                 *
                 */
                var to_client_id = "11520616@gm.uit.edu.vn";
                if($scope.myProfile.email=="11520616@gm.uit.edu.vn"){
                    to_client_id="dominhquan.uit@gmail.com";
                }
                sendChat("CHAT-"+ to_client_id + "-" + $scope.yourMessage);
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
    $scope.challengeUser = function () {
        soundForClick.play();
        $('#modalListUser').modal("hide");
        $('#modalWaitingAcceptChallenge').modal("show");
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
     * ADD friend with user in list user online
     *
     **/
    $scope.addFriend = function () {
        soundForClick.play();
    };
});
