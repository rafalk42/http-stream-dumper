package rafalk42.http.stream.dumper.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;


/**
 *
 * @author rafalk42
 */
public class HttpStreamDumper implements HttpStreamDumperInterface
{
	private final static Logger log = Logger.getLogger(HttpStreamDumper.class);
	private final HttpStreamRequestDescription description;
	private boolean stopTheStream;

	public HttpStreamDumper(HttpStreamRequestDescription description)
	{
		this.description = description;
	}

	@Override
	public void start(OutputStream outputStream)
		   throws HttpStreamDumperInternalError
	{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(description.getUrl());

		try
		{
			log.info(String.format("Opening HTTP connection to %s", description.getUrl()));
			try (CloseableHttpResponse response = httpclient.execute(httpget))
			{
				InputStream contentStream = response.getEntity().getContent();

				log.debug("HTTP stream dump starting");
				byte[] buffer = new byte[65536];
				stopTheStream = false;
				do
				{
					int ret = contentStream.read(buffer);

					if (ret == -1)
					{
						log.debug("End of HTTP stream reached");
						break;
					}

					outputStream.write(buffer, 0, ret);
				} while (!stopTheStream);

				log.info(String.format("Closing HTTP connection to %s", description.getUrl()));
			}
		}
		catch (IOException ex)
		{
			throw new HttpStreamDumperInternalError(ex);
		}
	}

	@Override
	public void stop()
	{
		stopTheStream = true;
		log.debug("Stop has been requested");
	}
}
