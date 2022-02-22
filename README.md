抢红包

![表结构](./db.png)

示例来源：<<分布式中间件技术实战：Java版>> 第四章

学习目标： Redis分布式锁在高并发场景下的运用


## 普通版本分支
通过 jMeter 压力测试时（./TestPlan.jmx），会出现问题数据
```
2022-02-22 20:24:24.177  INFO 15572 --- [o-8080-exec-262] c.e.r.service.impl.RedPacketService      : 用户抢到红包了，userId=[10002],redPacket=[red:1001:27474708893200],amount=[226]
2022-02-22 20:24:24.177  INFO 15572 --- [o-8080-exec-320] c.e.r.service.impl.RedPacketService      : 用户抢到红包了，userId=[10002],redPacket=[red:1001:27474708893200],amount=[199]
2022-02-22 20:24:24.177  INFO 15572 --- [o-8080-exec-144] c.e.r.service.impl.RedPacketService      : 用户抢到红包了，userId=[10004],redPacket=[red:1001:27474708893200],amount=[98]
2022-02-22 20:24:24.177  INFO 15572 --- [o-8080-exec-232] c.e.r.service.impl.RedPacketService      : 用户抢到红包了，userId=[10003],redPacket=[red:1001:27474708893200],amount=[134]
2022-02-22 20:24:24.177  INFO 15572 --- [o-8080-exec-265] c.e.r.service.impl.RedPacketService      : 用户抢到红包了，userId=[10001],redPacket=[red:1001:27474708893200],amount=[343]
```

比如，上面的日志可以看到 10002 用户抢了两个红包。


## 使用Redis分布式锁
普通版本之所以出现抢到红包的情况，原因是高并发时，同一用户的两次抢红包请求都通过了抢红包方法的检测。

可以使用 Redis 的分布式锁，来串行化同一用户的并发请求。（由于当前应用只部署在一台机器上，也可以使用JVM锁来解决）

Redis底层架构是采用单线程进行设计的，因而它提供的这些操作也是单线程的，即其操作具备原子性。而所谓的原子性，指的是同一时刻只能有一个线程处理核心业务逻辑，当有其他线程对应的请求过来时，如果前面的线程没有处理完毕，那么当前线程将进入等待状态（堵塞），直到前面的线程处理完毕。


通过Redis的原子操作setIfAbsent()方法对该业务逻辑加分布式锁，表示如果当前的Key不存在于缓存中，则设置其对应的Value，该方法的操作结果返回True；如果当前的Key已经存在于缓存中，则设置其对应的Value失败，即该方法的操作结果将返回False。由于该方法具备原子性（单线程）操作的特性，因而当多个并发的线程同一时刻调用setIfAbsent()时，Redis的底层是会将线程加入“队列”排队处理的。


```
// 上分布式锁
String lockKey = redPacket + ":" + userId + ":lock";
Boolean lock = valueOperations.setIfAbsent(lockKey, redPacket);
redisTemplate.expire(lockKey, 24L, TimeUnit.HOURS);

if (lock) {
    // 拆红包
}
```

上面的代码对于1个红包相同用户的多次抢红包请求，会排队。只有第1个到达setIfAbsent方法的请求会获取到锁。所以可以控制住1个用户对1个红包只又及抢一次。
