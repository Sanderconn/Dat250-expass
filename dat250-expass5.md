# DAT250, Experiment Assignment 5

For this assignment I followed the steps provided, learning about redis and integrated Redis as a cache in my Spring Boot app

### Implementation
I implemented a small Redis cache for poll results. For each poll I store a Redis Hash, where each field maps a voteOptionId to its current count. When the results endpoint is called, it first tries HGETALL on that key. If the hash exists, the counts are returned immediately. On a cache miss, the server aggregates the counts from the in-memory store, writes them back with a single HSET, and sets a short TTL (120 seconds) on the key so stale data expires automatically. Whenever a vote is cast or changed, the cache is updated in place using HINCRBY, which is atomic on the Redis side and keeps the counters hot without re-computing everything. This gives fast reads for popular polls while keeping the implementation simple.

### Technical problems
Installation and learning Redis went smoothly, though it took some time since it was new to me.
I also had some issues with the CI tests as they passed locally but failed in the GitHub actions. I solved this by starting a Redis service in the workflow and pointing Spring to it. That way, the tests passed both locally and in GitHub Actions. 
