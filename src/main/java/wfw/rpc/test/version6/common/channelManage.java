package wfw.rpc.test.version6.common;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//可以用channelGroup管理
public class channelManage {
    private static Map<String, Channel> channels = new ConcurrentHashMap<>();

    public static Channel get(String s) {
        Channel channel = channels.get(s);
        if(channel==null){
            return null;
        }else {
            return channel;
        }

    }

    public static void put(String s, Channel channel) {
        channels.put(s,channel);
    }
    public static int   size(){
       return channels.size();
    }
}
