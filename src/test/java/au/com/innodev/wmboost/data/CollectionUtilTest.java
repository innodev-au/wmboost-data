package au.com.innodev.wmboost.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class CollectionUtilTest {

	@Test
	public void testAreCollectionElementsOfObject() {
		List<Object> items = Lists.<Object>newArrayList(5, "A");
		
		assertTrue(CollectionUtil.areAllElementsOfType(items, Object.class));
		assertFalse(CollectionUtil.areAllElementsOfType(items, Integer.class));
		assertFalse(CollectionUtil.areAllElementsOfType(items, String.class));
	}
	
	@Test
	public void testAreCollectionElementsOfString() {
		List<Object> items = Lists.<Object>newArrayList("A", "B");
		
		assertTrue(CollectionUtil.areAllElementsOfType(items, String.class));
	}

}
