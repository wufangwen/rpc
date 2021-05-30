package wfw.rpc.test.version4.server;

public interface RPCServer {
    void start(int port);
    void stop();
}