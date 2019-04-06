package rafalk42.hsd.api.authentication;


/**
 *
 * @author rafalk42
 */
public class AuthenticationFailure
	   extends AuthenticationResult
{
	private final String message;

	public AuthenticationFailure(String message)
	{
		super(false, null);
		this.message = message;
	}

	@Override
	public String getMessage()
	{
		return message;
	}

	@Override
	public AuthenticationToken getToken()
	{
		throw new IllegalStateException("Failed authentication does not provide a token");
	}
}
