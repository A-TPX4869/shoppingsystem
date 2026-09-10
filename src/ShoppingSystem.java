import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ShoppingSystem {
    Customer customer=new Customer();
    Admin admin=new Admin();
    Goods g1=new Goods();
    double price = g1.getPurchasePrice();
    Scanner mid=new Scanner(System.in);
    public static Customer[] customerArray;
    public void ShoppingMenu(){
        System.out.println("1.挑选商品。");
        System.out.println("2.删除商品。");
        System.out.println("3.修改商品。");
        System.out.println("4.登出该用户。");
    }
    public void StartMenu(){
        System.out.println("1.用户登录。");
        System.out.println("2.管理员登录。");
        System.out.println("3.用户注册。");
        System.out.println("4.管理员注册。");
        int choisen=mid.nextInt();
        switch (choisen) {
            case 1:
                System.out.print("请输入用户名：");
                String name = mid.next();
                System.out.print("请输入密码：");
                String pwd = mid.next();
                currentCustomer = UserManager.loginCustomer(name, pwd);
                if (currentCustomer != null) {
                    System.out.println("登录成功！欢迎 " + currentCustomer.getUsername());
                    ShoppingMenu();
                } else {
                    System.out.println("用户名或密码错误！");
                }
                break;

            case (2):
                System.out.println("此处实现管理员登录。");
                admin.login();
                System.out.println("输入 0 返回");
                returnMain();
                break;
            case (3):
                System.out.println("此处实现用户注册。");
                customer.register();
                System.out.println("输入 0 返回");
                returnMain();
                break;
            case (4):
                System.out.println("此处实现管理员注册。");
                admin.register();
                System.out.println("输入 0 返回");
                returnMain();
                break;
            default:
                System.out.println("输入选项无效，请重新选择！");
        }
    }
   public void addGoodsTowardsCart(){
       System.out.println("请输入想添加的商品。");
       String goodName=mid.next();
       System.out.println("请输入想添加的数目。");
       int num=mid.nextInt();
       System.out.println("已添加"+num+"份商品"+goodName+"到您的购物车中。");
   }
    public void deleteGoodsTowardsCart(){
        System.out.println("请输入想删除的商品。");
        String goodName=mid.next();
        System.out.println("请输入想删除的数目。");
        int num=mid.nextInt();
        System.out.println("请确认此删除操作，删除后将无法恢复y/n。");
        String answer=mid.next();
        if(answer.equals("y")){
            System.out.println("已删除"+num+"份商品"+goodName+"到您的购物车中。");
        }
        else if (answer.equals("n")) {
            System.out.println("已取消该操作。");
        }
        else{
            System.out.println("输入不符合规范,请重新进行删除操作。");
            deleteGoodsTowardsCart();
        }
    }
    boolean checkGoods(String goods,int num){
        System.out.println("请确认商品为"+goods+"件数为"+num+"y/n");
        while(true) {
            String answer = mid.next();
            if (answer.equals("y")) {
                System.out.println("请选择支付方式微信/支付宝");
                String payment = mid.next();
                if (payment.equals("微信")) {
                    System.out.println("已为你跳转到微信支付界面，待支付金额为" + num * price + "元，请等待系统支付。");
                    return true;
                }
                if (payment.equals("支付宝")) {
                    System.out.println("已为你跳转到支付宝支付界面，待支付金额为" + num * price + "元，请等待系统支付。");
                    return true;
                }
            } else if (answer.equals("n")) {
                System.out.println("已取消该操作。");
                return false;
            } else {
                System.out.println("输入不符合规范,请重新进行删除操作。");
            }
        }

    }
    void returnMain() {
        int choiceNum = mid.nextInt();
        if (choiceNum == 0) {
            StartMenu();
        } else {
            System.out.print("请只输入数字 0，请重新输入：");
            returnMain();
        }
    }
}