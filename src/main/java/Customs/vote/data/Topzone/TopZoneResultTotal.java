package Customs.vote.data.Topzone;



import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author L2Cygnus
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopZoneResultTotal
{
	private int totalVotes;
	
	public void setTotalVotes(int total)
	{
		totalVotes = total;
	}
	@JsonProperty("totalVotes")
	public int getTotalVotes()
	{
		return totalVotes;
	}
}
