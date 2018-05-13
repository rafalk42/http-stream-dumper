package rafalk42.http.stream.dumper.domain;

import java.io.IOException;


/**
 *
 * @author rafalk42
 */
public class StreamConfigurationReaderInternalError extends Exception
{
	public StreamConfigurationReaderInternalError(IOException ex)
	{
		super(ex);
	}
}
