package wfw.rpc.test.version6.serviceImpl;

import wfw.rpc.test.version6.annotation.MyServiceImpl;
import wfw.rpc.test.version6.common.Blog;
import wfw.rpc.test.version6.service.BlogService;
@MyServiceImpl("a")
// 服务端新的服务接口实现类
public class BlogServiceImpl implements BlogService {
    @Override
    public Blog getBlogById(Integer id) {
        Blog blog = Blog.builder().id(id).title("我的博客").useId(22).build();
        System.out.println("客户端查询了"+id+"博客");
        return blog;
    }
}