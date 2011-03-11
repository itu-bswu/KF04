import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class View extends JFrame{
	
	// felter
	JComponent canvas;

	public View(String header){
		createCanvas();
		createContent();
		
		this.pack();
		this.setVisible(true);
	}

	// Emils
	private void createCanvas() {
		// TODO Auto-generated method stub
		
	}

	// Filips
	private void createContent() {
		Container outer = this.getContentPane();
		outer.setLayout(new GridLayout(1,2));
		outer.add(canvas);
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(2,1));
		outer.add(menuPanel);
		JPanel navigationPanel = new JPanel();
		menuPanel.add(navigationPanel);
		navigationPanel.setLayout(new BorderLayout());
		navigationPanel.add(new JButton("^"),BorderLayout.NORTH);
		navigationPanel.add(new JButton(">"),BorderLayout.EAST);
		navigationPanel.add(new JButton("v"),BorderLayout.SOUTH);
		navigationPanel.add(new JButton("<"),BorderLayout.WEST);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(2,1));
		centerPanel.add(new JButton("+"));
		centerPanel.add(new JButton("-"));
		navigationPanel.add(centerPanel,BorderLayout.CENTER);
	}
}
