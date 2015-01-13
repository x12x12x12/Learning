<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="zh-CN" ng-app="myApp">
<head>
    <title>Chinese Chess</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="resources/extra/Style.css">
    <link rel="stylesheet" type="text/css" href="resources/extra/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/extra/bootstrap-theme.min.css">
    <link href="resources/css/sb-admin-2.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="resources/font-awesome-4.1.0/css/font-awesome.min.css" >
</head>
<body ng-controller="MyAppController">
<div id="main">
    <div id="debug" style="float:left;display:none"></div>
    <div style="float:left">
        <canvas id="board" width="460" height="510" ></canvas>
    </div>
    <div style="float:right">
        <div class="row">
            <div class="col-md-6">
                <div id="newGame" class="btn btn-primary btn-block" ng-disabled="isDisabled.btn_newGame" >New Game</div>
                <div id="pauseGame" class="btn btn-success btn-block" ng-disabled="isDisabled.btn_pause" >Pause</div>
                <div id="unpauseGame" class="btn btn-success btn-block" ng-disabled="isDisabled.btn_unpause" >UnPause</div>
                <div id="drawGame" class="btn btn-warning btn-block " ng-disabled="isDisabled.btn_draw" >Draw</div>
                <div id="loseGame" class="btn btn-danger btn-block" ng-disabled="isDisabled.btn_lose" >Lose</div>
                <div id="quitGame" class="btn btn-danger btn-block" ng-disabled="" >Quit match</div>
                <div id="listUser" class="btn btn-success btn-block" ng-click="showListUser()" >List User</div>
            </div>
            <div class="col-md-6" style="height: 200px; border: solid 1px; float: right;" ng-show="opponent" ng-animate="{show: 'fadeIn', hide:'fadeOut'}">
                <div style="font-weight: bold" align="center">Your Opponent</div>
                <img src="{{opponent.img_url}}" style="border-radius: 2px;" height="50%;"/>
                <div ng-bind="opponent.name" align="center" style="font-weight: bold"></div>
                <div ng-bind-template="Score: {{opponent.point}}" style="font-weight: bold"></div>
            </div>
        </div>
        <br/>
        <div class="row">
            <div class="conversation">
                <div class="title">
                    <div style="font-weight: bold;" ng-bind="titleOfChatConversation">
                    </div>
                </div>
                <div id="talks">
                    <div ng-repeat="message in messages track by $index" ng-bind="message.text" class="bubble"
                         ng-class="{'bubble--alt':message.yours==false}">
                    </div>
                </div>
                <div class="send-message">
                    <input type="text" class="write-message" placeholder="Write Message ..." ng-model="yourMessage"
                           ng-keypress="sendMessage($event)">
                </div>
            </div>
        </div>
    </div>
</div>


<!--------------START: Popup show user online---------------------->
<div class="modal fade" id="modalListUser" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="height: 100vh; width: 150%; margin-left: -22%; overflow-y: scroll;" >
            <div class="modal-header" style="background-color: #5bc0de;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">List User Online</h4>
            </div>
            <div class="modal-body">
                <div ng-repeat="user in userOnline" class="col-md-2 userInListUsers">
                    <br/>
                    <img src="{{user.img_url}}" height="80px" style="border-radius: 30px; margin-left: 10%;"/><br/>
                    <span ng-bind="user.name"></span><br/>
                    <span ng-bind-template="Score: {{user.point}}"></span>
                    <button class="btn btn-success" style="width: 100%; border-radius: 10px;" ng-click="addFriend()">
                        Add Friend
                    </button>
                    <button class="btn btn-primary" style="width: 100%; margin-top: 5%; border-radius: 10px;" ng-click="challengeUser(user)">
                        Chalenge
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--------------END :Popup show user online---------------------->

<!--------------START: Popup for Waiting opponent accept your challenge---------------------->
<div class="modal fade" id="modalWaitingAcceptChallenge" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" align="center" style="height: 20vh; width: 80vh; margin-top: 40vh;">
            <div class="modal-header" style="background-color: #5cb85c;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">
                    Waiting For Opponent Accept Your Challenge
                </h4>
            </div>
            <div class="modal-body">
                <div align="center">
                    <button class="btn btn-lg btn-danger" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--------------END :Popup for Waiting opponent accept your challenge---------------------->

