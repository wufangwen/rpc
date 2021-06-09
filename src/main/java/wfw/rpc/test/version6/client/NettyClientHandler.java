package wfw.rpc.test.version6.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import wfw.rpc.test.version6.common.*;


import java.util.concurrent.CompletableFuture;


public class NettyClientHandler extends SimpleChannelInboundHandler<RPCResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCResponse msg) throws Exception {
           //将结果存在future里面，然后获取
           UnprocessedRequests.complete(msg);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
         //心跳机制，如果没有写事件就关闭。
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                Channel channel = ctx.channel();
                RPCRequest build = RPCRequest.builder().isHeartBeat(true).build();
                CompletableFuture<RPCResponse> resultFuture = new CompletableFuture<>();
                UnprocessedRequests.put("0", resultFuture);
                channel.writeAndFlush(build).sync().addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("我激活了");
        ctx.fireChannelActive();
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("我关闭了");
        ctx.fireChannelInactive();

    }
}
