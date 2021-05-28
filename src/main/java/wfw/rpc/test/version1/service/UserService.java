package wfw.rpc.test.version1.service;


import wfw.rpc.test.version1.common.User;

public interface UserService {
    // 客户端通过这个接口调用服务端的实现类
    User getUserByUserId(Integer id);
}
