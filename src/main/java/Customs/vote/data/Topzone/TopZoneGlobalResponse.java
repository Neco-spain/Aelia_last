package Customs.vote.data.Topzone;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author L2Cygnus
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopZoneGlobalResponse
{
	private boolean ok;
	private TopZoneResultTotal result;
	
	@JsonProperty("result")
	public TopZoneResultTotal getResult()
	{
		return result;
	}
	
	public void setTotalVotes(TopZoneResultTotal responseResult)
	{
		result = responseResult;
	}
	
	@JsonProperty("ok")
	public boolean isOk()
	{
		return ok;
	}
	
	public void setOk(boolean isOk)
	{
		ok = isOk;
	}
}
