package wfw.rpc.test.version6.serviceImpl;

import wfw.rpc.test.version6.annotation.MyServiceImpl;
import wfw.rpc.test.version6.service.sendMessage;
@MyServiceImpl("a")
public class sendMessageImpl implements sendMessage {
    @Override
    public void send(String s) {
        System.out.println("收到信息了");
    }
}
