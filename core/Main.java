package core;
import javax.swing.JOptionPane;

import core.Control;

public class Main {

	public static void main(String[] args) {
		Control c = null;
		try{
		c = new Control();
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, e.toString());
			c = new Control();
		}
	}
}
