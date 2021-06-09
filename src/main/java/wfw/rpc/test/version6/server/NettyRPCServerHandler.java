package wfw.rpc.test.version6.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.AllArgsConstructor;
import wfw.rpc.test.version6.common.RPCRequest;
import wfw.rpc.test.version6.common.RPCResponse;
import wfw.rpc.test.version6.register.ServiceProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 因为是服务器端，我们知道接受到请求格式是RPCRequest
 * Object类型也行，强制转型就行
 */
@AllArgsConstructor
public class NettyRPCServerHandler extends SimpleChannelInboundHandler<RPCRequest> {
    private ServiceProvider serviceProvider;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequest msg) throws Exception {
        //System.out.println(msg);
        if(msg.isHeartBeat()){
            System.out.println("这是一个心跳包");
            return;
        }
        RPCResponse response = getResponse(msg);
        ctx.writeAndFlush(response);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    RPCResponse getResponse(RPCRequest request) {
        // 得到服务名
        String interfaceName = request.getInterfaceName();
        // 得到服务端相应服务实现类
        Object service = serviceProvider.getService(interfaceName);
        // 反射调用方法
        Method method = null;
        try {
            method = service.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
            Object invoke = method.invoke(service, request.getParams());
            Class<?> returnType = method.getReturnType();
            if("void".equals(returnType.toString())){
                return RPCResponse.builder().code(120).data("void").message("已经收到").responseId(request.getRequestId()).dataType(String.class).build();
            }
            return RPCResponse.success(invoke,request.getRequestId());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            //如果是容错机制，这里可以回调容错方法
            return RPCResponse.fail();
        }
    }
   @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {


       if(evt instanceof  ChannelInputShutdownReadComplete){
           System.out.println("连接关闭，断开连接 " );
       }
       if (evt instanceof IdleStateEvent) {
           IdleStateEvent event = (IdleStateEvent) evt;
           if (event.state() == IdleState.READER_IDLE) {
               System.out.println("长时间没有收到心跳包，断开连接 " );
               ctx.close();
           }
       }
   }
}
