<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cờ tướng online - </title>
</head>
<body>
 	 <!-- jQuery Version 1.11.0 -->
    <script src="resources/js/jquery-1.11.0.js"></script>
	<script>
		
	  /**
		*	The code below is basic function to receive data from game's server
		*	@onOpen
		*	@onMessage
		*	@onClose
		*	0: OK ----- 1: ERROR
	    **/
		var ws=new WebSocket("ws://localhost:8080/chessgame/game");
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
					document.getElementById("chatlog").textContent += data[1] + "\n";
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
		function postMessage(){
			var to_client_id=2;
			var text=$("#msg").val();
			ws.send("CHAT-"+to_client_id+"-"+text);
			$("#msg").val("");
		}
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
	</script>
	 <textarea id="chatlog" readonly></textarea><br/>
       <input id="msg" type="text" />
       <button type="submit" id="sendButton" onClick="postMessage()">Send!</button>
</body>
</html>