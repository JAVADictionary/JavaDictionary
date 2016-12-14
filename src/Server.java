import javax.swing.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server {
	ArrayList<Card> Cardlist=new ArrayList<Card>();

	Mysql sql=new Mysql();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Server();
	}
	public Server(){
		 ExecutorService executor=Executors.newCachedThreadPool();
		 ServerSocket serversocket;
		 Socket socket;
		 try{
			 serversocket=new ServerSocket(8000);
			 while(true){
				 socket=serversocket.accept();
				 System.out.println("新的用户连接");
				 Control c=new Control(socket);
				 executor.execute(c);
			 }
			 
		 }catch(IOException e){
			 System.err.println(e);
		 }
	}
	public class Control implements Runnable{
		String user;
		Search websearch=new Search();
		boolean login=false;
		DataInputStream input;
		DataOutputStream output;
		public Control(Socket socket) throws IOException{
			input=new DataInputStream(socket.getInputStream());
			output=new DataOutputStream(socket.getOutputStream());
		}
		@Override
		public void run(){
			try{
				while(true){
					if(login&&Cardlist.size()>0){
						for(int i=0;i<Cardlist.size();i++){
							if(user.equals(Cardlist.get(i).getAccepter())){
								output.writeUTF("card");
								output.writeUTF(Cardlist.get(i).getSeeder());
								output.writeUTF(Cardlist.get(i).getWord());
								Cardlist.remove(Cardlist.get(i));
							}
						}
					}
					String order=input.readUTF();
					if(order.equals("login")){
						user=input.readUTF();
						String pwd=input.readUTF();
						System.out.println(user+" "+pwd);
						if(sql.TestUser(user, pwd)){
							login=true;
							System.out.println("true");
							output.writeUTF("true");
						}
						else
							output.writeUTF("false");
						output.flush();
					}
					else if(order.equals("register")){
						String name=input.readUTF();
						String pwd=input.readUTF();
						System.out.println(name+" "+pwd);
						if(sql.insertUser(name, pwd))
							output.writeUTF("true");
						else
							output.writeUTF("false");
						output.flush();
					}
					else if(order.equals("search")){
						String op=input.readUTF();
						String word=input.readUTF();
						System.out.println(word);
						if(op.equals("card")){
							String accepter=input.readUTF();
							Card cd=new Card(accepter,user,word);
							System.out.println(accepter+" "+user+" "+word);
							Cardlist.add(cd);
						}
						String Jinshan=websearch.Jinshan(word);
						String bing=websearch.Bing(word);
						String youdao=websearch.Youdao(word);
						ArrayList<String> Inturn=sql.search(word);
						for(int i=0;i<Inturn.size();i++){
							if(Inturn.get(i).equals("jinshan")){
								output.writeUTF("jinshan");
								output.writeUTF(Jinshan);
							} 
							else if(Inturn.get(i).equals("bing")){
								output.writeUTF("bing");
								output.writeUTF(bing);
							}
							else{
								output.writeUTF("youdao");
								output.writeUTF(youdao); 
							}
						}
						output.flush();
					}
					else if(order.equals("like")){
						String word=input.readUTF();
						String web=input.readUTF();
						System.out.println(word+web);
						sql.like(word, web);
					}
					
				}
			}catch(IOException | SQLException e){
				System.err.println(e);
			}
		}
	}
}
