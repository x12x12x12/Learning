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
	   private static HashMap<String,String> list_user = new HashMap<String,String>();
	   
	   @OnOpen
	   public void onOpen(Session session) throws IOException{
		   System.out.println(session.getId() + " : connected ... ");
		   session.getBasicRemote().sendText("Session id "+session.getId());
		   list.add(session);
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
	   public void onMessage(Session session,String data_receive) throws IOException,SQLException{
		   String[] data=data_receive.split("-");
		   switch (data[0]) {
				case "REG": 
					/**
					 *  Every client whom connect to server have to post their email to the socket server
					 *  Then save user's email into list_user
					 *  List<SessionId,EmailPlayerConnect> (String,String)
					 */
					list_user.put(session.getId(),data[1]);
					break;
				case "REQHANDSHAKE":
					/**
					 * 
					 * 
					 */
					break;
				case "REPHANDSHAKE":
					/**
					 * Reply the handshake to player who send the request.
					 * Data    :  HANDSHAKE-BOOL-ID
					 *  - BOOL 
					 * 		+ Accept  : 0
					 * 		+ Decline : 1
					 *  - ID 	: Session id of player to response the request handshake
					 */
					break;
				case "CHAT":
					/**
					 * Data : CHAT-ID-MESSAGE
					 * 	- ID 		: Session id player receive the message
					 * 	- MESSAGE   : Message data
					 */
						String send_to=data[1];
						try {
							System.out.println("Client nhận message:"+send_to);
							System.out.println("Nội dung message :"+data[2]);
//							session_to.getBasicRemote().sendText("CHAT-|-"+data[2]);
						} catch (Exception ex) {
							System.out.println("Client không online");
						}
					break;
			
				default:
					break;
				}
	   } 
}
