var myApp = angular.module('myApp', []);
var soundForClick = null;
var email;
var myBoard;
/**
 *    The code below is basic function to receive data from game's server
 *    @onOpen
 *    @onMessage
 *    @onClose
 *    0: OK ----- 1: ERROR
 **/
var ws = new WebSocket("ws://localhost:8080/game");

ws.onopen = function (message) {
    email = user_data.email;
    if(email=="11520616@gm.uit.edu.vn")
        campOrder = 0;
    else campOrder = 1;
    ws.send("REG-" + email);
};

ws.onmessage = function (message) {
    var data = message.data.split("-|-");
    switch (data[0]) {
        case "REQHANDSHAKE":
            var scope = angular.element($(document.body)).scope();
            var id_requestHandShake = data[1];
            for(var i=0; i<scope.userOnline.length; i++){
                if(scope.userOnline[i].email==data[1]){
                    scope.opponent=scope.userOnline[i];
                    break;
                }
            }
            $('#modalAcceptChallenge').modal({
                backdrop: 'static',
                keyboard: false
            });
            $('#modalAcceptChallenge').modal('show');
            break;
        case "REPHANDSHAKE":
            var accept = data[1];  // 0 : yes , 1 : no
            if (accept == "0") {
                $('#modalWaitingAcceptChallenge').modal("hide");
            }else{
                // the enemy decline to accept
                //$('#modalWaitingAcceptChallenge').modal("hide");
            }
            break;
        case "REQPAUSE":
            var id_requestPause = 1;
            // show form yes or no
            break;
        case "REPPAUSE":
            var accept = data[1]; // 0 : yes , 1 : no
            if (accept == "0") {
                console.log("continue");
            }else{
                // the enemy decline to accept
            }
            break;
        case "CHAT":
            var text = data[1].replace("CHAT-|-", ""); // cut 'CHAT-|-' out data[1]
            var scope = angular.element($(document.body)).scope();
            scope.$apply(function(){
                scope.messages.push({'text': text, 'yours': true});
                scope.yourMessage = "";
            });
            document.getElementById("talks").scrollTop = document.getElementById("talks").scrollHeight;
            soundForClick.play();
            break;
        case "REQNEWGAME":
            var id_enemy=data[1]; // email enemy
            // get enemy info then show form accept or not
            break;
        case "REPNEWGAME":
            var accept = data[1];
            if (accept == "0") {
                console.log("continue");
            }else{
                // the enemy decline to accept
            }
            break;
        case "LOSE":
            var id_enemy=data[1]; // email enemy
            // alert enemy accept lose, then increase your point
            break;
        case "PLAY":
            console.log("Receiver : "+email);
            console.log(data[1]);
            var tmp = data[1].split(" ");
            var pos1 = tmp[0].split(",");
            var pos2 = tmp[1].split(",");
            var chessPos1 = new Point(pos1[0],9-pos1[1]);
            var chessPos2 = new Point(pos2[0],9-pos2[1]);
            myBoard.MoveEnemyChess(chessPos1,chessPos2,tmp[3]);
            //myBoard.changeMover(tmp[3]);
            break;
        case "MOVER":
            myBoard.changeMover(data[1]);
            break;
        default :
            break;
    }
};

ws.onclose =function(message){ ws.close();};

function requestHandShake(email){
    ws.send("REQHANDSHAKE-"+email);
}

function acceptHandShake() {
    ws.send("REPHANDSHAKE-"+ getEmailCurrentPlayer()+"-0");
}

function declineHandShake() {
    ws.send("REPHANDSHAKE-" + getEmailCurrentPlayer()+"-1");
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
    ws.send("REQDRAW-" + getEmailCurrentPlayer()+"-"+get);
}

function repDrawGame(){
    var response=0; // or 1
    ws.send("REPDRAW-"+response+"-"+ getEmailCurrentPlayer());
}

function getEmailCurrentPlayer(){
    var scope = angular.element($(document.body)).scope();
    return scope.opponent.email;
}

function playerMove(data){
    ws.send("PLAY-"+getEmailCurrentPlayer()+","+email+"-"+data);
}

function sendChat(data){
    ws.send(data);
}

myApp.controller('MyAppController', function ($scope, $http) {

    soundManager.setup({
        onready: function () {
            soundForClick = soundManager.createSound({
                url: 'resources/extra/sounds/click-button.mp3'
            });
        },
        ontimeout: function () {
        }
    });
    $scope.userOnline = [];
    $scope.myProfile =user_data;
    $scope.messages = [];
    $scope.yourMessage = "";
    $scope.titleOfChatConversation = "TO : ";
    $scope.countDown = 15;
    $scope.opponent = {};

    /**
    * GET list user online from server
    **/
    $.getJSON("http://localhost:8080/rest/online", function (result) {
        result = result
            .filter(function (el) {
                return el.name != $scope.myProfile.name;
            });
        $scope.userOnline = result;
        userOnlines=result;
    });

    /**
     * SHOW list user online
     **/
    $scope.showListUser = function () {
        $.getJSON("http://localhost:8080/rest/online", function (result) {
            result = result
                .filter(function (el) {
                    return el.name != $scope.myProfile.name;
                });
            $scope.userOnline = result;
            $scope.$apply();
        });
        soundForClick.play();
        $('#modalListUser').modal("show");
    };

    /**
     * SEND message from CHAT conversation to server
     * @param keyEvent
     **/
    $scope.sendMessage = function (keyEvent) {
        if (keyEvent.which == 13) {
            if ($scope.yourMessage != null & $scope.yourMessage != "") {
                $scope.messages.push({'text': $scope.yourMessage, 'yours': false});
                var to_client_id=getEmailCurrentPlayer();
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
     * CHALLENGE user in list user online
     * @param user
     **/
    $scope.challengeUser = function (user) {
        soundForClick.play();
        $('#modalListUser').modal("hide");
        $('#modalWaitingAcceptChallenge').modal("show");
        $scope.opponent=user;
        $scope.countDown=15;
        requestHandShake($scope.opponent.email);
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
     * ADD friend with user in list user online
     **/
    $scope.addFriend = function () {
        soundForClick.play();
    };
    /**
     * ACCEPT challenge from opponent
     **/
    $scope.acceptChallenge=function(){
        acceptHandShake();
    };
    /**
     * DISACCEPT challenge from opponent
     **/
    $scope.disAcceptChallenge=function(){
        declineHandShake();
    };

});
