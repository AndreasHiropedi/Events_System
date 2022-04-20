package logging;

import java.util.Map;

public class LogEntry
{

    private String callerName, result;
    private Map<String, Object> additionalInfo;

    /*
        LogEntry takes a Map as third parameter, where the keys are String
        variable names, and values are the variable values of any Object type.
     */
    public LogEntry(String callerName, Object result, Map<String, Object> additionalInfo)
    {
        this.callerName = callerName;
        this.result = result.toString();
        this.additionalInfo = additionalInfo;
    }
    public LogEntry(String callerName, Object result)
    {
        this.callerName = callerName;
        this.result = result.toString();
    }

    public String getResult()
    {
        return result;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("----- Log Entry Start -----");
        sb.append("\nCalled by "+callerName.toUpperCase());
        sb.append("\n\nResult:\n");
        sb.append(result);
        sb.append("\n\nAdditional Info:");

        // Iterate over each Map element and append its string
        // value to the string builder if any elements exist.
        // Otherwise, append "NONE".
        if (additionalInfo != null && !additionalInfo.isEmpty()) sb.append(additionalInfo);
        else sb.append("NONE");

        sb.append("\n----- Log Entry End -------");

        return sb.toString();
    }
}
