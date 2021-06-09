package wfw.rpc.test.version6.service;

import wfw.rpc.test.version6.common.Blog;

//新建一个BlogService service，保证呢根据对应的接口名调用对应的服务
public interface BlogService {
    Blog getBlogById(Integer id);
}
