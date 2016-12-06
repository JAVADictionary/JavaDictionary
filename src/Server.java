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
			 socket=serversocket.accept();
			 System.out.println("�µ��û�����");
			 Control c=new Control(socket);
			 executor.execute(c);
			 
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
						if(sql.TestUser(user, pwd)){
							login=true;
							output.writeUTF("true");
						}
						else
							output.writeUTF("false");
					}
					else if(order.equals("register")){
						String name=input.readUTF();
						String pwd=input.readUTF();
						if(sql.insertUser(name, pwd))
							output.writeUTF("true");
						else
							output.writeUTF("false");
					}
					else if(order.equals("search")){
						String word=input.readUTF();
						ArrayList<String> baidu=websearch.Baidu(word);
						ArrayList<String> bing=websearch.Bing(word);
						ArrayList<String> youdao=websearch.Youdao(word);
						ArrayList<String> Inturn=sql.search(word);
						for(int i=0;i<Inturn.size();i++){
							if(Inturn.get(i).equals("baidu")){
								;
							}
							else if(Inturn.get(i).equals("bing")){
								;
							}
							else{
								;
							}
						}
					}
					else if(order.equals("card")){
						String word=input.readUTF();
						String accepter=input.readUTF();
						Card cd=new Card(accepter,user,word);
						Cardlist.add(cd);
					}
					output.flush();
				}
			}catch(IOException | SQLException e){
				System.err.println(e);
			}
		}
	}
}
