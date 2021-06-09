package wfw.rpc.test.version6.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;

import wfw.rpc.test.version6.code.JsonSerializer;
import wfw.rpc.test.version6.code.Serializer;
import wfw.rpc.test.version6.common.*;
import wfw.rpc.test.version6.register.ServiceRegister;
import wfw.rpc.test.version6.register.ZkServiceRegister;

import java.net.InetSocketAddress;
import java.sql.SQLOutput;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * 实现RPCClient接口
 */

public class NettyRPCClient implements RPCClient {
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    private static final NettyClientInitializer nettyClientInitializer = new NettyClientInitializer(new JsonSerializer());
    private String host;
    private int port;
    private InetSocketAddress address;
    private ConcurrentHashMap<String ,InetSocketAddress > InetMap;
    private static final ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();
    private  static  Serializer serializer=new JsonSerializer();
    private ServiceRegister serviceRegister;
    public NettyRPCClient(Serializer serializera) {
        this.serviceRegister = new ZkServiceRegister();
        this.serializer=serializera;
        nettyClientInitializer.serializer=serializera;
    }
    // netty客户端初始化，重复使用
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(nettyClientInitializer);

    }

    /**
     * 这里需要操作一下，因为netty的传输都是异步的，你发送request，会立刻返回一个值， 而不是想要的相应的response
     */
    @Override
    public RPCResponse sendRequest(RPCRequest request,String uuid) {
        address = serviceRegister.serviceDiscovery(request.getInterfaceName());
        host = address.getHostName();
        port = address.getPort();
        //这里可以加本地缓存，即查到地址后放到本地缓存中，以防万一zk宕机

        RPCResponse rpcResponse=null;
        try {
            //这里可以传客户端的 id一对一绑定
            Channel channel = channelManage.get(host + port+uuid);
            if(channel==null){
                ChannelFuture channelFuture  = bootstrap.connect(host, port).sync();
                channel = channelFuture.channel();
                System.out.println(channel.remoteAddress()+"channel.remoteAddress()");
                channelManage.put(host + port+uuid,channel);
            }

            CompletableFuture<RPCResponse> resultFuture = new CompletableFuture<>();
            UnprocessedRequests.put(request.getRequestId(), resultFuture);
            channel.writeAndFlush(request);

            rpcResponse = resultFuture.get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rpcResponse;
    }
}
