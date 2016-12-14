import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class Search {
	
	public ArrayList<String> Baidu (String word) throws IOException{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		word = word.replaceAll(" ","+");
		HttpGet getWordMean = new HttpGet("http://dict.baidu.com/s?wd=" + word + "&ptype=english");
        CloseableHttpResponse response = httpClient.execute(getWordMean);
        String result = EntityUtils.toString(response.getEntity());
        response.close();
        Pattern searchMeanPattern = Pattern.compile("(?s)<div class=\"en-content\">.*?<div>.*?</div>.*?</div>");
        Matcher m1 = searchMeanPattern.matcher(result);
        if (m1.find()) {
            String means = m1.group();
            String means1=means;
            Pattern getChinese = Pattern.compile("<strong>(.*?)</strong>"); 
            Matcher m2 = getChinese.matcher(means);
            Pattern getChinese1 = Pattern.compile("<span>(.*?)</span>");
            Matcher m3 = getChinese1.matcher(means1);
            ArrayList arr=new ArrayList<String>();
            while (m2.find()&&m3.find()) {
                arr.add(m2.group(1)+m3.group(1));
            }
            return arr;
        } else {
            System.exit(0);
        }
        return null;
	}
	public String Bing(String word) throws IOException{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		word = word.replaceAll(" ","+");
		HttpGet getWordMean = new HttpGet("http://cn.bing.com/dict/search?q=" + word + "&go=%E6%90%9C%E7%B4%A2&qs=n&form=Z9LH5&pq="+word);
        CloseableHttpResponse response = httpClient.execute(getWordMean);
        String result = EntityUtils.toString(response.getEntity());
        response.close();
        Pattern searchMeanPattern = Pattern.compile("<meta name=\"description\" content=(.*?) />");
        Matcher m1 = searchMeanPattern.matcher(result);
        if (m1.find()) {
        	String means = m1.group();
        	 String[] m2=means.split("，");
             if(m2.length>=3) {
            	String arr="";
             	String[] p=m2[3].split("\"");
             	String[] m=p[0].split(" ");
             	for(int i=0;i*2<m.length;i++)
             		arr=arr+m[i*2]+" "+m[i*2+1];
             	return arr;
             }
         } else {
             System.exit(0);
         }
		return null;
	}
	public String Youdao(String word) throws IOException{
		CloseableHttpClient httpClient = HttpClients.createDefault();
        word = word.replaceAll(" ","+");
        HttpGet getWordMean = new HttpGet("http://dict.youdao.com/search?q=" + word + "&keyfrom=dict.index");
        CloseableHttpResponse response = httpClient.execute(getWordMean);
        String result = EntityUtils.toString(response.getEntity());
        response.close();
        Pattern searchMeanPattern = Pattern.compile("(?s)<div class=\"trans-container\">.*?<ul>.*?</div>");
        Matcher m1 = searchMeanPattern.matcher(result);

        if (m1.find()) {
            String means = m1.group();
            Pattern getChinese = Pattern.compile("(?m)<li>(.*?)</li>");
            Matcher m2 = getChinese.matcher(means);
            String arr="";
            while (m2.find()) {
                arr+=m2.group(1);
            }
            return arr;
        } else {
            System.out.println("未查找到释义."); 
            System.exit(0);
        }
		return null;
	}
	public String Jinshan(String word) throws IOException{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		word = word.replaceAll(" ","+");
		HttpGet getWordMean = new HttpGet("http://www.iciba.com/" + word);
        CloseableHttpResponse response = httpClient.execute(getWordMean);//取得返回的网页源码

        String result = EntityUtils.toString(response.getEntity());
        response.close();
        Pattern searchMeanPattern = Pattern.compile("(?s)<ul class=\"base-list switch_part\" class=\"\">.*?</ul>");
        Matcher m1 = searchMeanPattern.matcher(result);
        if (m1.find()) {
        	String means = m1.group();
            String means1=means;
            Pattern getChinese = Pattern.compile("(?s)<span class=\"prop\">(.*?)</span>"); //(?m)代表按行匹配
            Matcher m2 = getChinese.matcher(means);
            getChinese = Pattern.compile("(?s)<p>(.*?)</p>");
            Matcher m3 = getChinese.matcher(means1);
            getChinese = Pattern.compile("(?m)<span>(.*?)</span>");
            String arr="";
            while (m2.find()) {
            	arr=arr+m2.group(1);
            	if(m3.find()){
            		String str=m3.group(1);
            		Matcher m4 = getChinese.matcher(str);
            		while(m4.find()){
            			arr=arr+m4.group(1);
            		}
            	}
            	arr=arr+"\n";
            }
           
           return arr;
        }
        
        return null;
	}
}
