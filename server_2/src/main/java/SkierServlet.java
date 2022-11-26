import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.concurrent.LinkedBlockingDeque;

@WebServlet(name = "SkierServlet", value = "/skiers/*")
public class SkierServlet extends HttpServlet {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String QUEUE_NAME = "liftride";
    private final Integer NUM_CHANNELS = 2000;
    private Connection conn;
    private BlockingQueue<Channel> pool;

    @Override
    public void init() throws ServletException {
        super.init();
        ConnectionFactory factory = new ConnectionFactory();

        //factory.setHost("54.159.142.43");
        factory.setHost("44.202.241.49");
        factory.setPort(5672);
        factory.setUsername("yiyu");
        factory.setPassword("828828");

        try {
            conn = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            System.err.println("Unable to create RabbitMQ connection");
            e.printStackTrace();
        }

        pool = new LinkedBlockingDeque<>();
        for (int i = 0; i < NUM_CHANNELS; i++) {
            try {
                Channel channel = conn.createChannel();
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                pool.add(channel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing paramterers");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)
        boolean validUrl = isUrlValid(urlParts);
        System.out.println("valid url: "+ validUrl);

        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("It works!");
        }
    }

    private boolean isUrlValid(String[] urlPath) {
        // TODO: validate the request url path according to the API spec
        // urlPath  = "/1/seasons/2022/day/1/skier/123"
        // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
        for (int i = 1; i < urlPath.length; i++) {
            // resort id
            if (i == 1) {
                int resortId = Integer.valueOf(urlPath[i]);
                if (resortId < 1 || resortId > 10 ) {
                    System.out.println("resortId wrong");
                    return false;
                }
            } else if (i == 2) {
                if (!urlPath[i].equals("seasons")) {
                    System.out.println("seasons wrong");
                    return false;
                }
            } else if (i == 3) {
                if (!urlPath[i].equals("2022")) {
                    System.out.println("2022 wrong");
                    return false;
                }
            } else if (i == 4) {
                if (!urlPath[i].equals("days")) {
                    System.out.println("day wrong");
                    return false;
                }
            } else if (i == 5) {
                if (!urlPath[i].equals("1")) {
                    return false;
                }
            } else if (i == 6) {
                if (!urlPath[i].equals("skiers")) {
                    return false;
                }

            } else if (i == 7) {
                // skier id
                int time = Integer.valueOf(urlPath[i]);
                if (time < 1 || time > 100000) {
                    return false;
                }
            }
        }
        return true;
    }

    // for assignment 2:
    // 1. validate url and json payload
    // 2. if valid: format the data and send it as payload to the rabbitmq and return success to client
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String urlPath = req.getPathInfo();
        res.setContentType("application/json");

        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("url is empty");
            return;
        }
        String[] urlParts = urlPath.split("/");
        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("url is not valid");
        } else {
            try {
                Integer resortId = Integer.parseInt(urlParts[1]);
                Integer seasonId = Integer.parseInt(urlParts[3]);
                Integer dayId = Integer.parseInt(urlParts[5]);
                Integer skierID = Integer.parseInt(urlParts[7]);

                LiftResponse liftResponse = new LiftResponse(resortId, seasonId, dayId, skierID);
                //System.out.println("liftresponse: " + liftResponse);

                // retrieve channel
                Channel channel = pool.take();
                String jsonString = gson.toJson(liftResponse);
                channel.basicPublish("", QUEUE_NAME, null, new Gson().toJson(liftResponse).getBytes());

                pool.add(channel);
                // send back response
                res.setStatus(HttpServletResponse.SC_OK);
                LiftResponse responseForUser = new Gson().fromJson(getMessageFromRequest(req), LiftResponse.class);
                res.getWriter().write("Successfully write a new lift for: " + urlParts[7]);
                System.out.println("[x]Sent '" + jsonString + "'");
                res.getWriter().write(new Gson().toJson(responseForUser));

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getMessageFromRequest(HttpServletRequest req)throws IOException {
        BufferedReader bufferedReader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }
}
