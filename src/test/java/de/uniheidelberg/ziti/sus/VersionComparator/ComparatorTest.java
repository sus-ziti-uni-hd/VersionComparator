package de.uniheidelberg.ziti.sus.VersionComparator;

import org.junit.Test;

import de.uniheidelberg.ziti.sus.VersionComparator.Comparator;

import static org.junit.Assert.*;

public class ComparatorTest {
	@Test
	public void testSimpleComparator() {
		assertTrue(Comparator.compareSimpleVersions("1", "1") == 0);
		assertTrue(Comparator.compareSimpleVersions("1", "2") == -1);
		assertTrue(Comparator.compareSimpleVersions("1.1", "2") == -1);
		assertTrue(Comparator.compareSimpleVersions("2", "1") == +1);
		assertTrue(Comparator.compareSimpleVersions("2.1", "2.0") == +1);
		assertTrue(Comparator.compareSimpleVersions("2.0.1", "2.0") == +1);
		assertTrue(Comparator.compareSimpleVersions("2.0.0", "2.0") == 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegal() {
		Comparator.compareSimpleVersions("1a", "1");
	}

	@Test
	public void testMixedComparator() {
		assertTrue(Comparator.compareVersions("1", "1") == 0);
		assertTrue(Comparator.compareVersions("1", "2") == -1);
		assertTrue(Comparator.compareVersions("1.1", "2") == -1);
		assertTrue(Comparator.compareVersions("2", "1") == +1);
		assertTrue(Comparator.compareVersions("2.1", "2.0") == +1);
		assertTrue(Comparator.compareVersions("2.0.1", "2.0") == +1);
		assertTrue(Comparator.compareVersions("2.0.0", "2.0") == 0);

		assertTrue(Comparator.compareVersions("1a", "1") == +1);
		assertTrue(Comparator.compareVersions("1a", "1b") == -1);
		assertTrue(Comparator.compareVersions("2", "2c") == -1);
		assertTrue(Comparator.compareVersions("10", "2c") == +1);

		assertTrue(Comparator.compareVersions("0.96", "1.0-pre") == -1);

		assertTrue(Comparator.compareVersions("0.1aa", "0.1b") == +1);
		assertTrue(Comparator.compareVersions("0.1ba", "0.1bb") == -1);
		assertTrue(Comparator.compareVersions("0.1c", "0.1bb") == -1);
	}
}
