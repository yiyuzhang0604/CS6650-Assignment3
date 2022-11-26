import io.swagger.client.model.GeneratedRequest;
import io.swagger.client.model.LiftRide;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Producer implements Runnable {
    protected BlockingQueue queue = null;
    private static final int TOTAL_REQUEST = 200000;
    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < TOTAL_REQUEST; i++) {
            try {
                LiftRide liftRide = new LiftRide();
                liftRide.setLiftID(getRandomLiftId());
                liftRide.setTime(getRandomTime());
                GeneratedRequest request = new GeneratedRequest(liftRide, getRandomSkierId(),getRandomResortId(), 2022, 1);
                queue.put(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int getRandomTime() {
        return ThreadLocalRandom.current().nextInt(1, 361);
    }

    private int getRandomLiftId() {
        return ThreadLocalRandom.current().nextInt(1, 41);
    }

    private int getRandomSkierId() {
        return ThreadLocalRandom.current().nextInt(1, 100001);
    }


    private int getRandomResortId() {
        return ThreadLocalRandom.current().nextInt(1, 11);
    }
}
