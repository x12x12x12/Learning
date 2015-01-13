var myApp = angular.module('myApp', []);
var chessGame = new ChessGame("board");
var ws = new WebSocket("ws://localhost:8080/game");
var soundForClick,email,myBoard,counter,myVar;
//var time=10;
//var sidemove=0;

ws.onopen = function (message) {
    email = user_data.email;
    ws.send("REG-" + email);
    getListUserOnline();
    lockControlButton(1,1,1,1,1,1);
};

ws.onmessage = function (message) {
    var data = message.data.split("-|-");
    switch (data[0]) {
        case "REQHANDSHAKE":
            var scope = angular.element($(document.body)).scope();
            for(var i=0; i<scope.userOnline.length; i++){
                if(scope.userOnline[i].email==data[1]){
                    scope.opponent=scope.userOnline[i];
                    scope.$apply();
                    break;
                }
            }
            $('#modalAcceptChallenge').modal({
                backdrop: 'static',
                keyboard: false
            });
            $('#modalAcceptChallenge').modal('show');
            //countDown(15,"#modalRepPause","timerREQPAUSE");
            break;
        case "REPHANDSHAKE":
            if (data[1] == "0") {
                $('#modalWaitingAcceptChallenge').modal('hide');
                lockControlButton(0,1,1,1,1,0);
            }else{
                alert('Player decline');
                $('#modalWaitingAcceptChallenge').modal('hide');
            }
            break;
        case "REQNEWGAME":
            $('#modalRepNewGame').modal({
                backdrop: 'static',
                keyboard: false
            });
            $('#modalRepNewGame').modal('show');
            //countDown(15,"#modalRepPause","timerREQPAUSE");
            break;
        case "REPNEWGAME":
            if (data[1] == "0") {
                campOrder = 0;
                chessGame.init();
                //chessGame.mover=0;
                lockControlButton(1,0,1,0,0,0);
                //myVar = setInterval(function(){ myTimer() }, 1000);
            }else{
                lockControlButton(0,1,1,1,1,0);
            }
            break;
        case "REQPAUSE":
            $('#modalRepPause').modal({
                backdrop: 'static',
                keyboard: false
            });
            $('#modalRepPause').modal('show');
            countDown(15,"#modalRepPause","timerREQPAUSE");
            break;
        case "REPPAUSE":
            if (data[1] == "0") {
                chessGame.lockChess();
                lockControlButton(1,1,0,0,0,0);
            }else{
                alert("Decline pause");
            }
            $('#modalWaitingRepPause').modal('hide');
            clearInterval(counter);
            break;
        case "REQUNPAUSE":
            $('#modalRepUnPause').modal({
                backdrop: 'static',
                keyboard: false
            });
            $('#modalRepUnPause').modal('show');
            countDown(15,"#modalRepUnPause","timerREQUNPAUSE");
            break;
        case "REPUNPAUSE":
            if (data[1] == "0") {
                chessGame.lockChess();
                lockControlButton(1,0,1,0,0,0);
            }else{
                alert("Decline unpause");
            }
            $('#modalWaitingRepUnPause').modal('hide');
            clearInterval(counter);
            break;
        case "REQDRAW":
            $('#modalRepDraw').modal({
                backdrop: 'static',
                keyboard: false
            });
            $('#modalRepDraw').modal('show');
            countDown(15,"#modalRepDraw","timerREQDRAW");
            break;
        case "REPDRAW":
            if (data[1] == "0") {
                chessGame.lockChess();
                alert("Draw ! No point for this match ");
                lockControlButton(0,1,1,1,1,0);
            }else{
                alert("Your enemy decline to draw ");
            }
            $('#modalWaitingRepDraw').modal('hide');
            clearInterval(counter);
            break;
        case "LOSE":
            lockControlButton(0,1,1,1,1,0);
            chessGame.lockChess();
            var scope = angular.element($(document.body)).scope();
            scope.$apply(function(){
                scope.opponent.point-=10;
            });
            $('#modalOpponentLoseGame').modal({
                backdrop: 'static',
                keyboard: false
            });
            $('#modalOpponentLoseGame').modal('show');
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
    soundForClick.play();
}

function repHandShake(rep){
    if(rep==0){
        lockControlButton(0,1,1,1,1,0);
    }
    ws.send("REPHANDSHAKE-" + getEmailCurrentPlayer()+"-"+rep);
}

function requestNewGame(){
    ws.send("REQNEWGAME-"+ getEmailCurrentPlayer());
    soundForClick.play();
}

function repNewGame(rep){
    if(rep=="0"){
        campOrder=1;
        chessGame.init();
        //chessGame.mover=1;
        lockControlButton(1,0,1,0,0,0);
    }
    ws.send("REPNEWGAME-" + getEmailCurrentPlayer()+"-"+rep);
}

function requestPause() {
    ws.send("REQPAUSE-" + getEmailCurrentPlayer());
    soundForClick.play();
}

function repPause(rep){
    if(rep==0){
        //accept request pause => lock chess board.
        chessGame.lockChess();
        lockControlButton(1,1,0,0,0,0);
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
        lockControlButton(1,0,1,0,0,0);
    }
    ws.send("REPUNPAUSE-" + getEmailCurrentPlayer()+"-"+rep);
}

function acceptLose() {
    ws.send("LOSE-" + getEmailCurrentPlayer());
    chessGame.lockChess();
    lockControlButton(0,1,1,1,1,0);
    var scope = angular.element($(document.body)).scope();
    scope.$apply(function(){
        scope.opponent.point+=10;
    });
    $('#modalYouLoseGame').modal({
        backdrop: 'static',
        keyboard: false
    });
    $('#modalYouLoseGame').modal('show');
}

function requestDrawGame() {
    ws.send("REQDRAW-" + getEmailCurrentPlayer());
}

function repDrawGame(rep){
    if(rep==0){
        //accept request draw => lock chess board.
        chessGame.lockChess();
        lockControlButton(0,1,1,1,1,0);
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

function lockControlButton(btn_newgame,btn_pause,btn_unpause,btn_draw,btn_lose,btn_quit){
    var scope = angular.element($(document.body)).scope();
        if(btn_newgame==0){
            scope.isDisabled.btn_newGame=false;
        }
        if(btn_newgame==1){
            scope.isDisabled.btn_newGame=true;
        }
        if(btn_pause==0){
            scope.isDisabled.btn_pause=false;
        }
        if(btn_pause==1){
            scope.isDisabled.btn_pause=true;
        }
        if(btn_unpause==0){
            scope.isDisabled.btn_unpause=false;
        }
        if(btn_unpause==1){
            scope.isDisabled.btn_unpause=true;
        }
        if(btn_draw==0){
            scope.isDisabled.btn_draw=false;
        }
        if(btn_draw==1){
            scope.isDisabled.btn_draw=true;
        }
        if(btn_lose==0){
            scope.isDisabled.btn_lose=false;
        }
        if(btn_lose==1){
            scope.isDisabled.btn_lose=true;
        }
        if(btn_quit==0){
            scope.isDisabled.btn_quit=false;
        }
        if(btn_quit==1){
            scope.isDisabled.btn_quit=true;
        }
    scope.$apply();
}

function countDown(count,modal,id){
    counter=setInterval(timer, 1000); //1000 will  run it every 1 second
    function timer() {
        document.getElementById(id).innerHTML=count + " secs";
        count=count-1;
        if (count <= 0)  {
            clearInterval(counter);
            $(modal).modal("hide");
        }
    }
}

//function resetTimer(move) {
//    if(mover != move){
//        document.getElementById("demo3").innerHTML = "Chua toi luot ban";
//        return;
//    }
//    if(mover == 0) {
//        document.getElementById("demo3").innerHTML = "A da di co , tuoi luot B";
//    }
//    else {
//        document.getElementById("demo3").innerHTML = "B da di co , tuoi luot A";
//    }
//    mover = 1 - mover;
//    clearInterval(myVar);
//    t1=10;
//    document.getElementById("demo1").innerHTML = t1;
//    document.getElementById("demo2").innerHTML = t1;
//    myVar = setInterval(function(){myTimer()}, 1000);
//}
//
//function myTimer() {
//    document.getElementById("demo1").innerHTML = t1;
//    document.getElementById("demo2").innerHTML = t1;
//    t1--;
//    if(t1 == 0 && mover == 0 ){
//
//        alert("A lose");
//        clearInterval(myVar);
//        document.getElementById("demo1").innerHTML = t1;
//        document.getElementById("demo2").innerHTML = t1;
//    } else if (t1 ==0 && mover == 1){
//        alert("A Win");
//        clearInterval(myVar);
//        document.getElementById("demo1").innerHTML = t1;
//        document.getElementById("demo2").innerHTML = t1;
//    }
//
//
//}

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
     * Variables for set DISABLE button
     **/
     $scope.isDisabled={};

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
        requestUnPause();
        var timeCountDown = setInterval(function(){
            if($scope.countDown>0){
                $scope.countDown--;
                $scope.$apply();
            }else{
                clearInterval(timeCountDown);
                alert("Request time out");
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
        clearInterval(counter);
    };
    /**
     * ACCEPT | DECLINE Rep UnPause from opponent
     **/
    $scope.modalRepUnPause=function(rep){
        repUnPause(rep);
        clearInterval(counter);
    };
    /**
     * ACCEPT | DECLINE Rep Draw from opponent
     **/
    $scope.modalRepDraw=function(rep){
        repDrawGame(rep);
        clearInterval(counter);
    };
});
