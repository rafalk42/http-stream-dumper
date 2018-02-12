package rafalk42.http.stream.dumper;

import java.nio.charset.Charset;
import org.apache.log4j.Logger;
import rafalk42.http.stream.dumper.domain.StreamConfigurationReader;
import rafalk42.http.stream.dumper.domain.StreamConfigurationReaderInternalError;
import rafalk42.http.stream.dumper.domain.StreamsConfiguration;


public class Main
{
	private final static Logger log = Logger.getLogger(Main.class);

	public static void main(String[] args)
	{
		log.info(String.format("Starting application [encoding:%s Java:(%s %s) OS:(%s %s %s)]", Charset.defaultCharset(),
						   System.getProperty("java.vendor"), System.getProperty("java.version"), System.getProperty("os.arch"),
						   System.getProperty("os.name"), System.getProperty("os.version")));

		StreamConfigurationReader streamConfigurationReader = new StreamConfigurationReader("configuration.json");
		StreamsConfiguration streamsConfiguration;
		try
		{
			streamsConfiguration = streamConfigurationReader.read();
		}
		catch (StreamConfigurationReaderInternalError ex)
		{
			log.error("Streams configuration read error", ex);
			System.exit(1);
			return;
		}

		log.debug(String.format("Streams configuration: %s", streamsConfiguration.toString()));

		StreamDumperConfiguration dumperConfiguration = new StreamDumperConfiguration("storage");
		StreamDumper streamDumper = new StreamDumper(dumperConfiguration, streamsConfiguration);
		streamDumper.start();
	}
}
