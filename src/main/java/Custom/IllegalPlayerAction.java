package Custom;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.data.xml.AdminData;
import net.sf.l2j.gameserver.enums.PunishmentType;
import net.sf.l2j.gameserver.model.actor.Player;

public final class IllegalPlayerAction implements Runnable
{
    private static Logger _logAudit;
    private final String _message;
    private final int _punishment;
    private final Player _actor;
    public static final int PUNISH_BROADCAST = 1;
    public static final int PUNISH_KICK = 2;
    public static final int PUNISH_KICKBAN = 3;
    public static final int PUNISH_JAIL = 4;
    
    public IllegalPlayerAction(final Player actor, final String message, final int punishment) {
        this._message = message;
        this._punishment = punishment;
        this._actor = actor;
        switch (punishment) {
            case 2: {
                this._actor.sendMessage("You will be kicked for illegal action, GM informed.");
                break;
            }
            case 3: {
                this._actor.setAccessLevel(-100);
                this._actor.setAccountAccesslevel(-100);
                this._actor.sendMessage("You are banned for illegal action, GM informed.");
                break;
            }
            case 4: {
                this._actor.sendMessage("Illegal action performed!");
                this._actor.sendMessage("You will be teleported to GM Consultation Service area and jailed.");
                break;
            }
        }
    }
    
    @Override
    public void run() {
        final LogRecord record = new LogRecord(Level.INFO, "AUDIT:" + this._message);
        record.setLoggerName("audit");
        record.setParameters(new Object[] { this._actor, this._punishment });
        IllegalPlayerAction._logAudit.log(record);
        AdminData.getInstance().broadcastMessageToGMs(this._message);
        switch (this._punishment) {
            case 1: {}
            case 2: {
                this._actor.logout(false);
                break;
            }
            case 3: {
                this._actor.logout(false);
                break;
            }
            case 4: {
                this._actor.getPunishment().setType(PunishmentType.JAIL, CustomConfig.DEFAULT_PUNISH_PARAM);
                break;
            }
        }
    }
    
    static {
        IllegalPlayerAction._logAudit = Logger.getLogger("audit");
    }
}
