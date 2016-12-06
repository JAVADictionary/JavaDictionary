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
	public ArrayList Bing(String word) throws IOException{
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
        	 String[] m2=means.split("��");
             System.out.println("����:");
             if(m2.length>=3) {
            	ArrayList arr=new ArrayList<String>();
             	String[] p=m2[3].split("\"");
             	String[] m=p[0].split(" ");
             	for(int i=0;i*2<m.length;i++)
             		arr.add(m[i*2]+" "+m[i*2+1]);
             	return arr;
             }
         } else {
             System.exit(0);
         }
		return null;
	}
	public ArrayList Youdao(String word) throws IOException{
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
            ArrayList<String> arr=new ArrayList<String>();
            while (m2.find()) {
                arr.add(m2.group(1));
            }
            return arr;
        } else {
            System.out.println("δ���ҵ�����.");
            System.exit(0);
        }
		return null;
	}
}
