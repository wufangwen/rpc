package wfw.rpc.test.version6.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import wfw.rpc.test.version6.code.Serializer;
import wfw.rpc.test.version6.register.ServiceProvider;

/**
 * 实现RPCServer接口，负责监听与发送数据
 */
@AllArgsConstructor
public class NettyRPCServer implements RPCServer {
    private ServiceProvider serviceProvider;
    private Serializer serializer;
    @Override
    public void start(int port) {
        // netty 服务线程组boss负责建立连接， work负责具体的请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        System.out.printf("Netty服务端启动了...");
        try {
            // 启动netty服务器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 初始化
            serverBootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                    //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    //如果A+B的长度大于Backlog时，新的连接就会被TCP内核拒绝掉。
                    //所以，如果backlog过小，就可能出现Accept的速度跟不上，A,B队列满了，就会导致客户端无法建立连接。
                    //需要注意的是，backlog对程序的连接数没影响，但是影响的是还没有被Accept取出的连接
                    .option(ChannelOption.SO_BACKLOG, 256)
                    //心跳机制
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //naggle算法
                    // 在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，同时，对方接收到数据，也需要发送ACK表示确认。为了尽可能
                    //的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。这里就涉及到一个名为Nagle的算法，该算法的目的就是为了尽可能发送大
                    //块数据，避免网络中充斥着许多小数据块。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new NettyServerInitializer(serviceProvider,serializer));
            // 同步阻塞
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            // 死循环监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {
    }
}
