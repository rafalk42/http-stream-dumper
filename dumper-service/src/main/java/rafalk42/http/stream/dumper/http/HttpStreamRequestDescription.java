package rafalk42.http.stream.dumper.http;


/**
 *
 * @author rafalk42
 */
public class HttpStreamRequestDescription
{
	private final String url;

	public HttpStreamRequestDescription(String url)
	{
		this.url = url;
	}

	String getUrl()
	{
		return url;
	}
}
