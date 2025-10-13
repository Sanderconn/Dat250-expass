package dat250.pollApp;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class Expass2Application {
	public static void main(String[] args) {
		SpringApplication.run(Expass2Application.class, args);
	}

}
