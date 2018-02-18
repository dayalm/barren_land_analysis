package com.target.analyzer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BarrenLandAnalyzerTest {

	private BarrenLandAnalyzer analyzer;
	
	@Before
	public void setUp() throws Exception {
		this.analyzer = new BarrenLandAnalyzer();
	}

	@After
	public void tearDown() throws Exception {
		this.analyzer = null;
	}
	

	@Test
	public void testFindFertileAreas() {
		String[] barrenLandCoordinates = {"0 292 399 307"};
		
		List<Integer> fertileAreas = analyzer.findFertileAreas(barrenLandCoordinates);
		
		assertNotNull(fertileAreas);
		assertEquals(2,fertileAreas.size());
		assertEquals(116800,fertileAreas.get(0).intValue());
		assertEquals(116800,fertileAreas.get(1).intValue());
		
		barrenLandCoordinates = new String[]{"48 192 351 207","48 392 351 407", "120 52 135 547", "260 52 275 547"};
		
		
		fertileAreas = analyzer.findFertileAreas(barrenLandCoordinates);
		fertileAreas.sort(Comparator.naturalOrder());
		
		assertNotNull(fertileAreas);
		assertEquals(2,fertileAreas.size());
		assertEquals(22816,fertileAreas.get(0).intValue());
		assertEquals(192608,fertileAreas.get(1).intValue());
	}
	
	
	@Test
	public void testValidateCoordinates_success() {
		analyzer.validateCoordinates(0, 292, 399, 307);
	}
	
	@Test
	(expected = IllegalArgumentException.class)
	public void testValidateCoordinates_failure_negative_coordinate() {
		analyzer.validateCoordinates(0, -292, 399, 307);
	}
	
	@Test
	(expected = IllegalArgumentException.class)
	public void testValidateCoordinates_failure_topRightY_less_than_bottomLeftY() {
		analyzer.validateCoordinates(0, 292, 281, 291);
	}
	
	@Test
	(expected = IllegalArgumentException.class)
	public void testValidateCoordinates_failure_topRightX_equal_to_bottomLeftX() {
		analyzer.validateCoordinates(0, 292, 0, 307);
	}
	
	@Test
	(expected = IllegalArgumentException.class)
	public void testValidateCoordinates_failure_topRightX_less_than_bottomLeftX() {
		analyzer.validateCoordinates(48, 392, 30, 407);
	}
	
	@Test
	(expected = IllegalArgumentException.class)
	public void testValidateCoordinates_failure_X_great_than_399() {
		analyzer.validateCoordinates(0, 292, 400, 307);
	}
	
	@Test
	(expected = IllegalArgumentException.class)
	public void testValidateCoordinates_failure_Y_great_than_599() {
		analyzer.validateCoordinates(0, 292, 399, 600);
	}
	
	
}
