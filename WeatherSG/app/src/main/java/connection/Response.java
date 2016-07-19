package connection;

/**
 * Created by uba on 14/7/16.
 */
public class Response {
    /**
     * NEA API server response
     */
    ParseResult parseResult;
    /**
     * Indicates result of background task
     */
    TaskResult taskResult;

    public enum ParseResult {OK, DATA_EXCEPTION}

    public enum TaskResult {SUCCESS, BAD_RESPONSE, IO_EXCEPTION, TOO_MANY_REQUESTS;}
}
