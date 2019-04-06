package rafalk42.hsd.api.authentication;

import java.util.Objects;


/**
 *
 * @author rafalk42
 */
public class AuthenticationToken
{
	private final String id;

	AuthenticationToken(String id)
	{
		this.id = id;
	}

	String getId()
	{
		return id;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 47 * hash + Objects.hashCode(this.id);

		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (getClass() != obj.getClass())
		{
			return false;
		}

		final AuthenticationToken other = (AuthenticationToken) obj;

		return Objects.equals(this.id, other.id);
	}
}
