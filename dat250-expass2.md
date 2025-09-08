# DAT250, Experiment Assignment 2

For this Assignment I followed the steps provided.
For the HTTP client for testing, I landed on using the VS Code Rest Client.

### Implementation
For the implementation, I started with the Java bean classes for the domain. 
I added some parameters that weren't present in the domain model, such as id's so that it would be easier to identify and manage the objects. Each entity has a Long id field which made it easy to store them in a hashmap and reference them when handling requests.

After that I handled the PollManager class. This class manages the in-memory storage of all domain objects. It provides methods to add, retrieve, list, and remove users, polls, and votes. I also added simple AtomicLong counters to generate unique IDs for each new entity.

After setting up the domain model and manager, I moved on to the controllers. I implemented UserController, PollController, and VoteController. These classes are responsible for handling incoming HTTP requests at the API endpoints, while the logic for managing users, polls, and votes is delegated to the PollManager.

### Testing
For testing, I first wrote a .http file for the VS Code REST Client to manually send sequences of requests and check the responses. Later, I automated the same scenarios as integration tests using Spring Boot’s TestRestTemplate

### Technical Problems
I did not really experience any significant technical problems during installation. 

### Pending Issues
I do not think there are any pending issues that need fixing, but I would like to do the optional steps 6 and 7 at some point.