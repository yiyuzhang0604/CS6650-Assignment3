/**
 * The type Request result, which stores the result of all request.
 */
public class RequestResult implements Comparable<RequestResult>{

    private long startTime;
    private long latency;
    private int responseCode;

    @Override
    public String toString() {
        return startTime + ",post," + latency + "," + responseCode + "\n";
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Gets latency.
     *
     * @return the latency
     */
    public long getLatency() {
        return latency;
    }

    /**
     * Gets response code.
     *
     * @return the response code
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Instantiates a new Request result.
     *
     * @param startTime the start time
     * @param latency the latency
     * @param responseCode the response code
     */
    public RequestResult(long startTime, long latency, int responseCode) {
        this.startTime = startTime;
        this.latency = latency;
        this.responseCode = responseCode;
    }


    @Override
    public int compareTo(RequestResult requestResult) {
        if(this.latency - requestResult.latency>0)
            return 1;
        else if(this.latency - requestResult.latency==0)
            return  0;
        else
            return -1;
    }
}