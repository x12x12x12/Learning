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
		var ws=new WebSocket("ws://localhost:8080/chessgame/game");
		ws.onopen=function(message){
			var id_player=1;
			ws.send("REG"+id_player);
		};
		ws.onmessage=function(message){
			var data=message.data.split("-|-");
			switch(data[0]){
				case "PAUSE":
					alert('Pause');
					break;
				case "CHAT":
					document.getElementById("chatlog").textContent += data[1] + "\n";
					break;
				case "LOGIN":
					break;
			}
		}
		ws.onclose=function(message){
			ws.close();
		}
		function postMessage(){
			var to_client_id=2;
			var text=$("#msg").val();
			ws.send("CHAT-"+to_client_id+"-"+text);
			$("#msg").val("");
		}
	</script>
	 <textarea id="chatlog" readonly></textarea><br/>
       <input id="msg" type="text" />
       <button type="submit" id="sendButton" onClick="postMessage()">Send!</button>
</body>
</html>