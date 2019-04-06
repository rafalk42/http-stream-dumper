package rafalk42.hsd.api.authentication;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;


/**
 *
 * @author rafalk42
 */
public class HsdAuthenticationApiFileBased
	   implements HsdAuthenticationApi
{
	private final Set<String> authenticatedTokens;

	public HsdAuthenticationApiFileBased()
	{
		this.authenticatedTokens = new HashSet<>();
	}

	@Override
	public AuthenticationResult authenticate(HsdUserCredentials credentials)
	{
		String token = generateNewToken();
		authenticatedTokens.add(token);

		return new AuthenticationSuccessful(new AuthenticationToken(token));
	}

	@Override
	public boolean isAuthenticated(AuthenticationToken token)
	{
		return authenticatedTokens.contains(token.getId());
	}

	private String generateNewToken()
	{
		String randomNumberString = Double.toString(Math.random());
		return DigestUtils.sha256Hex(randomNumberString);
	}
}
