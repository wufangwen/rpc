package wfw.rpc.test.version6.server;

import org.reflections.Reflections;
import wfw.rpc.test.version6.annotation.MyServiceImpl;

import java.util.Set;

public class test {
    public static void main(String[] args) {
        Reflections f = new Reflections("wfw.rpc.test.version6.service");
        Set<Class<?>> set = f.getTypesAnnotatedWith(MyServiceImpl.class);
        for (Class<?> aClass : set) {
            try {
                Object o = aClass.newInstance();
                System.out.println(o);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
