import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.GeneratedRequest;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
public class PhaseRunner implements Runnable {
    private String name;
    private int threadNum;
    private SkiersApi skiersApi;
    private BlockingQueue queue;
    private WriteToCSV csvWriter;
    private CountDownLatch countDownLatch;
    private Queue<RequestResult> globalQueue;
    private ConcurrentCount successCount;
    private ConcurrentCount failCount;
    public PhaseRunner(String name, int threadNum, SkiersApi skiersApi, BlockingQueue queue, CountDownLatch countDownLatch, WriteToCSV csvWriter, Queue<RequestResult> globalQueue, ConcurrentCount successCount, ConcurrentCount failCount) {
        this.name = name;
        this.threadNum = threadNum;
        this.skiersApi = skiersApi;
        this.queue = queue;
        this.countDownLatch = countDownLatch;
        this.csvWriter = csvWriter;
        this.globalQueue = globalQueue;
        this.successCount = successCount;
        this.failCount = failCount;
    }

    @Override
    public void run() {
        int localSuccessCount = 0;
        int localFailCount = 0;
        for (int i = 0; i < 1000; i ++) {
            try {
                String[] records = new String[4];
                GeneratedRequest request = (GeneratedRequest) queue.take();
                // send post request
                long beforePostTime = System.currentTimeMillis();
                //skiersApi.writeNewLiftRide(request.getLiftRide(), request.getResortId(), request.getSeasonId().toString(), request.getDayId().toString(), request.getSkierId());
                // Received HTTP response
                try {
                    ApiResponse<Void> apiResponse = skiersApi.writeNewLiftRideWithHttpInfo(request.getLiftRide(), request.getResortId(),
                            request.getSeasonId().toString(), request.getDayId().toString(), request.getSkierId());
                    // Record the timestamp that receive post response
                    int responseCode = apiResponse.getStatusCode();
                    long receivePostTime = System.currentTimeMillis();
                    long latency = receivePostTime - beforePostTime;
                    records[0] = String.valueOf(beforePostTime);
                    records[1] = "POST";
                    records[2] = String.valueOf(latency);
                    records[3] = String.valueOf(responseCode);
                    RequestResult requestResult = new RequestResult(beforePostTime, latency, responseCode);
                    globalQueue.add(requestResult);
                    localSuccessCount++;
                } catch (ApiException e) {
                    e.printStackTrace();
                    RequestResult requestResult = new RequestResult(beforePostTime, System.currentTimeMillis() - beforePostTime, e.getCode());
                    records[0] = String.valueOf(beforePostTime);
                    records[1] = "POST";
                    records[2] = String.valueOf(System.currentTimeMillis() - beforePostTime);
                    records[3] = String.valueOf(e.getCode());
                    globalQueue.add(requestResult);
                    localFailCount++;
                }
                csvWriter.saveRecord(records);


            } catch (InterruptedException e ) {
                System.out.println("Thrown InterruptedException: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        successCount.incrementBy(localSuccessCount);
        failCount.incrementBy(localFailCount);
        countDownLatch.countDown();
    }
}
