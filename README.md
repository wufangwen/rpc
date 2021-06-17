### version1
#### 流程:
最简单的版本，定义公共接口Service与User,即功能与消息类，接着
定义了Client与Server端。首先通过开启serverSocket监听端口，while（true）
循环，accept等待连接客户端请求，收到请求获取输入输出流，用输入流读取id，
调用方法查询到数据再写入到输出流，客户端读取即可。
#### 缺点
1. 因为我们是new User来处理请求的，事先我们是不知道处理什么方法，调用什么类
以及参数类型是什么，所以需要封装请求参数，需要 类名，方法名，
参数，参数类型。恰好，可以通过反射中的invoke方法获取method ,args ,interfaceName,paramsTypes
2. 同样，返回值也要封装，两种情况，成功--》返回信息，失败--》返回错误信息
3. 不够抽象，host与port都写死了，应该封装成对象。
### version2
1. 定义更加通用的消息格式：Request 与Response格式， 从此可能调用不同的方法，与返回各种类型的数据。
2. 使用了动态代理进行不同服务方法的Request的封装，
3. 客户端更加松耦合，不再与特定的Service，host，port绑定
#### 缺点
1.默认绑定的是userService服务，如果多一个服务怎么办？去哪找？所以我们可以建一个map
根据客户端的请求去map中找接口，从而定位到实现类处理逻辑。
2. 服务端处理，用runable实现类封装处理
3. 监听功能复杂，需要松耦合
### version3
1. 定义Sevicesprovide 集中管理服务接口
2. 封装了workThread任务处理类
#### 缺点
1. 传统的BIO与线程池网络传输性能低，推荐使用高性能网络框架netty
### version4
此版本我们完成了客户端的重构，使之能够支持多种版本客户端的扩展（实现RPCClient接口）
并且使用netty实现了客户端与服务端的通信
#### 缺点
1. java自带序列化方式（Java序列化写入不仅是完整的类名，也包含整个类的定义，包含所有被引用的类），不够通用，不够高效
### version5
1. 自定义了JSON序列化方式
2. 定义消息格式，消息类型（2byte),序列化方式（2byte),消息长度（4Byte),解码时先读消息类型
确定消息是response还是request，然后再读序列化方式，最后读出消息长度，根据长度建立字节数组
然后读取。
#### 缺点
服务器的host 与port必须事先知道，每一个客户端都必须知道对应服务的IP与端口号，并且如果
服务挂了或者换地址了，就很麻烦，扩展性也不强。
### version6
此版本中我们加入了注册中心，终于一个完整的RPC框架三个角色都有了：服务提供者，服务消费者，注册中心。
并且加入了负载均衡机制，注册多个服务器。可以轮询访问。
### 优化
1. 手动注入impl类，使用反射+注解仿spring模式实现自动注入
```java
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();
        sendMessage sendMessage=new sendMessageImpl();
        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 8545);
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);
        serviceProvider.provideServiceInterface(sendMessage);
        RPCServer RPCServer = new NettyRPCServer(serviceProvider);
        RPCServer.start(8899);
```
以上需要手动注入太麻烦了
```java
             ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 8899);
             //自动扫描，impl类加注解就行了
             ScanServiceAnnotation sc=new ScanServiceAnnotation();
             sc.scan("wfw.rpc.test.version6.service",serviceProvider);
             RPCServer RPCServer = new NettyRPCServer(serviceProvider);
             RPCServer.start(8899);
```
2. 新加了几个序列化算法，HessianSerializer，KryoSerializer，ProtobufSerializer
3. 实现心跳机制，每隔几秒没有读写事件就输出语句。


