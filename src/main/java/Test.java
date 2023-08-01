public class Test {
    String a;
    static {
        System.out.println(1);
    }

    public static void main(String[] args) {
        String a ="java.util.Properties properties = new java.util.Properties();\n" +
                "        javax.naming.CompoundName compoundName = new javax.naming.CompoundName(\"rmi://127.0.0.1:6666/calc\",properties);" +
                "contextName=compoundName;";
        System.out.println(a);
    }
    public Test(String a){
        this.a = a;
    }
    private void DiaoWo(){
        System.out.println("DiaoWo");
    }

    private void DiaoDiaoWo(String a){
        System.out.println(a);
    }
}
