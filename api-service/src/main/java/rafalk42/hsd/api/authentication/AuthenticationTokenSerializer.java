package rafalk42.hsd.api.authentication;


/**
 *
 * @author rafalk42
 */
public class AuthenticationTokenSerializer
{
	public static String serialize(AuthenticationToken token)
	{
		return token.getId();
	}

	public static AuthenticationToken deserialize(String tokenText)
	{
		return new AuthenticationToken(tokenText);
	}
}
