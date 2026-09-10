import java.util.Scanner;
import java.util.Random;

public class Customer extends User {
    Scanner mid= new Scanner(System.in);
    private String customerId;
    private String level;
    private String registerTime;
    private double totalConsume;
    private String phone;
    private String email;
    public String getCustomerId(){
        return customerId;
    }
    public String getLevel(){
        return level;
    }
    public String getRegisterTime(){
        return registerTime;
    }
    public double  getTotalConsume(){
        return totalConsume;
    }
    public String getPhone(){
        return phone;
    }
    public String getEmail(){
        return email;
    }
    public void setCustomerId(String customerId){
        this.customerId=customerId;
    }
    public void setLevel(String level){
        this.level=level;
    }
    public void setRegisterTime(String registerTime){
        this.registerTime=registerTime;
    }
    public void setTotalConsumet(double totalConsume){
        this.totalConsume=totalConsume;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public void setEmail(String email){
        this.email=email;
    }
    void forgetPassword(){
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String higherCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String symbol = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        String all = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";
        Random random = new Random();
        int Length = 10;
        char c1 = lowerCase.charAt(random.nextInt(lowerCase.length()));
        char c2 = digits.charAt(random.nextInt(digits.length()));
        char c3 = symbol.charAt(random.nextInt(symbol.length()));
        char c4 = higherCase.charAt(random.nextInt(higherCase.length()));
        StringBuilder password = new StringBuilder();
        password.append(c1);
        password.append(c2);
        password.append(c3);
        password.append(c4);
        for (int i = 4; i <= Length; i++) {
            password.append(all.charAt(random.nextInt(all.length())));
        }
        char[] arr = password.toString().toCharArray();
        for (int i = 0; i < arr.length; i++) {
            int randomNum = random.nextInt(arr.length);
            char temp = arr[i];
            arr[i] = arr[randomNum];
            arr[randomNum] = temp;
        }
        String resetPassword = new String(arr);
        this.setPassword(resetPassword);
        System.out.println("已经向你的注册邮箱发送了新的重置密码，密码为"+resetPassword+"。");
    }
}
