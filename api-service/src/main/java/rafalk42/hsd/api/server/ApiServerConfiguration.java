package rafalk42.hsd.api.server;


/**
 *
 * @author rafalk42
 */
public class ApiServerConfiguration
{
	private final String address;
	private final int port;

	public ApiServerConfiguration(String address, int port)
	{
		this.address = address;
		this.port = port;
	}

	String getAddress()
	{
		return address;
	}

	int getPort()
	{
		return port;
	}
}
