package gadget.agent;

import com.sun.tools.attach.VirtualMachine;
import java.util.List;
import com.sun.tools.*;
import com.sun.tools.attach.VirtualMachineDescriptor;

public class AgentC3P0JNDI {
    public static void main(String[] args) throws Throwable {
        Class.forName("sun.tools.attach.HotSpotAttachProvider");
        List<VirtualMachineDescriptor> vms = VirtualMachine.list();
        String targetPid = null;
        for (int i = 0; i < vms.size(); i++) {
            VirtualMachineDescriptor vm = vms.get(i);
            if (vm.displayName().equals("gadget.C3P0JNDI")) {
                System.out.println(vm.displayName());
                targetPid = vm.id();
                System.out.println(targetPid);
            }
        }
        VirtualMachine virtualMachine = VirtualMachine.attach(targetPid);
        virtualMachine.loadAgent("out\\JavaAgent-1.0-SNAPSHOT.jar", null);
        virtualMachine.detach();
    }
}




