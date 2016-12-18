import java.util.*;
import java.util.Map.Entry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class Mysql {
	Connection conn=null;
	Statement stmt;
	public Mysql(){
		String url="jdbc:mysql://localhost:3306/Dictionary";
		Properties info=new Properties();
		info.put("user", "root");
		info.put("password", "nju");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection(url,info);
			stmt=conn.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	boolean TestUser(String name,String pwd) throws SQLException{
		String sql="select pwd from User where name='"+name+"';";
		ResultSet rs=stmt.executeQuery(sql);
		if(rs.next()&&pwd.equals(rs.getString("pwd"))){
			return true;
		}
		return false;
	}
	public ArrayList<String> AllUser() throws SQLException{
		String sql="select name from User;";
		ResultSet rs=stmt.executeQuery(sql);
		ArrayList<String> tmp=new ArrayList<String>();
		while(rs.next()){
			tmp.add(rs.getString("name"));
		}
		return tmp;
	}
	synchronized boolean insertUser(String name,String pwd) throws SQLException{
		String sql="select * from User where name='"+name+"';";
		ResultSet rs=stmt.executeQuery(sql);
		if(rs.next())
			return false;
		sql="insert into User(name,pwd) values('"+name+"','"+pwd+"');";
		System.out.println(sql);
		stmt.execute(sql);
		return true;
	}
	synchronized void like(String word,String web) throws SQLException{
		String sql="update wordlist set wordlist."+web+"=wordlist."+web+"+1 where wordlist.word='"+word+"';";
		stmt.execute(sql);
	}
	ArrayList<String> search(String word) throws SQLException{
		String sql="select * from wordlist where wordlist.word='"+word+"';";
		ResultSet rs=stmt.executeQuery(sql); 
		ArrayList<String> result=new ArrayList<String>();
		int jinshan=0;
		int bing=0;
		int youdao=0;
		boolean ex=false;
		while(rs.next()){
			jinshan=rs.getInt("jinshan");
			bing=rs.getInt("bing");
			youdao=rs.getInt("youdao");
			ex=true;
		}
		if(!ex){
			sql="insert into wordlist(word,jinshan,bing,youdao) values('"+word+"',0,0,0)";
			stmt.execute(sql);
		}
		if(jinshan>bing){
			if(jinshan>youdao){
				result.add("jinshan");
				if(bing>youdao){
					result.add("bing");
					result.add("youdao");
				}
				else{
					result.add("youdao");
					result.add("bing");
				}
			}
			else{
				result.add("youdao");
				result.add("jinshan");
				result.add("bing");
			}
		}
		else{
			if(bing>youdao){
				result.add("bing");
				if(jinshan>youdao){
					result.add("jinshan");
					result.add("youdao");
				}
				else{
					result.add("youdao");
					result.add("jinshan");
				}
			}
			else{
				result.add("youdao");
				result.add("bing");
				result.add("jinshan");
			}
		}
		return result;
	} 
}
