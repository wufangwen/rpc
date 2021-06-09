package wfw.rpc.test.version6.server;

import wfw.rpc.test.version6.code.JsonSerializer;
import wfw.rpc.test.version6.common.ScannServiceAnnotation;
import wfw.rpc.test.version6.service.BlogService;
import wfw.rpc.test.version6.register.ServiceProvider;
import wfw.rpc.test.version6.service.UserService;
import wfw.rpc.test.version6.service.sendMessage;
import wfw.rpc.test.version6.serviceImpl.BlogServiceImpl;
import wfw.rpc.test.version6.serviceImpl.UserServiceImpl;
import wfw.rpc.test.version6.serviceImpl.sendMessageImpl;

public class TestServer2 {
    public static void main(String[] args) {

        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 8545);
        ScannServiceAnnotation sc=new ScannServiceAnnotation();
        sc.scan("wfw.rpc.test.version6.service",serviceProvider,"a");
        RPCServer RPCServer = new NettyRPCServer(serviceProvider,new JsonSerializer());
        RPCServer.start(8545);
    }
}