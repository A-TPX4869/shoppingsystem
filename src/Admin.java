
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Admin extends User {
    Scanner mid = new Scanner(System.in);
    ArrayList<Customer> customerList = new ArrayList<>();
    boolean resetCustomerPassword() {
        System.out.println("请输入顾客的用户名。");
        String Username = mid.nextLine();
        Customer targetCustomer = null;
        for (Customer cust : customerList) {
            if (cust.getUsername().equals(Username)) {
                targetCustomer = cust;
                break;
            }
        }
        if (targetCustomer == null) {
            System.out.println("未查找到该用户，请仔细核对用户名 。");
            return false;
        }
        String Answer;
        while (true) {
            System.out.println("是否需要重置该账户的密码？y/n");
            Answer = mid.next();
            if ("y".equals(Answer) || "n".equals(Answer)) {
                break;
            } else {
                System.out.println("输入无效，请输入 y 或者 n");
            }
        }
        if ("n".equals(Answer)) {
            return false;
        }
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String higherCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String symbol = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        String all = lowerCase + higherCase + digits + symbol;
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
        for (int i = 4; i < Length; i++) {
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
        targetCustomer.setPassword(resetPassword);
        System.out.println("已经为该用户重置密码，密码为" + resetPassword + "。");
        return true;
    }
}

