package wfw.rpc.test.version4.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.AllArgsConstructor;
import wfw.rpc.test.version4.common.ServiceProvider;

import java.util.concurrent.TimeUnit;

/**
 * 初始化，主要负责序列化的编码解码， 需要解决netty的粘包问题
 */
@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 消息格式 [长度][消息体], 解决粘包问题
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        // 计算当前待发送消息的长度，写入到前4个字节中
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new IdleStateHandler(8, 0, 0, TimeUnit.SECONDS));
        // 这里使用的还是java 序列化方式， netty的自带的解码编码支持传输这种结构
        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
            @Override
            public Class<?> resolve(String className) throws ClassNotFoundException {
                return Class.forName(className);
            }
        }));

        pipeline.addLast(new NettyRPCServerHandler(serviceProvider));
    }
}
