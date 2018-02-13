package com.fynn.smsforwarder.common;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Fynn
 * @date 18/2/13
 */
public class Reflects {

    /**
     * 类名转 Class
     *
     * @param clsName
     * @return
     */
    public static Class<?> forClassName(String clsName) {
        try {
            return Class.forName(clsName);
        } catch (ClassNotFoundException var2) {
            return null;
        }
    }

    /**
     * @see #invokeStaticMethod
     *
     * @param clsName
     * @param methodName
     * @param argsTypes
     * @param args
     * @return
     */
    public static <T> T invokeStaticMethod(
            String clsName, String methodName, Class<?>[] argsTypes, Object[] args) {
        return invokeStaticMethod(forClassName(clsName), methodName, argsTypes, args);
    }

    /**
     * 调用静态方法
     *
     * @param cls
     * @param methodName
     * @param argsTypes
     * @param args
     * @return
     */
    public static <T> T invokeStaticMethod(
            Class<?> cls, String methodName, Class<?>[] argsTypes, Object[] args) {
        try {
            Method method = getMethod(cls, methodName, argsTypes);
            if(method != null) {
                return (T) method.invoke(null, args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取 Method 方法
     *
     * @param cls
     * @param methodName
     * @param types
     * @return
     */
    public static Method getMethod(Class<?> cls, String methodName, Class... types) {
        try {
            Method method = cls.getDeclaredMethod(methodName, types);

            if(!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            return method;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
