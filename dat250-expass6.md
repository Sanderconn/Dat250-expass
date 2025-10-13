# DAT250, Experiment Assignment 6

### implementation

I added Spring AMQP and a few localhost RabbitMQ settings, then turned on @EnableRabbit so @RabbitListener methods get picked up. Since votes can be anonymous, I also made Vote.voter optional.

On the messaging side I made a small AmqpConfig that provides a JSON converter, a RabbitTemplate, an AmqpAdmin, and one durable queue for the app. I also added a AmqpTopicsService. When a poll is created, the server declares that exchange and binds our queue to it with the routing key vote. I trigger this from PollManager.addPoll

Then I built the consumer: a small VoteEventListener with a single @RabbitListener method. It gets a VoteEvent, logs it, creates a Vote, and updates the in-memory store. To make sure other clients also see votes that come in through my REST API, I added a small producer VoteEventPublisher. In POST /votes I save the vote and then publish a matching VoteEvent to the poll with the routing key vote.

I wrote StandAloneVotePublisher, a tiny Java main that connects to RabbitMQ and sends a JSON vote to the poll with the same routing key. Running it while the app is up shows a log line from the listener, and the /results endpoint shows the counts going up.

### Technical problems
Getting RabbitMQ running locally was fine, but wiring it into Spring took some trial and error.
This includes the listener not starting because of missing annotations and wiring, a message conversion error where the listener method expected one type while Spring was converting the payload to another, and on the REST side, some fields didn’t line up between the request and what the server actually saved, which led to incomplete events and which blew up my terminal and caused the spring boot app to crash. I also had to add a test profile so that my tests could run without rabbitmq dependencies.