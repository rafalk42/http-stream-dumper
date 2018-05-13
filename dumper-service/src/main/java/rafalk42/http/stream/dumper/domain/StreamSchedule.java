package rafalk42.http.stream.dumper.domain;

import java.util.Objects;
import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;


/**
 *
 * @author rafalk42
 */
public class StreamSchedule
{
	public enum StreamScheduleType
	{
		WEEKLY,
		HOURLY
	}
	private final StreamScheduleType type;
	private final int day_of_week;
	private final int minute_of_hour_start;
	private final int minute_of_hour_stop;
	private final LocalTime start_time;
	private final LocalTime stop_time;

	private StreamSchedule()
	{
		this.type = null;
		this.day_of_week = 0;
		this.minute_of_hour_start = 0;
		this.minute_of_hour_stop= 0;
		this.start_time = null;
		this.stop_time = null;
	}

	@Override
	public String toString()
	{
		return String.format("{type: %s, day_of_week: %d, minute_of_hour_start: %d, minute_of_hour_stop: %d, start_time: %s, stop_time: %s}",
						 type.name(),
						 day_of_week,
						 minute_of_hour_start,
						 minute_of_hour_stop,
						 start_time == null ? "null" : start_time.toString(ISODateTimeFormat.dateHourMinuteSecond()),
						 stop_time == null ? "null" : stop_time.toString(ISODateTimeFormat.dateHourMinuteSecond()));
	}

	public StreamScheduleType getType()
	{
		return type;
	}

	public int getDayOfWeek()
	{
		return day_of_week;
	}

	public int getMinuteOfHourStart()
	{
		return minute_of_hour_start;
	}

	public int getMinuteOfHourStop()
	{
		return minute_of_hour_stop;
	}

	public LocalTime getStartTime()
	{
		return start_time;
	}

	public LocalTime getStopTime()
	{
		return stop_time;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 97 * hash + Objects.hashCode(this.type);
		hash = 97 * hash + this.day_of_week;
		hash = 97 * hash + Objects.hashCode(this.start_time);
		hash = 97 * hash + Objects.hashCode(this.stop_time);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final StreamSchedule other = (StreamSchedule) obj;
		if (this.type != other.type)
		{
			return false;
		}
		if (this.day_of_week != other.day_of_week)
		{
			return false;
		}
		if (!Objects.equals(this.start_time, other.start_time))
		{
			return false;
		}
		if (!Objects.equals(this.stop_time, other.stop_time))
		{
			return false;
		}
		return true;
	}
}
