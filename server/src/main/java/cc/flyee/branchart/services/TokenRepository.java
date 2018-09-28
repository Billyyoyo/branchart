package cc.flyee.branchart.services;

import cc.flyee.branchart.models.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class TokenRepository {

    private final static long DURATION = 60000l;

    RedisTemplate redisTemplate;

    public TokenRepository() {
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    public Token getToken(String tokenId) {
        return (Token) redisTemplate.opsForValue().get("token_" + tokenId);
    }

    public String setToken(String userId, String uuid) {
        Token token = new Token();
        token.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
        token.setForbid(Token.FORBID_FALSE);
        token.setUuid(uuid);
        token.setUserId(userId);
        token.setLastLoginTime(System.currentTimeMillis());
        redisTemplate.opsForValue().set("token_" + token.getToken(), token);
        return token.getToken();
    }

    public void delToken(String tokenId){
        redisTemplate.delete("token_" + tokenId);
    }

}
