package rafalk42.hsd.api.server.authentication;

import rafalk42.hsd.api.authentication.AuthenticationResult;
import rafalk42.hsd.api.authentication.AuthenticationToken;
import rafalk42.hsd.api.authentication.AuthenticationTokenSerializer;
import rafalk42.hsd.api.authentication.HsdAuthenticationApi;
import rafalk42.hsd.api.authentication.HsdUserCredentials;


/**
 *
 * @author rafalk42
 */
public class Authentication
{
	private final HsdAuthenticationApi api;

	public Authentication(HsdAuthenticationApi api)
	{
		this.api = api;
	}

	public AuthenticateResponse authenticate()
	{
		AuthenticationResult result = api.authenticate(new HsdUserCredentials());

		return new AuthenticateResponse(result.isSuccessful() ? AuthenticateResponse.Result.SUCCESSFUL : AuthenticateResponse.Result.FAILED,
								  result.getToken());
	}

	public boolean isAuthenticated(String tokenString)
	{
		AuthenticationToken token = AuthenticationTokenSerializer.deserialize(tokenString);
		return api.isAuthenticated(token);
	}
}
