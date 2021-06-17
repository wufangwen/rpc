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
class Solution {
    public String addStrings(String num1, String num2) {
        int length = num1.length()-1;
        int length1 = num2.length()-1;
        int  a=0;
        int b=0;
        int c=0;
        StringBuilder sb=new StringBuilder();
        while(length>=0 &&length1>=0){
            if(length>=0){
                a= (int) Integer.parseInt(String.valueOf(num1.charAt(length)));
            }else {
                a=0;
            }
            if(length1>=0){
                b= (int) Integer.parseInt(String.valueOf(num2.charAt(length1)));
            }else {
                b=0;
            }
            int sum=a+b+c;
            c=sum/10;
            int d=sum%10;
            sb.append(d);
        }
    if (c>0){
        sb.append(c);
    }
    return  sb.reverse().toString();

    }
}