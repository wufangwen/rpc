package wfw.rpc.test.version4.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wfw.rpc.test.version4.common.RPCResponse;

import java.util.Date;

public class NettyClientHandler extends SimpleChannelInboundHandler<RPCResponse> {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCResponse msg) throws Exception {
        // 接收到response, 给channel设计别名，让sendRequest里读取response

        UnprocessedRequests.complete(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("进来了吗！！！");
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.WRITER_IDLE){
                System.out.println("客户端循环心跳监测发送: "+new Date());

            }
        }
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {


    }
}
