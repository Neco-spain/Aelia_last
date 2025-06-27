package Customs.vote.data.Topzone;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author L2Cygnus
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopZonePersonalResponse
{
	private boolean ok;
	private TopZoneResultPersonal result;
	
	@JsonProperty("result")
	public TopZoneResultPersonal getResult()
	{
		return result;
	}
	
	public void setTotalVotes(TopZoneResultPersonal responseResult)
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
