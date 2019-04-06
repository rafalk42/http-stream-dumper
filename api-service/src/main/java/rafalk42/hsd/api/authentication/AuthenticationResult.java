package rafalk42.hsd.api.authentication;


/**
 *
 * @author rafalk42
 */
public abstract class AuthenticationResult
{
	private final boolean successful;
	private final AuthenticationToken token;

	AuthenticationResult(boolean successful, AuthenticationToken token)
	{
		this.successful = successful;
		this.token = token;
	}

	public boolean isSuccessful()
	{
		return successful;
	}

	public abstract AuthenticationToken getToken();

	public abstract String getMessage();

	AuthenticationToken internalGetToken()
	{
		return token;
	}
}
