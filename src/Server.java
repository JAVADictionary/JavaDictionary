import javax.swing.*;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server {
	ArrayList<Card> Cardlist=new ArrayList<Card>();
	Mysql sql=new Mysql();
	HashMap<String,ObjectOutputStream> map=new HashMap<>();
	public static void main(String[] args) {
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
		//Search websearch=new Search();
		boolean login=false;
		ObjectInputStream input;
		ObjectOutputStream output;
		public Control(Socket socket) throws IOException{
			input=new ObjectInputStream(socket.getInputStream());
			output=new ObjectOutputStream(socket.getOutputStream());
		}
		@Override
		public void run(){
			try{
				while(true){
					String order=input.readUTF();
					if(order.equals("login")){
						user=input.readUTF();
						String pwd=input.readUTF();
						System.out.println(user+" "+pwd);
						output.writeUTF("login");
						if(sql.TestUser(user, pwd)){
							map.put(user, output);
							login=true;
							output.writeUTF(user);
							System.out.println("true");
							output.writeUTF("true");
							ArrayList<String> all=sql.AllUser();
							output.writeUTF((all.size()-1)+"");
							for(int i=0;i<all.size();i++){
								if(!all.get(i).equals(user))
									output.writeUTF(all.get(i));
							}
							output.flush();
							int num=Cardlist.size();
							for(int i=0;i<num;i++){
								if(Cardlist.get(i).getAccepter().equals(user)){
									output.writeUTF("card");
									output.writeObject(Cardlist.get(i).getImg());
									Cardlist.remove(i);
									output.flush();
									i--;
									num--;
								}
							}
						}
						else
							output.writeUTF("false");
						output.flush();
					}
					else if(order.equals("logout")){
						map.remove(user);
					}
					else if(order.equals("register")){
						String name=input.readUTF();
						String pwd=input.readUTF();
						output.writeUTF("register");
						System.out.println(name+" "+pwd);
						if(sql.insertUser(name, pwd))
							output.writeUTF("true");
						else
							output.writeUTF("false");
						output.flush();
					}
					else if(order.equals("search")){
						String word=input.readUTF();
						output.writeUTF("search");
						output.writeUTF(word);
						ArrayList<String> Inturn=sql.search(word);
						for(int i=0;i<Inturn.size();i++){
							output.writeUTF(Inturn.get(i));
						}
						output.flush();
					}
					else if(order.equals("like")){
						String word=input.readUTF();
						String web=input.readUTF();
						System.out.println(word+" "+web);
						sql.like(word, web);
					}
					else if(order.equals("card")){
						String acc=input.readUTF();
						ImageIcon img;
						try {
							img = (ImageIcon) input.readObject();
							ObjectOutputStream Seedto=map.get(acc);
							if(Seedto==null){
								Card c=new Card(acc,img);
								Cardlist.add(c); 
							}
							else{
								Seedto.writeUTF("card");
								Seedto.writeObject(img);
							}
						} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}catch(IOException | SQLException e){
				System.err.println(e);
			}
		}
	}
}
