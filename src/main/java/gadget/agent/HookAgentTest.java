//package gadget.agent;
//
//import com.sun.tools.attach.*;
//
//import java.io.IOException;
//import java.util.List;
//
//public class HookAgentTest {
//    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
//
//        List<VirtualMachineDescriptor> list = VirtualMachine.list(); //
//        for (VirtualMachineDescriptor vir : list) {
//            System.out.println(vir.displayName());//打印JVM加载类名
//            if (vir.displayName().contains("fastjava")) {
//                String pid = vir.id();
//                VirtualMachine attach = VirtualMachine.attach(pid);
//                String jarName = "E:\\MemoryShell\\AgentMemShell\\target\\AgentMemShell-1-jar-with-dependencies.jar";
//                attach.loadAgent(jarName);
//                attach.detach();
//                break;
//            }
//        }
//    }
//}
