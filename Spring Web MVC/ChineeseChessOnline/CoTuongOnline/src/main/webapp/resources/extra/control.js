var myApp = angular.module('myApp', []);
var soundForClick = null;
var email;
var myBoard;
var chessGame = new ChessGame("board");
var ws = new WebSocket("ws://localhost:8080/game");

ws.onopen = function (message) {
    email = user_data.email;
    ws.send("REG-" + email);
    getListUserOnline();
};

ws.onmessage = function (message) {
    var data = message.data.split("-|-");
    switch (data[0]) {
        case "REQHANDSHAKE":
            var scope = angular.element($(document.body)).scope();
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
            if (data[1] == "0") {
                $('#modalWaitingAcceptChallenge').modal('hide');
            }else{
                alert('Player decline');
                $('#modalWaitingAcceptChallenge').modal('hide');
            }
            break;
        case "REQPAUSE":
            $('#modalRepPause').modal({
                backdrop: 'static',
                keyboard: false
            });
            $('#modalRepPause').modal('show');
            break;
        case "REPPAUSE":
            if (data[1] == "0") {
                chessGame.lockChess();
            }else{
                alert("Decline pause");
            }
            $('#modalWaitingRepPause').modal('hide');
            break;
        case "REPUNPAUSE":
            if (data[1] == "0") {
                chessGame.lockChess();
            }else{
                alert("Decline unpause");
            }
            break;
        case "REQNEWGAME":
            $('#modalRepNewGame').modal({
                backdrop: 'static',
                keyboard: false
            });
            $('#modalRepNewGame').modal('show');
            break;
        case "REPNEWGAME":
            if (data[1] == "0") {
                campOrder = 0;
                chessGame.init();
                chessGame.mover=0;
            }else{
                // the enemy decline to accept
            }
            break;
        case "REQDRAW":
            $('#modalRepDraw').modal({
                backdrop: 'static',
                keyboard: false
            });
            $('#modalRepDraw').modal('show');
            break;
        case "REPDRAW":
            if (data[1] == "0") {
                chessGame.lockChess();
                alert("Draw ! No point for this match ");
            }else{
                alert("Your enemy decline to draw ");
            }
            $('#modalWaitingRepDraw').modal('hide');
            break;
        case "LOSE":
            alert("User "+data[1]+ "accept lose");
            chessGame.lockChess();
            break;
        case "PLAY":
            var tmp = data[1].split(" ");
            var pos1 = tmp[0].split(",");
            var pos2 = tmp[1].split(",");
            var chessPos1 = new Point(pos1[0],9-pos1[1]);
            var chessPos2 = new Point(pos2[0],9-pos2[1]);
            myBoard.MoveEnemyChess(chessPos1,chessPos2,tmp[3]);
            break;
        case "MOVER":
            myBoard.changeMover(data[1]);
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
        default :
            break;
    }
};

ws.onclose =function(message){ ws.close();};

function requestHandShake(email){
    ws.send("REQHANDSHAKE-"+email);
}

function repHandShake(rep){
    ws.send("REPHANDSHAKE-" + getEmailCurrentPlayer()+"-"+rep);
}

function requestNewGame(){
    ws.send("REQNEWGAME-"+ getEmailCurrentPlayer());
}

function repNewGame(rep){
    if(rep=="0"){
        campOrder=1;
        chessGame.init();
        chessGame.mover=1;
    }
    ws.send("REPNEWGAME-" + getEmailCurrentPlayer()+"-"+rep);
}

function requestPause() {
    ws.send("REQPAUSE-" + getEmailCurrentPlayer());
}

function repPause(rep){
    if(rep==0){
        //accept request pause => lock chess board.
        chessGame.lockChess();
    }
    ws.send("REPPAUSE-" + getEmailCurrentPlayer()+"-"+rep);
}

function requestUnPause(){
    ws.send("REQUNPAUSE-" + getEmailCurrentPlayer());
}

