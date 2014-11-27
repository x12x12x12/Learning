
var myApp = angular.module('myApp', []);

myApp.controller('MyAppController', function ($scope,$http) {

	/**
	*	The code below is basic function to receive data from game's server
	*	@onOpen
	*	@onMessage
	*	@onClose
	*	0: OK ----- 1: ERROR
    **/
	var ws=new WebSocket("ws://localhost:8080/cotuong/game");
	ws.onopen=function(message){
		var id_player=1;
		ws.send("REG-"+id_player);
	};
	ws.onmessage=function(message){
		var data=message.data.split("-|-");
		switch(data[0]){
			case "HANDSHAKE":
				var id_requestHandShake=data[1];
				
				break;
			case "ACCEPT_HANDSHAKE":
				// đóng lại 
				break;
			case "PAUSE":
				var id_requestPause=1;
				
				break;
			case "ACCEPT_PAUSE":
				var accept=data[1];
				if(accept=="1"){
					console.log("continue");
				}
				break;
			case "CHAT":
				console.log(data[1]);
				break;
			case "LOGIN":
				break;
			case "LOGOUT":
				break;
		}
	}
	ws.onclose=function(message){
		ws.close();
	}
	/**
	 *
	 *
	 *
	 **/
	function acceptHandShake(){
		var id_requestHandshake=1;
		ws.send("HANDSHAKE-0-"+id_requestHandshake);
	}
	function declineHandShake(){
		var id_requestHandshake=1;
		ws.send("HANDSHAKE-1-"+id_requestHandshake);
	}
	function requestPause(){
		var id_requestPause=1;
		ws.send("PAUSE-1-"+id_requestHandshake);
	}
	function acceptLose(){
		
	}
	function requestDrawGame(){
		
	}
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
                /**
                 * 
                 */
                var to_client_id=2;
        		ws.send("CHAT-"+to_client_id+"-"+$scope.yourMessage);
                
        		/**
        		 * 
        		 */
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
