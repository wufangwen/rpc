package wfw.rpc.test.version6.common;


import wfw.rpc.test.version6.common.RPCResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ziyang
 */
public class UnprocessedRequests {

    private static ConcurrentHashMap<String, CompletableFuture<RPCResponse>> unprocessedResponseFutures = new ConcurrentHashMap<>();

    public static void put(String requestId, CompletableFuture<RPCResponse> future) {
        unprocessedResponseFutures.put(requestId, future);
    }

    public static void remove(String requestId) {
        unprocessedResponseFutures.remove(requestId);
    }

    public static void complete(RPCResponse rpcResponse) {
        CompletableFuture<RPCResponse> future = unprocessedResponseFutures.remove(rpcResponse.getResponseId());
        if (null != future) {
            future.complete(rpcResponse);
        } else {
            throw new IllegalStateException();
        }
    }

}
