package rafalk42.http.stream.dumper;


/**
 *
 * @author rafalk42
 */
public class StreamDumperConfiguration
{
	private final String storagePath;

	public StreamDumperConfiguration(String storagePath)
	{
		this.storagePath = storagePath;
	}

	public String getStoragePath()
	{
		return storagePath;
	}
}
