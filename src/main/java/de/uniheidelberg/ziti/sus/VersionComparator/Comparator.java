package de.uniheidelberg.ziti.sus.VersionComparator;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Comparator {
	/**
	 * Compare two numerical-only versions.
	 * 
	 * @param version1 The first version to compare.
	 * @param version2 The second version to compare.
	 * @return -1 if version1 &lt; version2, +1 if version1 &gt; version2, 0 if
	 *         version1 == version2.
	 */
	public static int compareSimpleVersions(final String version1, String version2) {
		Pattern versionPattern = Pattern.compile("[0-9]+(\\.[0-9]+)*");
		if (!versionPattern.matcher(version1).matches()) {
			throw new IllegalArgumentException("version1 has unsupported format.");
		}
		if (!versionPattern.matcher(version2).matches()) {
			throw new IllegalArgumentException("version2 has unsupported format.");
		}
		// split into individual parts separated by ".".
		// Scanners are resources ⇒ use try-with-resource.
		try (Scanner scan1 = new Scanner(version1); Scanner scan2 = new Scanner(version2);) {
			scan1.useDelimiter("\\.");
			scan2.useDelimiter("\\.");

			while (scan1.hasNextInt() && scan2.hasNextInt()) {
				// while both versions have components at this level, a mismatch gives us an
				// immediate result.
				int p1 = scan1.nextInt();
				int p2 = scan2.nextInt();
				if (p1 < p2) {
					return -1;
				} else if (p1 > p2) {
					return +1;
				}
				// equal ⇒ fall through to next part.
			}

			// all parts of at least one version consumed. check if one version has another
			// non-zero part.
			if (scan1.hasNextInt() && scan1.nextInt() != 0) {
				return +1;
			}
			if (scan2.hasNextInt() && scan2.nextInt() != 0) {
				return -1;
			}

			// no mismatch found ⇒ equal
			return 0;
		}
	}

	/**
	 * Compare two complex version strings.
	 * 
	 * Each part may have a non-numerical suffix. Suffixed are compared
	 * lexicographically, and a version with no suffix is considered small than the
	 * same version with any suffix. I.e. 1.0-pre would be larger than 1.0!
	 * 
	 * @param version1 The first version to compare.
	 * @param version2 The second version to compare.
	 * @return -1 if version1 &lt; version2, +1 if version1 &gt; version2, 0 if
	 *         version1 == version2.
	 */
	public static int compareVersions(final String version1, final String version2) {
		// split into individual parts separated by ".".
		// Scanners are resources ⇒ use try-with-resource.
		try (Scanner scan1 = new Scanner(version1); Scanner scan2 = new Scanner(version2);) {
			scan1.useDelimiter("\\.");
			scan2.useDelimiter("\\.");

			while (scan1.hasNext() && scan2.hasNext()) {
				// while both versions have components at this level, a mismatch gives us an
				// immediate result.
				String p1 = scan1.next();
				String p2 = scan2.next();

				// split into numeric part and remainder
				String num1 = new String();
				String rem1 = new String();
				for (int i = 0; i < p1.length(); ++i) {
					if (Character.isDigit(p1.charAt(i))) {
						num1 += p1.charAt(i);
					} else {
						rem1 = p1.substring(i);
						break;
					}
				}
				// what to do if there is no numeric part, i.e. 1.a?
				// we treat it as 1.0a
				long v1 = num1.isEmpty() ? 0 : Long.valueOf(num1);

				String num2 = new String();
				String rem2 = new String();
				for (int i = 0; i < p2.length(); ++i) {
					if (Character.isDigit(p2.charAt(i))) {
						num2 += p2.charAt(i);
					} else {
						rem2 = p2.substring(i);
						break;
					}
				}
				long v2 = num2.isEmpty() ? 0 : Long.valueOf(num2);

				if (v1 < v2) {
					return -1;
				} else if (v1 > v2) {
					return +1;
				}

				// equal ⇒ compare remainders.
				if (rem1.isEmpty() && !rem2.isEmpty()) {
					return -1;
				} else if (!rem1.isEmpty() && rem2.isEmpty()) {
					return +1;
				} else if (!rem1.isEmpty() && !rem2.isEmpty()) {
					// scale to +1/-1
					int result = rem1.compareTo(rem2);
					return result / Math.abs(result);
				}

				// equal ⇒ fall through to next part.
			}

			// all parts of at least one version consumed. check if one version has another
			// non-zero part.
			if (scan1.hasNextInt() && scan1.nextInt() != 0) {
				return +1;
			}
			if (scan2.hasNextInt() && scan2.nextInt() != 0) {
				return -1;
			}

			// no mismatch found ⇒ equal
			return 0;
		}
	}
}
