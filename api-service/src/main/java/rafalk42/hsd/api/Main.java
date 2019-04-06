package rafalk42.hsd.api;

import rafalk42.hsd.api.server.ApiServer;
import org.apache.log4j.PropertyConfigurator;
import rafalk42.hsd.api.authentication.HsdAuthenticationApi;
import rafalk42.hsd.api.authentication.HsdAuthenticationApiFileBased;
import rafalk42.hsd.api.server.ApiServerConfiguration;


/**
 *
 * @author rafalk42
 */
public class Main
{
	public static void main(String[] args)
	{
		String log4jConfigPath = System.getProperty("log4jConfigPath");
		PropertyConfigurator.configureAndWatch(log4jConfigPath);

		HsdAuthenticationApi authenticationApi = new HsdAuthenticationApiFileBased();
		ApiServer server = new ApiServer(new ApiServerConfiguration("0.0.0.0", 1122),
								   authenticationApi);

		server.initialize();
	}
}
