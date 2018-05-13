package rafalk42.http.stream.dumper.domain;

import java.util.List;


/**
 *
 * @author rafalk42
 */
public class StreamsConfiguration
{
	private final List<StreamItem> streams;

	private StreamsConfiguration()
	{
		this.streams = null;
	}

	@Override
	public String toString()
	{
		return String.format("{streams: %s}",
						 streams.toString());
	}

	public List<StreamItem> getStreams()
	{
		return streams;
	}
}
