package wfw.rpc.test.version4.client;


import wfw.rpc.test.version4.common.RPCRequest;
import wfw.rpc.test.version4.common.RPCResponse;
import wfw.rpc.test.version4.common.User;
import wfw.rpc.test.version4.service.UserService;

// 共性抽取出来
public interface RPCClient {
    RPCResponse sendRequest(RPCRequest response);
}
