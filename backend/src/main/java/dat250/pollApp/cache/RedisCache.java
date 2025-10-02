package dat250.pollApp.cache;

import java.util.Map;

import redis.clients.jedis.UnifiedJedis;

public class RedisCache {
    private final UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379");

    private String key(long pollId) {
        return "poll" + pollId + ":counts";
    }

    public Map<String, String> getCounts(long pollId){
        return jedis.hgetAll(key(pollId));
    }

    public void putCounts(long pollId, Map<Long, Integer> counts){
        String k = key(pollId);
        for (var e : counts.entrySet()){
            jedis.hset(k, String.valueOf(e.getKey()), String.valueOf(e.getValue()));
        }
        jedis.expire(k, 120);
    }

    public void increment(long pollId, long voteOptionId, long change){
        jedis.hincrBy(key(pollId), String.valueOf(voteOptionId), change);
        jedis.expire(key(pollId), 120);
    }
}
