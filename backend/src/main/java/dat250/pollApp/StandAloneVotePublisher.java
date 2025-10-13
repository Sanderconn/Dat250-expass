package dat250.pollApp;

import com.rabbitmq.client.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;

public class StandAloneVotePublisher {
  public static void main(String[] args) throws Exception {
    long pollId = Long.parseLong(args[0]);
    long optionId = Long.parseLong(args[1]);
    Long userId = (args.length > 2) ? Long.parseLong(args[2]) : null;

    ConnectionFactory f = new ConnectionFactory();
    f.setHost("localhost"); f.setPort(5672); f.setUsername("guest"); f.setPassword("guest");
    try (Connection c = f.newConnection(); Channel ch = c.createChannel()) {
        String exchange = "poll." + pollId;
        var payload = new java.util.HashMap<String,Object>();
        payload.put("pollId", pollId);
        payload.put("optionId", optionId);
        payload.put("userId", userId); 
        payload.put("publishedAt", Instant.now().toString());
        byte[] body = new ObjectMapper().writeValueAsBytes(payload);

        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
        .contentType("application/json")
        .build();

        ch.basicPublish(exchange, "vote", props, body);
        System.out.println("sent vote to " + exchange + " rk=vote");
    }
  }
}
