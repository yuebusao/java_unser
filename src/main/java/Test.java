public class Test {
    String a;
    static {
        System.out.println(1);
    }
    public Test(String a){
        this.a = a;
    }
    private void DiaoWo(){
        System.out.println("abcdefg");
    }

    private void DiaoDiaoWo(String a){
        System.out.println(a);
    }
}
