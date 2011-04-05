package core;
import static junit.framework.Assert.*;
import org.junit.*;

public class ModelTest {
	/**
	 * 
	 */
	private Model model;
	
	private void BeforeClass() {
		model =  new Model(null);
	}	
	
	@Test public void updateBoundsTestNull() {
		model.updateBounds(null);
	}
}