function repUnPause(rep){
    if(rep==0){
        //accept request un pause => unlock chess board.
        chessGame.lockChess();
    }
    ws.send("REPUNPAUSE-" + getEmailCurrentPlayer()+"-"+rep);
}

function acceptLose() {
    ws.send("LOSE-" + getEmailCurrentPlayer());
    chessGame.lockChess();
}

function requestDrawGame() {
    ws.send("REQDRAW-" + getEmailCurrentPlayer());
}

function repDrawGame(rep){
    if(rep==0){
        //accept request draw => lock chess board.
        chessGame.lockChess();
    }
    ws.send("REPDRAW-" +getEmailCurrentPlayer()+"-"+rep);
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

function getListUserOnline(){
    var scope = angular.element($(document.body)).scope();
    $.getJSON("http://localhost:8080/rest/online", function (result) {
        result = result
            .filter(function (el) {
                return el.name != scope.myProfile.name;
            });
        scope.userOnline = result;
        scope.$apply();
    });
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
    //$scope.opponent = {};

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
     * ACCEPT | DECLINE challenge from opponent
     **/
    $scope.acceptChallenge=function(){
        soundForClick.play();
        repHandShake(0);
    };
    $scope.declineChallenge=function(){
        repHandShake(1);
        soundForClick.play();
    };

    /**
     * Send Request PAUSE game
     **/
    $scope.sendReqPause = function () {
        soundForClick.play();
        $('#modalWaitingRepPause').modal({
            backdrop: 'static',
            keyboard: false
        });
        $scope.countDown=20;
        $('#modalWaitingRepPause').modal('show');
        requestPause();
        var timeCountDown = setInterval(function(){
            if($scope.countDown>0){
                $scope.countDown--;
                $scope.$apply();
            }else{
                clearInterval(timeCountDown);
                $('#modalWaitingRepPause').modal('hide');
            }
        },1000);
    };

    /**
     * Send Request UNPAUSE game
     **/
    $scope.sendReqUnPause = function () {
        soundForClick.play();
        $('#modalWaitingRepUnPause').modal({
            backdrop: 'static',
            keyboard: false
        });
        $scope.countDown=20;
        $('#modalWaitingRepUnPause').modal('show');
        requestPause();
        var timeCountDown = setInterval(function(){
            if($scope.countDown>0){
                $scope.countDown--;
                $scope.$apply();
            }else{
                clearInterval(timeCountDown);
                $('#modalWaitingRepUnPause').modal('hide');
            }
        },1000);
    };

    /**
     * Send Request DRAW game
     **/
    $scope.sendReqDraw = function () {
        soundForClick.play();
        $('#modalWaitingRepDraw').modal({
            backdrop: 'static',
            keyboard: false
        });
        $scope.countDown=20;
        $('#modalWaitingRepDraw').modal('show');
        requestDrawGame();
        var timeCountDown = setInterval(function(){
            if($scope.countDown>0){
                $scope.countDown--;
                $scope.$apply();
            }else{
                clearInterval(timeCountDown);
                $('#modalWaitingRepDraw').modal('hide');
            }
        },1000);
    };

    /**
     * ACCEPT | DECLINE Rep Pause from opponent
     **/
    $scope.modalRepNewGame=function(rep){
        repNewGame(rep);
    };

    /**
     * ACCEPT | DECLINE Rep Pause from opponent
     **/
    $scope.modalRepPause=function(rep){
        repPause(rep);
    };
    /**
     * ACCEPT | DECLINE Rep UnPause from opponent
     **/
    $scope.modalRepUnPause=function(rep){
        repUnPause(rep);
    };
    /**
     * ACCEPT | DECLINE Rep Draw from opponent
     **/
    $scope.modalRepDraw=function(rep){
        repDrawGame(rep);
    };
    /**
     * ACCEPT | DECLINE Rep Loose from opponent
     **/
    $scope.modalRepLoose=function(rep){

    };
});