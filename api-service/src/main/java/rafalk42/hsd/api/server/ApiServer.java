package rafalk42.hsd.api.server;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rafalk42.hsd.api.authentication.AuthenticationResult;
import rafalk42.hsd.api.authentication.AuthenticationToken;
import rafalk42.hsd.api.authentication.AuthenticationTokenSerializer;
import rafalk42.hsd.api.authentication.HsdAuthenticationApi;
import rafalk42.hsd.api.authentication.HsdAuthenticationApiFileBased;
import rafalk42.hsd.api.authentication.HsdUserCredentials;
import rafalk42.hsd.api.server.authentication.Authentication;
import spark.Request;
import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.path;
import static spark.Spark.setIpAddress;
import static spark.Spark.setPort;
import static spark.Spark.stop;


/**
 *
 * @author rafalk42
 */
public class ApiServer
{
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ApiServerConfiguration config;
	private final Authentication authentication;
	private final Gson gson;

	public ApiServer(ApiServerConfiguration config, HsdAuthenticationApi authenticationApi)
	{
		this.config = config;

		authentication = new Authentication(authenticationApi);
		gson = new Gson();
	}

	public void initialize()
	{
		setIpAddress(config.getAddress());
		setPort(config.getPort());

		exception(Exception.class, (e, req, res) -> e.printStackTrace());
		path("/api", () ->
	  {
		  before("/*", (req, resp) -> logger.info("HTTP API call for {}", req.pathInfo()));
		  before("/protected/*", (request, response) -> checkAuthentication(request));

		  path("/authentication", () ->
	    {
		    get("/authenticate", (request, response) -> authentication.authenticate(), gson::toJson);
	    });
		  path("/protected", () ->
	    {
		    get("/test", (req, resp) -> "Foo", gson::toJson);
	    });
	  });
	}

	void shutdown()
	{
		stop();
	}

	private void checkAuthentication(Request request)
	{
		String[] tokenParams = request.queryParamsValues("token");
		if (tokenParams == null)
		{
			handleNotAuthenticated();
			
			return;
		}
		
		for (String token : tokenParams)
		{
			if (!authentication.isAuthenticated(token))
			{
				handleNotAuthenticated();
			}
		}
	}

	private void handleNotAuthenticated()
	{
		throw halt(401, ":)");
	}
}
