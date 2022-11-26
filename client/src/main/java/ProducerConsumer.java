import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.GeneratedRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class ProducerConsumer {
    // tomcat server ec2 ip address
    public static final String BASE_URL = "http://localhost:8080/server_2_war_exploded";

    public static void main(String[] args) throws Exception{
        System.out.println("Start running....");
        // Take a timestamp before starting producer thread
        long startTimeStamp = System.currentTimeMillis();
        BlockingQueue<GeneratedRequest> queue = new LinkedBlockingQueue<GeneratedRequest>(1024);
        ConcurrentCount successCount = new ConcurrentCount();
        ConcurrentCount failCount = new ConcurrentCount();

        // when phase 1 is finished, count down
        CountDownLatch latch = new CountDownLatch(1);
        SkiersApi skiersApi = new SkiersApi();
        skiersApi.getApiClient().setBasePath(BASE_URL);

        // List to store request result
        Queue<RequestResult> globalQueue = new ConcurrentLinkedQueue<RequestResult>();

        Producer producer = new Producer(queue);
        new Thread(producer).start();
        File csvFile = new File("record.txt");
        WriteToCSV csvWriter = new WriteToCSV(csvFile);

       // start phase 1
        CountDownLatch phaseTwoCount = new CountDownLatch(1);
        List<PhaseRunner> threads = new ArrayList<>();
        Phase phase1 = new Phase("phase1", 32, skiersApi, queue, phaseTwoCount, csvWriter, globalQueue, successCount, failCount, threads);
        phase1.startPhase();

        // Take a timestamp after all threads completes running
        long endTimeStamp = System.currentTimeMillis();
        double totalPhaseOneTime = (double)(endTimeStamp - startTimeStamp);

        // start phase 2
        CountDownLatch phaseThreeCountDown = new CountDownLatch(16);
        List<PhaseRunner> threads2 = new ArrayList<>();
        Phase phase2 = new Phase("phase2", 168, skiersApi, queue, phaseThreeCountDown, csvWriter,globalQueue, successCount, failCount, threads2);
        phase2.startPhase();
        phaseThreeCountDown.await();

        phase1.waitForPhaseToFinish();
        phase2.waitForPhaseToFinish();
        //phase1.join();
        //phase2.join();


        long phaseTwoEndTime = System.currentTimeMillis();
        double totalPhaseTwo = (double)(phaseTwoEndTime - startTimeStamp);
        double totalDurationInSeconds = (totalPhaseTwo + totalPhaseOneTime)/1000.00;
        System.out.println("total duration in seconds " + totalDurationInSeconds);
        System.out.println("Throughput in seconds: " + (200000/totalDurationInSeconds));

        // start analyzer
        Analyzer analyzer = new Analyzer(globalQueue);
        System.out.println("Total number of successful requests: " + successCount.getCount());
        System.out.println("Total number of unsuccessful requests: " + failCount.getCount());

        double meanResponseTime = analyzer.getMeanResponseTime();
        System.out.println("Mean response time: " + meanResponseTime);
        double medianResponseTime = analyzer.getMedianResponseTime();
        System.out.println("Median response time: " + medianResponseTime);
        double p99ResponseTime = analyzer.getP99Response();
        System.out.println("p99 response time: " + p99ResponseTime);
        long minResponseTime = analyzer.getMinResponse();
        System.out.println("min response time: " + minResponseTime);
        long maxResponseTime = analyzer.getMaxResponse();
        System.out.println("max response time: " + maxResponseTime);
    }
}
