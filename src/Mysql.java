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
		info.put("password", "");
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
		String sql="select pwd from User where name="+name+";";
		ResultSet rs=stmt.executeQuery(sql);
		if(rs.next()&&pwd.equals(rs.getString(1))){
			return true;
		}
		return false;
	}
	synchronized boolean insertUser(String name,String pwd) throws SQLException{
		String sql="select * from User where name="+name+";";
		ResultSet rs=stmt.executeQuery(sql);
		if(rs.next())
			return false;
		sql="insert into User(name,pwd) values("+name+","+pwd+");";
		stmt.executeQuery(sql);
		return true;
	}
	synchronized void like(String word,String web) throws SQLException{
		String sql="update wordlist set wordlist."+web+"=wordlist."+web+"+1 where wordlist.word="+word+";";
		stmt.executeQuery(sql);
	}
	ArrayList<String> search(String word) throws SQLException{
		String sql="select * from wordlist where wordlist.word="+word+";";
		ResultSet rs=stmt.executeQuery(sql);
		ArrayList<String> result=new ArrayList<String>();
		Map<String,Integer> map=new HashMap<String, Integer>();
		while(rs.next()){
			int baidu=rs.getInt(2);
			map.put("baidu",baidu);
			int bing=rs.getInt(3);
			int youdao=rs.getInt(4);
			map.put("bing",bing);
			map.put("youdao",youdao);
		}
		List<Map.Entry<String, Integer>> list=new ArrayList<Map.Entry<String,Integer>>();
		Collections.sort(list,new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> arg0,
					Entry<String, Integer> arg1) {
				// TODO Auto-generated method stub
				return arg1.getValue().compareTo(arg0.getValue());
			}
		});
		for(Map.Entry<String, Integer> e:list){
			result.add(e.getKey());
		}
		return result;
	}
	 
}
