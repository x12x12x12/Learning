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
		   if(data[0].equalsIgnoreCase("REG")){
			   /**
				*  Every client whom connect to server have to post their email to the socket server
				*  Then save user's email into list_user
				*  List<SessionId,EmailPlayerConnect> (String,String)
				*  Client send : REG-ID_Client
				*/
			   list_user.put(session.getId(),data[1]);
		   }else if(data[0].equalsIgnoreCase("REQHANDSHAKE")){
			   /**
				* User A wanna play with B
				* User A : Send "REQHANDSHAKE-ID_B" to server
				* Server : Find ID_B in list_user -> session of user's B -> send to B
				* User B : Receive "REQHANDSHAKE-|-ID_A"
				*/
			   checkAndSendMsgToUser(data, session, "REQHANDSHAKE-|-");
		   }else if(data[0].equalsIgnoreCase("REPHANDSHAKE")){
			   /**
				* Reply the handshake to player who send the request.
				* User A : Send "REPHANDSHAKE-ID_B-BOOL"
				*  - BOOL
				* 		+ Accept  : 0
				* 		+ Decline : 1
				*  - ID 	: Session id of player to response the request handshake
				* Server : Find ID_B in list_user -> session of user's B -> send to B
				* User B : Receive "REPHANDSHAKE-BOOL-ID_A"
				*/
			   checkAndSendMsgToUser(data,session,"REPHANDSHAKE-|-"+data[1]+"-|-");
		   }else if(data[0].equalsIgnoreCase("REQPAUSE")){
			   /**
				* User A request pause game to user B
				* User A : Send "REQPAUSE-ID_B"
				* Server : Find ID_B in list_user -> session of user's B -> send to B
				* User B : Receive "REQPAUSE-ID_A"
				*/
			   checkAndSendMsgToUser(data,session,"REQPAUSE-|-");
		   }else if(data[0].equalsIgnoreCase("REPPAUSE")){

		   }else if(data[0].equalsIgnoreCase("CHAT")){
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
		   }
//		   switch (data[0]) {
//				case "REG":
					/**
					 *  Every client whom connect to server have to post their email to the socket server
					 *  Then save user's email into list_user
					 *  List<SessionId,EmailPlayerConnect> (String,String)
					 */
//					list_user.put(session.getId(),data[1]);
//					break;
//				case "REQHANDSHAKE":
//					/**
//					 *
//					 *
//					 */
//					break;
//				case "REPHANDSHAKE":
					/**
					 * Reply the handshake to player who send the request.
					 * Data    :  HANDSHAKE-BOOL-ID
					 *  - BOOL
					 * 		+ Accept  : 0
					 * 		+ Decline : 1
					 *  - ID 	: Session id of player to response the request handshake
					 */
//					break;
//				case "CHAT":
					/**
					 * Data : CHAT-ID-MESSAGE
					 * 	- ID 		: Session id player receive the message
					 * 	- MESSAGE   : Message data
					 */
//						String send_to=data[1];
//						try {
//							System.out.println("Client nhận message:"+send_to);
//							System.out.println("Nội dung message :"+data[2]);
////							session_to.getBasicRemote().sendText("CHAT-|-"+data[2]);
//						} catch (Exception ex) {
//							System.out.println("Client không online");
//						}
//					break;
//
//				default:
//					break;
//				}
	   }
	public void checkAndSendMsgToUser(String[] data,Session session,String msg) throws IOException{
		if(list_user.containsValue(data[1])){
			String session_id="";
			for (String key : list_user.keySet()){
				if(list_user.get(key)==data[1]){
					session_id=key;
				}
			}
			for(Session sess : list){
				if(sess.getId().equalsIgnoreCase(session_id)){
					String id_a=list_user.get(session.getId());
					sess.getBasicRemote().sendText(msg+id_a);
				}
			}
			return ;
		}
		session.getBasicRemote().sendText("RESULT-|-User không tồn tại");
	}
}
