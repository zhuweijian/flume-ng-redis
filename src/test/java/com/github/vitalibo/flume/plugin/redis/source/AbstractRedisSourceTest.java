package com.github.vitalibo.flume.plugin.redis.source;

import com.github.vitalibo.flume.plugin.redis.RedisClient;
import org.apache.flume.Context;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.lifecycle.LifecycleState;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import redis.clients.jedis.exceptions.JedisException;

public class AbstractRedisSourceTest {

    @Mock
    private RedisClient client;
    @Mock
    private ChannelProcessor mockChannelProcessor;

    private AbstractRedisSource source;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        source = new AbstractRedisSource(client) {
        };
        source.setChannelProcessor(mockChannelProcessor);
    }

    @Test
    public void testConfigure() {
        Context context = new Context();

        source.configure(context);

        Mockito.verify(client).configure(Mockito.eq(context));
        Assert.assertNotEquals(source.getLifecycleState(), LifecycleState.ERROR);
    }

    @Test
    public void testStart() {
        source.start();

        Mockito.verify(client).openConnection();
        Assert.assertNotEquals(source.getLifecycleState(), LifecycleState.ERROR);
    }

    @Test
    public void testFailStart() {
        Mockito.doThrow(JedisException.class).when(client).openConnection();

        source.start();

        Mockito.verify(client).openConnection();
        Assert.assertEquals(source.getLifecycleState(), LifecycleState.ERROR);
    }

}
