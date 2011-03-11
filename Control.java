import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Control {
	
	private static final String NAME = "Map";
	private View v;
	private Map m;
	
	/**
	 * Contstructor for class Control
	 */
	public Control() {
		v = new View(NAME, m.getLines());
		m = new Map();
		addListeners();
	}
	
	private void addListeners(){
		v.addUpListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							v.repaint(m.getLines());
						}});
		v.addDownListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							Line[] a = m.getLines();
							v.repaint(m.getLines());
						}});
		v.addLeftListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							v.repaint(m.getLines());
						}});
		v.addRightListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							v.repaint(m.getLines());
						}});
		v.addInListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							v.repaint(m.getLines());
						}});
		v.addOutListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							v.repaint(m.getLines());
						}});
	}
}
