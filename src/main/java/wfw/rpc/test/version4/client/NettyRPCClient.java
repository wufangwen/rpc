package wfw.rpc.test.version4.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import wfw.rpc.test.version4.common.RPCRequest;
import wfw.rpc.test.version4.common.RPCResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 实现RPCClient接口
 */
public class NettyRPCClient implements RPCClient {
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    private String host;
    private int port;

    public NettyRPCClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // netty客户端初始化，重复使用
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 256)
                //心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //naggle算法
                // 在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，同时，对方接收到数据，也需要发送ACK表示确认。为了尽可能
                //的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。这里就涉及到一个名为Nagle的算法，该算法的目的就是为了尽可能发送大
                //块数据，避免网络中充斥着许多小数据块。

                .handler(new NettyClientInitializer());
    }

    /**
     * 这里需要操作一下，因为netty的传输都是异步的，你发送request，会立刻返回， 而不是想要的相应的response
     */
    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        RPCResponse rpcResponse = null;
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            // 发送数据
            CompletableFuture<RPCResponse> resultFuture = new CompletableFuture<>();
            UnprocessedRequests.put(request.getRequestId(), resultFuture);
            channel.writeAndFlush(request).addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    System.out.println("future1.isSuccess())");
                } else {

                }
            });
            System.out.println("我与输出那个先");
            rpcResponse = resultFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return rpcResponse;
    }
}
