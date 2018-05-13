package rafalk42.http.stream.dumper.http;

import java.io.OutputStream;


/**
 *
 * @author rafalk42
 */
public interface HttpStreamDumperInterface
{
	void start(OutputStream outputStream)
		   throws HttpStreamDumperInternalError;

	void stop();
}
