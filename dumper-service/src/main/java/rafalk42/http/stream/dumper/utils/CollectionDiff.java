package rafalk42.http.stream.dumper.utils;

import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author rafalk42
 */
public class CollectionDiff
{
	public static <T> Set<T> getAddedInSecond(Set<T> first, Set<T> second)
	{
		Set<T> temp = new HashSet(second);
		temp.removeAll(first);

		return temp;
	}

	public static <T> Set<T> getRemovedInSecond(Set<T> first, Set<T> second)
	{
		Set<T> temp = new HashSet(first);
		temp.removeAll(second);

		return temp;
	}
}
