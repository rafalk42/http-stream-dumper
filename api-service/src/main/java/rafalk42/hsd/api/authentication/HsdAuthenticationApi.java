package rafalk42.hsd.api.authentication;


/**
 *
 * @author rafalk42
 */
public interface HsdAuthenticationApi
{
	AuthenticationResult authenticate(HsdUserCredentials credentials);
	boolean isAuthenticated(AuthenticationToken token);
}
