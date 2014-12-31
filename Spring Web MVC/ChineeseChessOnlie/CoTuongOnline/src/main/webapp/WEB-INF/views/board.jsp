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
                        <div id="newGame" class="button" >New Game</div>
                        <div id="restore" class="button" >Turn back</div>
                        <div id="listUser" class="button" ng-click="showListUser()" >List User</div>
                        <div class="info" >Time：<span id="TimeCost">0</span></div>
                        <div class="info" >Depth：<span id="depth">0</span></div>
                    </div>
                    <div class="col-md-6" style="height: 200px; border: solid 1px; float: right;" ng-hide="opponent==null">
                        <div style="font-weight: bold" align="center">Your Opponent</div>
                        <img src="opponent.img_url" style="border-radius: 2px;" height="50%;"/>
                        <div ng-bind="opponent.name" align="center"></div>
                        <div ng-bind-template="Score: {{opponent.point}}"></div>
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
                                   ng-keypress="sendMessage()">
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
                            Accept Challenge From .....
                        </h4>
                    </div>
                    <div class="modal-body">
                        <div align="center">
                            <button class="btn btn-lg btn-primary" data-dismiss="modal" style="margin-right: 50px;"ng-click="acceptChallenge()">Accept</button>
                            <button class="btn btn-lg btn-danger" data-dismiss="modal" ng-click="disAcceptChallenge()">Disaccept</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--------------END :Popup for Accept challenge from opponent---------------------->
        <div><input type="hidden"/></div>
        <div id="enemy_data">


        </div>
        

        
        <div id="debug" style="position:absolut;top:0px;left:0px;width:400px;"></div>
        <script src="resources/extra/Board.js"></script>
        <script src="resources/extra/Chess.js"></script>
        <script type='text/javascript'>
            var chessGame=new ChessGame("board");
            chessGame.init();
            document.getElementById("newGame").onclick=function(){chessGame.init()};
            document.getElementById("restore").onclick=function(){chessGame.restore()};
        </script>
        <script src="resources/extra/jquery-1.11.0.min.js"></script>
  		<script src="resources/extra/angular.min.js"></script>
        <script src="resources/extra/bootstrap.min.js"></script>
        <script src="resources/extra/soundmanager2.js"></script>
        <script type="text/javascript">
            var user_data={
                "name": "${account.name}",
                "email":"${account.email}",
                "img_url":"${account.img_url}",
                "point": "${account.point}"
            };
        </script>
        <script src="resources/extra/control.js"></script>
</body>
</html>