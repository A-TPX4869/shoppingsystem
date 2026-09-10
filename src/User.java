import java.util.Scanner;

public abstract class User {
    private String username;
    private String password;
    public User(){
        this.password="ynuinfo#777";
        this.username="yonghu";
    }
    Scanner mid = new Scanner(System.in);
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public void setPassword(String password){
        this.password=password;
    }

    void changePassword() {
        System.out.println("请输入初始密码。");
        String Password = mid.nextLine();
        if (!Password.equals(this.password)) {
            System.out.println("初始密码错误！");
        }
        else{
            System.out.println("初始密码正确！");
        }
        while(true){
            System.out.println("请输入修改之后的密码。");
            String NewPassword = mid.nextLine();
            boolean hasDigit = false;
            boolean hasLowerLetter = false;
            boolean hasUpperLetter = false;
            boolean hasSymbol = false;
            boolean invalidChar = false;
            for (int i = 0; i < NewPassword.length(); i++) {
                char ch = NewPassword.charAt(i);
                if (ch == 32) {
                    invalidChar = true;
                }
                if (48 <= ch && ch <= 57) {
                    hasDigit = true;
                } else if ((65 <= ch && ch <= 90)) {
                    hasUpperLetter = true;
                }
            else if ( (97 <= ch && ch <= 122)) {
                    hasLowerLetter = true;
            }
                else {
                    hasSymbol = true;
                }
            }
                if (invalidChar) {
                    System.out.println("密码不能包含空格！请重新输入。");
                } else if (hasDigit && hasUpperLetter && hasSymbol&&hasLowerLetter) {
                    this.password=NewPassword;
                    System.out.println("密码修改成功！");
                    break;
                } else {
                    System.out.println("密码设置不规范，必须包含数字、字母、符号，请重新输入。");
                }
        }

    }
    void logout() {
        while (true) {
            System.out.println("是否确认登出?请输入y/n。");
            String answer = mid.next();
            if (answer.equals("y")) {
                System.out.println("已经退出登录。");
                login();
                break;
            } else if (answer.equals("n")) {
                System.out.println("返回当前状态。");
                break;
            } else {
                System.out.println("输入不符合规范");
            }
        }
    }
    void login() {
        while (true) {
            System.out.println("是否已经注册?请输入y/n。");
            String answer = mid.nextLine();
            if (answer.equals("y")) {
                System.out.println("请输入用户名");
                String inputUsername = mid.next();
                System.out.println("请输入密码");
                String inputPassword = mid.next();
                if (inputUsername.equals(this.username)
                        && inputPassword.equals(this.password)) {
                    System.out.println("登录成功！");
                } else {
                    System.out.println("用户名或密码错误！");
                    break;
                }
            } else if (answer.equals("n")) {
                register();
                break;
            } else {
                System.out.println("输入不符合规范,请确认您是否注册。/n");
            }
        }
    }
        void register(){
            System.out.println("请输入你的用户名。");
            this.username = mid.next();
            this.password="ynuinfo#777";
            System.out.println("你的用户名是"+this.username+"初始密码为 "+this.password+"，请登陆后修改密码,已经自动为你转入登陆界面。");
            login();
    }
}