<!--------------START: Popup for Accept challenge from opponent---------------------->
<div class="modal fade" id="modalAcceptChallenge" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     data-backdrop="static" data-keyboard="false" >
    <div class="modal-dialog">
        <div class="modal-content" align="center" style="height: 20vh; width: 80vh; margin-top: 40vh;">
            <div class="modal-header" style="background-color: #5cb85c;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">
                    Wanna play with .. ?
                </h4>
            </div>
            <div class="modal-body">
                <div align="center">
                    <button class="btn btn-lg btn-primary" data-dismiss="modal" style="margin-right: 50px;"ng-click="acceptChallenge()">Accept</button>
                    <button class="btn btn-lg btn-danger" data-dismiss="modal" ng-click="declineChallenge()">Decline</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--------------END :Popup for Accept challenge from opponent---------------------->

<!--------------START: Popup for REP NEW GAME---------------------->
<div class="modal fade" id="modalRepNewGame" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     data-backdrop="static" data-keyboard="false" >
    <div class="modal-dialog">
        <div class="modal-content" align="center" style="height: 20vh; width: 80vh; margin-top: 40vh;">
            <div class="modal-header" style="background-color: #5cb85c;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">
                    Request start new game -- <span id="timerREPNEWGAME"></span>
                </h4>
            </div>
            <div class="modal-body">
                <div align="center">
                    <button class="btn btn-lg btn-primary" data-dismiss="modal" style="margin-right: 50px;"ng-click="modalRepNewGame(0)">Accept</button>
                    <button class="btn btn-lg btn-danger" data-dismiss="modal" ng-click="modalRepNewGame(1)">Decline</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--------------END : Popup for REP NEW GAME---------------------->

<!--------------START: Popup for REP PAUSE---------------------->
<div class="modal fade" id="modalRepPause" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     data-backdrop="static" data-keyboard="false" >
    <div class="modal-dialog">
        <div class="modal-content" align="center" style="height: 20vh; width: 80vh; margin-top: 40vh;">
            <div class="modal-header" style="background-color: #5cb85c;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">
                    Request pause game -- <span id="timerREQPAUSE"></span>
                </h4>
            </div>
            <div class="modal-body">
                <div align="center">
                    <button class="btn btn-lg btn-primary" data-dismiss="modal" style="margin-right: 50px;"ng-click="modalRepPause(0)">Accept</button>
                    <button class="btn btn-lg btn-danger" data-dismiss="modal" ng-click="modalRepPause(1)">Decline</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--------------END : Popup for REP PAUSE---------------------->

<!--------------START: Popup for Waiting opponent  REP PAUSE---------------------->
<div class="modal fade" id="modalWaitingRepPause" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" align="center" style="height: 20vh; width: 80vh; margin-top: 40vh;">
            <div class="modal-header" style="background-color: #5cb85c;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">
                     Waiting Accept Pause From Opponent <span id="timerREPPASUE"></span>
                </h4>
            </div>
            <div class="modal-body">
                <div align="center">
                    <button class="btn btn-lg btn-danger" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--------------END :Popup for Waiting opponent REP PAUSE---------------------->

<!--------------START: Popup for REP UNPAUSE---------------------->
<div class="modal fade" id="modalRepUnPause" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     data-backdrop="static" data-keyboard="false" >
    <div class="modal-dialog">
        <div class="modal-content" align="center" style="height: 20vh; width: 80vh; margin-top: 40vh;">
            <div class="modal-header" style="background-color: #5cb85c;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">
                    Resume game ? <span id="timerREQUNPASUE"></span>
                </h4>
            </div>
            <div class="modal-body">
                <div align="center">
                    <button class="btn btn-lg btn-primary" data-dismiss="modal" style="margin-right: 50px;"ng-click="modalRepUnPause(0)">Accept</button>
                    <button class="btn btn-lg btn-danger" data-dismiss="modal" ng-click="modalRepUnPause(1)">Decline</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--------------END : Popup for REP PAUSE---------------------->

<!--------------START: Popup for Waiting opponent  REP UNPAUSE---------------------->
<div class="modal fade" id="modalWaitingRepUnPause" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" align="center" style="height: 20vh; width: 80vh; margin-top: 40vh;">
            <div class="modal-header" style="background-color: #5cb85c;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">
                    Waiting For Opponent REP UNPAUSE
                </h4>
            </div>
            <div class="modal-body">
                <div align="center">
                    <button class="btn btn-lg btn-danger" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--------------END :Popup for Waiting opponent REP PAUSE---------------------->

