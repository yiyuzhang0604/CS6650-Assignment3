import Datastore.Resort;
import Datastore.ResortDAO;
import Datastore.Skier;
import Datastore.SkierDAO;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResortConsumer {
    private final static String QUEUE_NAME = "liftride";
    private final static Integer THREADS = 64;
    private static Map<Integer, List<LiftRide>> map;

    public static void main(String[] args) throws Exception{
        map = new ConcurrentHashMap<>();
        ConnectionFactory factory = new ConnectionFactory();
        Gson gson = new Gson();
        //factory.setHost("54.159.142.43");
        factory.setHost("44.202.241.49");
        factory.setUsername("yiyu");
        factory.setPassword("828828");
        Connection connection = factory.newConnection();
        Runnable runnable = () -> {
            Channel channel;
            try{
                channel = connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    //System.out.println("message: " + message);

                    LiftRide liftRide = gson.fromJson(message, LiftRide.class);
                    //System.out.println("skier id: " + liftRide.getSkierId());
                    System.out.println(" [x] Received '" + message + "'");

                   /* if (!map.containsKey(liftRide.getSkierId())) {
                        map.put(liftRide.getSkierId(), new LinkedList<>());

                    }
                    map.get(liftRide.getSkierId()).add(liftRide);*/

                    Resort resort = new Resort();
                    resort.setSkier_id(liftRide.getSkierId());
                    resort.setSeason(liftRide.getSeasonId());
                    resort.setDay_id(liftRide.getDayId());
                    resort.setLift_id(liftRide.getLiftId());
                    ResortDAO resortDao = new ResortDAO(resort);
                    resortDao.insertResort();
                };
                channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < THREADS; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }
}