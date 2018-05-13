package rafalk42.http.stream.dumper.domain;

import java.util.List;
import java.util.Objects;


/**
 *
 * @author rafalk42
 */
public class StreamItem
{
	private final String name;
	private final String url;
	private final List<StreamSchedule> schedules;

	private StreamItem()
	{
		this.name = null;
		this.url = null;
		this.schedules = null;
	}

	@Override
	public String toString()
	{
		return String.format("{name: \"%s\", url: \"%s\", %s}",
						 name, url, schedules.toString());
	}

	public String getName()
	{
		return name;
	}

	public String getUrl()
	{
		return url;
	}

	public List<StreamSchedule> getSchedules()
	{
		return schedules;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 43 * hash + Objects.hashCode(this.name);
		hash = 43 * hash + Objects.hashCode(this.url);
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
		final StreamItem other = (StreamItem) obj;
		if (!Objects.equals(this.name, other.name))
		{
			return false;
		}
		if (!Objects.equals(this.url, other.url))
		{
			return false;
		}
		return true;
	}
}
