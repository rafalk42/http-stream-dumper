package rafalk42.http.stream.dumper.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 *
 * @author rafalk42
 */
public class StreamConfigurationReader
{
	private final static Logger log = Logger.getLogger(StreamConfigurationReader.class);
	private final String path;
	private final Gson gson;

	public StreamConfigurationReader(String path)
	{
		this.path = path;

		DateTimeFormatter localTimeDateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss");

		gson = new GsonBuilder().registerTypeAdapter(LocalTime.class, new TypeAdapter<LocalTime>()
		{

			@Override
			public void write(JsonWriter out, LocalTime value) throws IOException
			{
			}

			@Override
			public LocalTime read(JsonReader reader) throws IOException
			{
				if (reader.peek() == JsonToken.NULL)
				{
					reader.nextNull();

					return null;
				}

				String text = reader.nextString();
				return localTimeDateTimeFormatter.parseLocalTime(text);
			}
		}).create();
	}

	public StreamsConfiguration read()
		   throws StreamConfigurationReaderInternalError
	{
		File file = new File(path);
		try
		{
			InputStream inputStream = new FileInputStream(file);
			String configurationJson = IOUtils.toString(inputStream, Charset.defaultCharset());

			StreamsConfiguration configuration = gson.fromJson(configurationJson, StreamsConfiguration.class);

			return configuration;
		}
		catch (IOException ex)
		{
			log.error("I/O error", ex);
			throw new StreamConfigurationReaderInternalError(ex);
		}
	}
}
