package com.cotuong.app;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;


@javax.websocket.server.ServerEndpoint(value="/game")
public class ServerEndPoint {
	
	   private static Set<Session> list = Collections.synchronizedSet(new HashSet<Session>());
	   private static HashMap<String,Session> list_user = new HashMap<String,Session>();
	   
	   @OnOpen
	   public void onOpen(Session session) throws IOException{
		   System.out.println(session.getId() + " : connected ... ");
		   session.getBasicRemote().sendText("Session id "+session.getId());
		   list.add(session);
		   list_user.put(session.getId(),session);
	   }
	   
	   @OnClose
	   public void onClose(Session session, CloseReason closeReason) throws IOException{
		   System.out.printf("Session %s closed because of %s", session.getId(), closeReason.getReasonPhrase());
		   list.remove(session);
		   for (Session session2 : list) {
			   session2.getBasicRemote().sendText("LOGOUT-|-"+session.getId());
		   }
	   }
	   
	   @OnMessage
	   public void onMessage(Session session,String msg) throws IOException,SQLException{
		   String[] data=msg.split("-");
		   System.out.println(msg);
		   switch (data[0]) {
				case "CHAT":
						String send_to=data[1];
						Session session_to=list_user.get(send_to);
						System.out.println(session_to.getId());
						System.out.println(data[2]);
						session_to.getBasicRemote().sendText("CHAT-|-"+data[2]);
					break;
				case "":
					
					break;
				case "REG": 
					break;
				default:
					break;
				}
	   } 
}
