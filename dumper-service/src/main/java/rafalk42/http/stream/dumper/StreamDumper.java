package rafalk42.http.stream.dumper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.format.ISODateTimeFormat;
import rafalk42.http.stream.dumper.domain.StreamItem;
import rafalk42.http.stream.dumper.domain.StreamSchedule;
import rafalk42.http.stream.dumper.domain.StreamsConfiguration;
import rafalk42.http.stream.dumper.http.HttpStreamDumper;
import rafalk42.http.stream.dumper.http.HttpStreamDumperInternalError;
import rafalk42.http.stream.dumper.http.HttpStreamRequestDescription;


/**
 *
 * @author rafalk42
 */
public class StreamDumper
{
	private final static Logger log = Logger.getLogger(StreamDumper.class);
	private final StreamDumperConfiguration configuration;
	private final StreamsConfiguration streamsConfiguration;
	private final ScheduledExecutorService scheduledExecutor;
	private final List<ScheduledDump> schduledDumps;
	private final AtomicInteger nextId;

	public StreamDumper(StreamDumperConfiguration configuration, StreamsConfiguration streamsConfiguration)
	{
		this.configuration = configuration;
		this.streamsConfiguration = streamsConfiguration;

		this.schduledDumps = new ArrayList<>();

		nextId = new AtomicInteger(1);

		scheduledExecutor = Executors.newScheduledThreadPool(8);
	}

	public void start()
	{
		initializeScheduling();
	}

	private void initializeScheduling()
	{
		for (StreamItem stream : streamsConfiguration.getStreams())
		{
			try
			{
				String storagePath = prepareStorage(configuration.getStoragePath(), stream.getName());
				ScheduledDumper scheduledDumper = new ScheduledDumper(stream.getUrl(), storagePath);

				for (StreamSchedule schedule : stream.getSchedules())
				{
					ScheduledDump scheduledDump = new ScheduledDump(nextId.getAndIncrement(),
														   scheduledExecutor,
														   scheduledDumper,
														   schedule);
					schduledDumps.add(scheduledDump);

					scheduledExecutor.scheduleAtFixedRate(() ->
					{
						scheduledDump.reschedule();
					}, 0, 1, TimeUnit.MINUTES);
				}
			}
			catch (StreamDumperInternalError ex)
			{
				log.error("Stream dumper error", ex);
			}
		}
	}

	private String prepareStorage(String storagePath, String name)
		   throws StreamDumperInternalError
	{
		File storageRoot = new File(storagePath);

		if (!storageRoot.exists())
		{
			throw new StreamDumperInternalError(String.format("Storage directory \"%s\" doesn't exist", storageRoot.getPath()));
		}

		if (!storageRoot.isDirectory())
		{
			throw new StreamDumperInternalError(String.format("\"%s\" is not a directory", storageRoot.getPath()));
		}

		String cleanSubDirName = cleanSubDirName(name);
		File storageSubdir = new File(storageRoot, cleanSubDirName);

		if (!storageSubdir.exists() || !storageSubdir.isDirectory())
		{
			if (!storageSubdir.mkdir())
			{
				throw new StreamDumperInternalError(String.format("Couldn't create storage sub-directory \"%s\"", storageSubdir.getPath()));
			}
		}

		return storageSubdir.getPath();
	}

	private String cleanSubDirName(String name)
	{
		return name.replace('"', '_');
	}


	private static class ScheduledDump
	{
		private final int id;
		private final ScheduledExecutorService executor;
		private final ScheduledDumper dumper;
		private final StreamSchedule schedule;
		private Instant scheduledStart;
		private Instant scheduledStop;

		public ScheduledDump(int id, ScheduledExecutorService executor, ScheduledDumper dumper, StreamSchedule schedule)
		{
			this.id = id;
			this.executor = executor;
			this.dumper = dumper;
			this.schedule = schedule;

			scheduledStart = null;
			scheduledStop = null;
		}

