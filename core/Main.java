package core;
<<<<<<< HEAD
import javax.swing.JOptionPane;
=======
>>>>>>> 21a396c9d4824c3f418bfbc15b9e744cc1c4fdab

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