<!--------------START: Popup for Waiting opponent  REP DRAW---------------------->
<div class="modal fade" id="modalWaitingRepDraw" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" align="center" style="height: 20vh; width: 80vh; margin-top: 40vh;">
            <div class="modal-header" style="background-color: #5cb85c;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">
                    Waiting For Opponent REP DRAW
                </h4>
            </div>
            <div class="modal-body">
                <div align="center">
                    <button class="btn btn-lg btn-danger" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--------------END :Popup for Waiting opponent REP DRAW---------------------->

<!--------------START: Popup for REP DRAW---------------------->
<div class="modal fade" id="modalRepDraw" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     data-backdrop="static" data-keyboard="false" >
    <div class="modal-dialog">
        <div class="modal-content" align="center" style="height: 20vh; width: 80vh; margin-top: 40vh;">
            <div class="modal-header" style="background-color: #5cb85c;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">
                    Request draw game --
                </h4>
            </div>
            <div class="modal-body">
                <div align="center">
                    <button class="btn btn-lg btn-primary" data-dismiss="modal" style="margin-right: 50px;"ng-click="modalRepDraw(0)">Accept</button>
                    <button class="btn btn-lg btn-danger" data-dismiss="modal" ng-click="modalRepDraw(1)">Decline</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--------------END : Popup for REP DRAW---------------------->

<!--------------START: Popup for YOU REQ LOOSE---------------------->
<div class="modal fade" id="modalYouLooseGame" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog" align="center">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                        <h4 class="modal-title" id="myModalLabel">Finished Game</h4>
                    </div>
                    <div class="modal-body">
                        <img src="{{myProfile.img_url}}" style="border-radius: 5px;>
                        <span style="margin-left: 20px;">Score: -10 </span>
                        <span style="margin-left: 50px;">Email: {{myProfile.email}} </span><br><br>
                        <img src="{{opponent.img_url}}" style="border-radius: 5px;>
                        <span style="margin-left: 20px;">Score: +10 </span>
                        <span style="margin-left: 50px;">Email: {{opponent.email}} </span><br>
                    </div>

                </div>
            </div>
</div>
<!--------------END : Popup for YOU REQ LOOSE---------------------->

<!--------------START: Popup for OPPONENT REQ LOOSE---------------------->
<div class="modal fade" id="modalOpponentLooseGame" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog" align="center">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                        <h4 class="modal-title" id="myModalLabel">Finished Game</h4>
                    </div>
                    <div class="modal-body">
                        <img src="{{myProfile.img_url}}" style="border-radius: 5px;>
                        <span style="margin-left: 20px;">Score: +10 </span>
                        <span style="margin-left: 50px;">Email: {{myProfile.email}} </span><br><br>
                        <img src="{{opponent.img_url}}" style="border-radius: 5px;>
                        <span style="margin-left: 20px;">Score: -10 </span>
                        <span style="margin-left: 50px;">Email: {{opponent.email}} </span><br>
                    </div>

                </div>
            </div>
</div>
<!--------------END : Popup for OPPONENT REQ LOOSE---------------------->


<!-- <div id="debug" style="position:absolute;top:0px;left:0px;width:400px;"></div>---->
<script src="resources/extra/Board.js"></script>
<script src="resources/extra/Chess.js"></script>
<script type='text/javascript'>
    document.getElementById("newGame").onclick=function(){requestNewGame()};
    document.getElementById("pauseGame").onclick=function(){requestPause()};
    document.getElementById("drawGame").onclick=function(){requestDrawGame()};
    document.getElementById("loseGame").onclick=function(){
        acceptLose();modalFinished
        $('#modalYouLooseGame').modal({
                backdrop: 'static',
                keyboard: false
            });
        $('#modalYouLooseGame').modal('show');
    };
    document.getElementById("quitGame").onclick=function(){acceptLose()};
    var user_data={
        "name": "${account.name}",
        "email":"${account.email}",
        "img_url":"${account.img_url}",
        "point": "${account.point}"
    };
</script>
<script src="resources/extra/jquery-1.11.0.min.js"></script>
<script src="resources/extra/angular.min.js"></script>
<script src="resources/extra/bootstrap.min.js"></script>
<script src="resources/extra/soundmanager2.js"></script>
<script src="resources/extra/control.js"></script>
</body>
</html>
