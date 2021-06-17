package wfw.rpc.test.version5.client;


import wfw.rpc.test.version5.common.RPCRequest;
import wfw.rpc.test.version5.common.RPCResponse;

// 共性抽取出来
public interface RPCClient {
    RPCResponse sendRequest(RPCRequest response);
}
