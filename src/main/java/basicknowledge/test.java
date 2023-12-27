//package basicknowledge;
//
//public class test {
//
//        private int privateField = 1;
//
//        public static void main(String[] args) throws Exception {
//            Example example = new Example();
//
//            // 获取Unsafe实例
//            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
//            theUnsafeField.setAccessible(true);
//            Unsafe unsafe = (Unsafe) theUnsafeField.get(null);
//
//            // 获取privateField字段的偏移地址
//            Field field = Example.class.getDeclaredField("privateField");
//            long offset = unsafe.objectFieldOffset(field);
//
//            // 修改privateField的值
//            unsafe.putInt(example, offset, 123);
//
//            // 验证值是否更改
//            System.out.println(example.privateField); // 应输出123
//        }
//    }
//
//}
