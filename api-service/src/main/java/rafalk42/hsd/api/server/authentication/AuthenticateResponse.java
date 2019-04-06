package rafalk42.hsd.api.server.authentication;

import rafalk42.hsd.api.authentication.AuthenticationToken;
import rafalk42.hsd.api.authentication.AuthenticationTokenSerializer;


/**
 *
 * @author rafalk42
 */
public class AuthenticateResponse
{
	enum Result
	{
		SUCCESSFUL, FAILED
	}
	private final Result result;
	private final String token;

	AuthenticateResponse(Result result, AuthenticationToken token)
	{
		this.result = result;
		this.token = AuthenticationTokenSerializer.serialize(token);
	}
}
