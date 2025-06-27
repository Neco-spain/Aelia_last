package net.sf.l2j.gameserver.handler;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.handler.voicedcommandhandlers.*;


public class VoicedCommandHandler
 {
     private final Map<Integer, IVoicedCommandHandler> _datatable = new HashMap<>();
     
     public static VoicedCommandHandler getInstance()
     {
         return SingletonHolder._instance;
     }
     
     protected VoicedCommandHandler()
     {
    	 //register handlers
    	 registerHandler(new trySkin());
    	 registerHandler(new Events());
    	 registerHandler(new Menu());
    	 registerHandler(new GrandBossStatus());
    	 registerHandler(new CastleManagers());
    	 registerHandler(new BankingCommand());
    	 registerHandler(new DropInfo());
    	 
    	 registerHandler(new Online());
    	 registerHandler(new Repair()); 
    	 registerHandler(new Rankings()); 
    	 
    	 registerHandler(new voteBuff());
    	 registerHandler(new VoteTopzoneCommandHandler());
    	 registerHandler(new VoteHopzoneCommandHandler());
    	 registerHandler(new VoteNetworkCommandHandler());
    	 registerHandler(new VoteL2VotesCommandHandler());
    	 registerHandler(new VoteBrasilCommandHandler());
    	 registerHandler(new VoteTopcoCommandHandler());
    	 
    	 registerHandler(new vip());
    	 registerHandler(new exit());
         registerHandler(new missions());

     }
     
     public void registerHandler(IVoicedCommandHandler handler)
     {
         String[] ids = handler.getVoicedCommandList();
         
         for (int i = 0; i < ids.length; i++)        
             _datatable.put(ids[i].hashCode(), handler);
     }
         
     public IVoicedCommandHandler getHandler(String voicedCommand)
     {
         String command = voicedCommand;
         
         if (voicedCommand.indexOf(" ") != -1)        
             command = voicedCommand.substring(0, voicedCommand.indexOf(" "));        
 
         return _datatable.get(command.hashCode());        
     }
     
     public int size()
     {
         return _datatable.size();
     }
     
     private static class SingletonHolder
     {
         protected static final VoicedCommandHandler _instance = new VoicedCommandHandler();
     }

 }