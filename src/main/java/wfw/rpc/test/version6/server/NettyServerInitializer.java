package wfw.rpc.test.version6.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.AllArgsConstructor;
import wfw.rpc.test.version6.code.JsonSerializer;
import wfw.rpc.test.version6.code.MyDecode;
import wfw.rpc.test.version6.code.MyEncode;
import wfw.rpc.test.version6.code.Serializer;
import wfw.rpc.test.version6.register.ServiceProvider;

import java.util.concurrent.TimeUnit;

/**
 * 初始化，主要负责序列化的编码解码， 需要解决netty的粘包问题
 */
@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;
    private Serializer serializer;
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
        // 消息格式 [长度][消息体], 解决粘包问题
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        // 计算当前待发送消息的长度，写入到前4个字节中
        pipeline.addLast(new LengthFieldPrepender(4));

        // json序列化器
        pipeline.addLast(new MyEncode(serializer));
        pipeline.addLast(new MyDecode());

        pipeline.addLast(new NettyRPCServerHandler(serviceProvider));
    }
}