		void reschedule()
		{
			Instant now = Instant.now();

			if (scheduledStart != null && scheduledStop != null)
			{
				if (scheduledStart.isAfter(now))
				{
					// haven't reached the start point yet
					log.debug(String.format("id:%d hasn't started yet", id));

					return;
				}
				// we are already past the start point

				if (scheduledStop.isAfter(now))
				{
					// didn't finish yet
					log.debug(String.format("id:%d hasn't finished yet", id));

					return;
				}
				// last scheduled execution has finished, need to reschedule

				log.debug(String.format("id:%d has finished, needs rescheduling", id));
			}
			else
			{
				log.debug(String.format("id:%d first time scheduling", id));
			}

			LocalTime stopTime = schedule.getStopTime();

			DateTime nextExecution;
			switch (schedule.getType())
			{
				case WEEKLY:
					LocalDate nearestDayOfWeek = getNearestDayOfWeek(new LocalDate(now), schedule.getDayOfWeek());
					nextExecution = nearestDayOfWeek.toDateTime(schedule.getStartTime());
					break;

				case HOURLY:
					DateTime nextHour = now.toDateTime().hourOfDay().roundFloorCopy();
					nextExecution = nextHour.withMinuteOfHour(schedule.getMinuteOfHourStart());
					break;

				default:
					throw new IllegalArgumentException(String.format("Unsupported schedule type: %s", schedule.getType().name()));
			}

			DateTime nextStop;
			switch (schedule.getType())
			{
				case WEEKLY:
					nextStop = nextExecution.withTime(stopTime);
					break;

				case HOURLY:
					nextStop = nextExecution.withMinuteOfHour(schedule.getMinuteOfHourStop());
					break;

				default:
					throw new IllegalArgumentException(String.format("Unsupported schedule type: %s", schedule.getType().name()));
			}
			if (nextExecution.isAfter(now))
			{
				// it didn't start yet
				log.debug(String.format("id:%d didn't start yet", id));
			}
			else if (nextExecution.isBefore(now))
			{

				if (nextStop.isBefore(now))
				{
					// already finished
					log.debug(String.format("id:%d already finished, execute at the next %s",
									    id,
									    schedule.getType()));
					switch (schedule.getType())
					{
						case WEEKLY:
							nextExecution = nextExecution.plusWeeks(1);
							nextStop = nextStop.plusWeeks(1);
							break;

						case HOURLY:
							nextExecution = nextExecution.plusHours(1);
							nextStop = nextStop.plusHours(1);
							break;

//						default:
//							throw new IllegalArgumentException(String.format("Unsupported schedule type: %s", schedule.getType().name()));
					}
				}
				else
				{
					log.debug(String.format("id:%d has already started, execute now", id));
					nextExecution = now.toDateTime();
				}
			}

			Period deltaToNextExecution = new Period(now, nextExecution.toInstant());
			Period deltaToNextStop = new Period(now, nextStop.toInstant());
			
			
			log.debug(String.format("id:%d delta to execution %s",
							    id,
							    deltaToNextExecution.toString()));
			log.debug(String.format("id:%d delta to stop %s",
							    id,
							    deltaToNextStop.toString()));

			log.debug(String.format("id:%d scheduling next execution at %s, stop at %s",
							    id,
							    nextExecution.toString(),
							    nextStop.toString()));

			// get as whole seconds
			long startDelay = deltaToNextExecution.toStandardSeconds().getSeconds() * 1000;
			// and add millis
			startDelay += deltaToNextExecution.getMillis();
			
			long stopDelay = deltaToNextStop.toStandardSeconds().getSeconds() * 1000;
			stopDelay += deltaToNextStop.getMillis();
			
			executor.schedule(dumper, startDelay, TimeUnit.MILLISECONDS);
			executor.schedule(() ->
			{
				dumper.stop();
			}, stopDelay, TimeUnit.MILLISECONDS);

			scheduledStart = nextExecution.toInstant();
			scheduledStop = nextStop.toInstant();

		}

		private static LocalDate getNearestDayOfWeek(LocalDate t0, int dow)
		{
			LocalDate t1 = t0.withDayOfWeek(dow);
			if (t1.isBefore(t0.minusDays(3)))
			{
				return t1.plusWeeks(1);
			}
			else if (t1.isAfter(t0.plusDays(3)))
			{
				return t1.minusWeeks(1);
			}
			else
			{
				return t1;
			}
		}
	}


	private static class ScheduledDumper implements Runnable
	{
		private final static Logger log = Logger.getLogger(ScheduledDumper.class);
		private final String path;
		private final HttpStreamDumper httpStreamDumper;
		private final String url;

		ScheduledDumper(String url, String path)
		{
			this.path = path;
			this.url = url;

			httpStreamDumper = new HttpStreamDumper(new HttpStreamRequestDescription(url));
		}

		void stop()
		{
			log.info(String.format("Stopping dump of %s to %s", url, path));
			httpStreamDumper.stop();
		}

		@Override
		public void run()
		{
			log.info(String.format("Starting dump of %s to %s", url, path));
			try
			{
				try
				{
					String fileName = Instant.now().toString(ISODateTimeFormat.basicDateTimeNoMillis());
					File storageSubdirectory = new File(path);
					File file = new File(storageSubdirectory, fileName);
					OutputStream outputStream = new FileOutputStream(file);

					httpStreamDumper.start(outputStream);
				}
				catch (HttpStreamDumperInternalError ex)
				{
					log.error("Http stream dumper error", ex);
				}
			}
			catch (Throwable ex)
			{
				log.error("Uncaught exception", ex);
			}
			log.info("Fin");
		}
	}
}
