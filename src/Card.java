
public class Card {
	String Accepter;
	String word;
	String Seeder;
	public Card(String accepter,String seeder,String word){
		Accepter=accepter;
		this.word=word;
		this.Seeder=seeder;
	}
	public String getAccepter(){
		return Accepter;
	}
	public String getSeeder(){
		return Seeder;
	}
	public String getWord(){
		return word;
	}
}
