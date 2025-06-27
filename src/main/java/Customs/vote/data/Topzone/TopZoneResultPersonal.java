package Customs.vote.data.Topzone;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author L2Cygnus
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopZoneResultPersonal
{
	private boolean isVoted;
	private long serverTime;
	
	/**
	 * @return the isVoted
	 */
	@JsonProperty("isVoted")
	public boolean isVoted()
	{
		return isVoted;
	}
	
	/**
	 * @param isVoted the isVoted to set
	 */
	public void setVoted(boolean isVoted)
	{
		this.isVoted = isVoted;
	}
	
	/**
	 * @return the serverTime
	 */
	@JsonProperty("serverTime")
	public long getServerTime()
	{
		return serverTime;
	}
	
	/**
	 * @param serverTime the serverTime to set
	 */
	public void setServerTime(long serverTime)
	{
		this.serverTime = serverTime;
	}
	
}
