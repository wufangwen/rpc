package wfw.rpc.test.version6.code;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wfw.rpc.test.version6.common.MessageType;
import wfw.rpc.test.version6.common.RPCRequest;
import wfw.rpc.test.version6.common.RPCResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoSerializer implements Serializer {

    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RPCResponse.class);
        kryo.register(RPCRequest.class);
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            System.out.println("反序列化时有错误发生");
        }
        return null;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj=null;

        switch (messageType){
            case 0:
                try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                     Input input = new Input(byteArrayInputStream)) {
                    System.out.println(RPCRequest.class);
                    Kryo kryo = kryoThreadLocal.get();

                    Object o = kryo.readObject(input,RPCRequest.class );
                    kryoThreadLocal.remove();
                    obj= o;
                } catch (Exception e) {
                    e.printStackTrace();

                }
            case 1:

                try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                     Input input = new Input(byteArrayInputStream)) {
                    Kryo kryo = kryoThreadLocal.get();
                    Object o = kryo.readObject(input, RPCResponse.class);

                    kryoThreadLocal.remove();
                    obj= o;
                } catch (Exception e) {
                }
            default:
                System.out.println("暂时不支持此种消息");

        }
        return obj;
    }

    @Override
    public int getType() {
        return 2;
    }

}
