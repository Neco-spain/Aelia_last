package net.sf.l2j.commons.logging.formatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

import net.sf.l2j.commons.logging.MasterFormatter;

public class ConsoleLogFormatter extends MasterFormatter
{
	//custom TimeStamp For Console
	private static final String CRLF = "\r\n";
	private static final SimpleDateFormat tsformat = new SimpleDateFormat("HH:mm:ss  ");
	private Date ts = new Date();
	
	@Override
	public String format(LogRecord record){
		StringBuffer output = new StringBuffer();
		ts.setTime(record.getMillis());
		output.append(tsformat.format(ts));
		output.append(record.getMessage());
		output.append(CRLF);
		if(record.getThrown() != null)
			try
			{
			StringWriter sw = new StringWriter();
				try (PrintWriter pw = new PrintWriter(sw);)
				{
					record.getThrown().printStackTrace(pw);
				}
				output.append(sw.toString());
				output.append(CRLF);
			}
			catch(Exception ex){}
		return output.toString();
	}
		
	/*
	@Override
	public String format(LogRecord record)
	{
		final StringWriter sw = new StringWriter();
		sw.append(record.getMessage());
		sw.append(CRLF);
		
		final Throwable throwable = record.getThrown();
		if (throwable != null)
			throwable.printStackTrace(new PrintWriter(sw));
		
		return sw.toString();
	}
	*/
}