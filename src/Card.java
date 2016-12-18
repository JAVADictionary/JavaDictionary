import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class Card {
	String Accepter;
	ImageIcon img;
	public Card(String accepter,ImageIcon img){
		Accepter=accepter;
		this.img=img;
	}
	public String getAccepter(){
		return Accepter;
	}
	public ImageIcon getImg(){
		return img;
	}
}
