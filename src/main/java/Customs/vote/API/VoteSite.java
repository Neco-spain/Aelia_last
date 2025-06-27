package Customs.vote.API;


public enum VoteSite {

    HOPZONE("Hopzone"),
    TOPZONE("Topzone"),
    NETWORK("L2Network"),
    L2VOTE("L2Vote"),
	TOPCO("l2Topco"),
	L2JBRASIL("l2jbrasil");
	
	
    private final String _name;

    VoteSite(String name)
    {
        _name = name;
    }

    public String getName(){
        return _name;
    }
}