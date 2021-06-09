package wfw.rpc.test.version6.common;

import lombok.AllArgsConstructor;
import org.reflections.Reflections;
import wfw.rpc.test.version6.annotation.MyServiceImpl;
import wfw.rpc.test.version6.register.ServiceProvider;

import java.lang.annotation.Annotation;
import java.util.Set;
//扫描注解类 serviceImpl
public class ScannServiceAnnotation {
    public void scan(String s, ServiceProvider serviceProvider,String version) {
        //guava 反射方法
        Reflections f = new Reflections(s);

        Set<Class<?>> set = f.getTypesAnnotatedWith(MyServiceImpl.class);
        for (Class<?> aClass : set) {
            Annotation annotation = aClass.getAnnotation(MyServiceImpl.class);
            String value = ((MyServiceImpl) annotation).value();
            if(version.equals(value)){
                try {
                    Object o = aClass.newInstance();
                    serviceProvider.provideServiceInterface(o);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
