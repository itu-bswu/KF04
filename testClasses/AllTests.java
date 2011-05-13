package testClasses;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		//suite.addTestSuite(RectangleMethodsTest.class);
		suite.addTestSuite(PointMethodsTest.class);
		//suite.addTestSuite(ModelTest.class);
		//$JUnit-END$
		return suite;
	}

}
