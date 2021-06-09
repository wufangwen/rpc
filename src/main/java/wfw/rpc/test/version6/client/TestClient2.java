package wfw.rpc.test.version6.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import wfw.rpc.test.version6.code.ProtobufSerializer;
import wfw.rpc.test.version6.common.Blog;
import wfw.rpc.test.version6.common.User;
import wfw.rpc.test.version6.common.channelManage;
import wfw.rpc.test.version6.service.BlogService;
import wfw.rpc.test.version6.service.UserService;
import wfw.rpc.test.version6.service.sendMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class TestClient2 {
    public static void main(String[] args) throws IOException {
        // 构建一个使用java Socket/ netty/....传输的客户端
        RPCClient rpcClient = new NettyRPCClient(new ProtobufSerializer());
        // 把这个客户端传入代理客户端
        RPCClientProxy rpcClientProxy = new RPCClientProxy(rpcClient, UUID.randomUUID().toString());
        // 代理客户端根据不同的服务，获得一个代理类， 并且这个代理类的方法以或者增强（封装数据，发送请求）
        UserService userService = rpcClientProxy.getProxy(UserService.class);
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new LoggingHandler());

            //channel表示与服务端的Connection
            Channel channel = bootstrap.connect("localhost",8899).sync().channel();
            //不断读取客户端输入的内容
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            for(;;){
                channel.writeAndFlush(br.readLine() + "\r\n");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }



    }
}
