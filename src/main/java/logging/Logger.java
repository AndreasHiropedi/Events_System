package logging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Logger
{

    private static Logger instance;
    private List<LogEntry> log;

    private Logger()
    {
        log = new ArrayList<>();
    }

    public void logAction(String callerName, Object result)
    {
        log.add(new LogEntry(callerName, result));
    }

    public void logAction(String callerName, Object result, Map<String, Object> additionalInfo)
    {
        log.add(new LogEntry(callerName, result, additionalInfo));
    }

    public static Logger getInstance()
    {
        if (instance == null)
        {
            instance = new Logger();
        }
        return instance;
    }

    public void clearLog()
    {
        log.clear();
    }

    public List<LogEntry> getLog()
    {
        return log;
    }
}
