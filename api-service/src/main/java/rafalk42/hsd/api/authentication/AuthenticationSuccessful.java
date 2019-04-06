package rafalk42.hsd.api.authentication;


/**
 *
 * @author rafalk42
 */
public class AuthenticationSuccessful
	   extends AuthenticationResult
{
	AuthenticationSuccessful(AuthenticationToken token)
	{
		super(true, token);
	}

	@Override
	public String getMessage()
	{
		return null;
	}

	@Override
	public AuthenticationToken getToken()
	{
		return internalGetToken();
	}
}
