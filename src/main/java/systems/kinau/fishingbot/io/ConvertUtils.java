/*
 * Created by David Luedtke (MrKinau)
 * 2019/8/29
 */
package systems.kinau.fishingbot.io;

public class ConvertUtils {

    public static Object convert(String value, Class type) {
        if(type.isAssignableFrom(String.class)) {
            return value;
        } else if(type.isAssignableFrom(double.class)) {
            return Double.valueOf(value);
        } else if(type.isAssignableFrom(boolean.class)) {
            return Boolean.valueOf(value);
        } else if(type.isAssignableFrom(byte.class)) {
            return Byte.valueOf(value);
        } else if(type.isAssignableFrom(float.class)) {
            return Float.valueOf(value);
        } else if(type.isAssignableFrom(int.class)) {
            return Integer.valueOf(value);
        } else if(type.isAssignableFrom(long.class)) {
            return Long.valueOf(value);
        }
        return null;
    }
}
