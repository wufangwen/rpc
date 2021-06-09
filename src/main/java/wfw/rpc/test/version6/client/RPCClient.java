package wfw.rpc.test.version6.client;


import wfw.rpc.test.version6.common.RPCRequest;
import wfw.rpc.test.version6.common.RPCResponse;

// 共性抽取出来
public interface RPCClient {
    RPCResponse sendRequest(RPCRequest response,String uuid);

}
