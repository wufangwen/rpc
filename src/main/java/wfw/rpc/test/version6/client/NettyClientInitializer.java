package wfw.rpc.test.version6.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.AllArgsConstructor;

import wfw.rpc.test.version6.code.MyDecode;
import wfw.rpc.test.version6.code.MyEncode;
import wfw.rpc.test.version6.code.Serializer;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    public Serializer serializer;
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //写5s空闲
        pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
        // 消息格式 [长度][消息体]
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        // 计算当前待发送消息的长度，写入到前4个字节中
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new MyDecode());

        pipeline.addLast(new MyEncode(serializer));

        pipeline.addLast(new NettyClientHandler());
    }
}
