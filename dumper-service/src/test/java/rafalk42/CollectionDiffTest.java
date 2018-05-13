package rafalk42;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import rafalk42.http.stream.dumper.utils.CollectionDiff;


/**
 *
 * @author rafalk42
 */
public class CollectionDiffTest
{
	private static Set<String> first;
	private static Set<String> second;

	public CollectionDiffTest()
	{
	}

	@BeforeClass
	public static void setUpClass()
	{
		first = new HashSet<>();
		second = new HashSet();
		Collections.addAll(first, "a", "b", "c");
		Collections.addAll(second, "b", "c", "d");
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
	}

	@After
	public void tearDown()
	{
	}

	@Test
	public void testGetAddedInSecond()
	{
		Set<String> expected = new HashSet<>();

		Collections.addAll(expected, "d");

		Set result = CollectionDiff.getAddedInSecond(first, second);

		assertEquals(expected, result);
	}

	@Test
	public void testGetRemovedInSecond()
	{
		Set<String> expected = new HashSet<>();

		Collections.addAll(expected, "a");

		Set result = CollectionDiff.getRemovedInSecond(first, second);

		assertEquals(expected, result);
	}
}
