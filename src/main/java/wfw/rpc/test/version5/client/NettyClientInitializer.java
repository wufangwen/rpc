package wfw.rpc.test.version5.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import wfw.rpc.test.version5.common.JsonSerializer;
import wfw.rpc.test.version5.common.MyDecode;
import wfw.rpc.test.version5.common.MyEncode;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 消息格式 [长度][消息体]
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        // 计算当前待大宋消息的长度，写入到前4个字节中
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new MyDecode());

        pipeline.addLast(new MyEncode(new JsonSerializer()));

        pipeline.addLast(new NettyClientHandler());
    }
}
