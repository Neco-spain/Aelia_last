package Customs.vote.API;

import Custom.CustomConfig;

public enum ApiData {

    HOPZONE( CustomConfig.HOPZONE_SERVER_API_KEY),
    TOPZONE(CustomConfig.TOPZONE_SERVER_API_KEY),
    NETWORK(CustomConfig.NETWORK_SERVER_NAME),
    L2VOTE(CustomConfig.L2VOTES_SERVER_API_KEY),
    TOPCO(CustomConfig.TOPCO_SERVER_API_KEY),
    L2JBRASIL(CustomConfig.BRASIL_SERVER_API_KEY),
    L2Servers(CustomConfig.L2SERVERS_SERVER_API_KEY);

    private final String _name;

    ApiData(String name)
    {
        _name = name;
    }

    public String getName(){
        return _name;
    }

}
