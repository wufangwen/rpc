package wfw.rpc.test.version6.server;

import org.reflections.Reflections;
import wfw.rpc.test.version6.annotation.MyServiceImpl;
import wfw.rpc.test.version6.code.*;
import wfw.rpc.test.version6.common.ScannServiceAnnotation;
import wfw.rpc.test.version6.register.ServiceProvider;

import java.util.Set;

public class TestServer {
    public static void main(String[] args) {

        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 8899);
        ScannServiceAnnotation sc=new ScannServiceAnnotation();
        sc.scan("wfw.rpc.test.version6.service",serviceProvider,"a");
        RPCServer RPCServer = new NettyRPCServer(serviceProvider,new ProtobufSerializer());
        RPCServer.start(8899);
    }
}
// 这里先不去讨论实现其中的细节，因为这里还应该进行优化，我们先去把服务端代码松耦合，再回过来讨论
