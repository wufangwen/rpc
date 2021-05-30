package wfw.rpc.test.version3.common;
//新建一个BlogService service，保证呢根据对应的接口名调用对应的服务
public interface BlogService {
    Blog getBlogById(Integer id);
}
