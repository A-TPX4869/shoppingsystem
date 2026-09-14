import java.util.ArrayList;
import java.util.Scanner;

public class ShoppingSystem {
    private Scanner scanner = new Scanner(System.in);
    private Customer currentCustomer;
    private Admin currentAdmin;
    public void startMenu() {
        while (true) {
            System.out.println();
            System.out.println("==============================");
            System.out.println("        欢迎进入购物系统");
            System.out.println("==============================");
            System.out.println("1. 顾客登录");
            System.out.println("2. 管理员登录");
            System.out.println("3. 顾客注册");
            System.out.println("4. 管理员注册");
            System.out.println("0. 退出系统");
            System.out.print("请输入你的选择：");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    customerLogin();
                    break;
                case "2":
                    adminLogin();
                    break;
                case "3":
                    customerRegister();
                    break;
                case "4":
                    adminRegister();
                    break;
                case "0":
                    System.out.println("感谢使用购物系统！");
                    UserManager.saveAll();
                    return;
                default:
                    System.out.println("输入错误，请重新选择！");
            }
        }
    }
    private void customerRegister() {
        System.out.println();
        System.out.println("===== 顾客注册 =====");
        System.out.print("请输入用户名：");
        String username = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();
        System.out.print("请输入电话号码：");
        String phone = scanner.nextLine();
        System.out.print("请输入邮箱：");
        String email = scanner.nextLine();
        boolean result = UserManager.registerCustomer(username, password, phone, email);


        if (result) {
            System.out.println("注册成功！");
        }
        else {
            System.out.println("用户名已经存在！");
        }
    }
    private void adminRegister() {
        System.out.println();
        System.out.println("===== 管理员注册 =====");
        System.out.print("请输入管理员用户名：");
        String username = scanner.nextLine();
        System.out.print("请输入管理员密码：");
        String password = scanner.nextLine();
        boolean result = UserManager.registerAdmin(username, password);
        if (result) {
            System.out.println("管理员注册成功！");
        }
        else {
            System.out.println("用户名已经存在！");
        }
    }
    private void customerLogin() {
        System.out.println();
        System.out.println("===== 顾客登录 =====");
        System.out.print("用户名：");
        String username = scanner.nextLine();
        System.out.print("密码：");
        String password = scanner.nextLine();
        currentCustomer = UserManager.loginCustomer(username, password);
        if (currentCustomer != null)
        {
            System.out.println();
            System.out.println("登录成功！");
            System.out.println("欢迎你：" + currentCustomer.getUsername());
            customerMenu();
        }
        else {
            System.out.println("用户名或密码错误！");
        }
    }
    private void adminLogin() {
        System.out.println();
        System.out.println("===== 管理员登录 =====");
        System.out.print("管理员用户名：");
        String username = scanner.nextLine();
        System.out.print("管理员密码：");
        String password = scanner.nextLine();
        currentAdmin =
                UserManager.loginAdmin(username, password);
        if (currentAdmin != null)
        {
            System.out.println("管理员登录成功！");
            System.out.println("欢迎管理员：" + currentAdmin.getUsername());
            adminMenu();
        }
        else {
            System.out.println("管理员用户名或密码错误！");
        }
    }
    private void customerMenu() {
        while (true) {
            System.out.println();
            System.out.println("===== 顾客菜单 =====");
            System.out.println("1. 查看个人信息");
            System.out.println("2. 修改密码");
            System.out.println("3. 忘记密码");
            System.out.println("4. 商品菜单");
            System.out.println("0. 登出");
            System.out.print("请选择：");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    showCustomerInfo();
                    break;
                case "2":
                    customerChangePassword();
                    break;
                case "3":
                    customerForgetPassword();
                    break;
                case "4":
                    shoppingMenu();
                    break;
                case "0":
                    System.out.println("已退出当前顾客账号。");
                    currentCustomer = null;
                    return;
                default:
                    System.out.println("输入错误！");
            }
        }
    }
    private void showCustomerInfo() {
        System.out.println();
        System.out.println("===== 个人信息 =====");
        System.out.println("用户名：" + currentCustomer.getUsername());
        System.out.println("顾客编号：" + currentCustomer.getCustomerId());
        System.out.println("会员等级：" + currentCustomer.getLevel());
        System.out.println("注册时间：" + currentCustomer.getRegisterTime());
        System.out.println("总消费：" + currentCustomer.getTotalConsume());
        System.out.println("电话：" + currentCustomer.getPhone());
        System.out.println("邮箱：" + currentCustomer.getEmail());
    }
    private void customerChangePassword() {
        System.out.print("请输入旧密码：");
        String oldPassword = scanner.nextLine();
        if (
                !currentCustomer
                        .getPassword()
                        .equals(oldPassword)
        ) {
            System.out.println(
                    "旧密码错误！"
            );
            return;
        }
        System.out.print(
                "请输入新密码："
        );
        String newPassword =
                scanner.nextLine();
        currentCustomer.setPassword(
                newPassword
        );
        UserManager.saveCustomers();
        System.out.println("密码修改成功！");
    }
    private void customerForgetPassword() {
        System.out.println("系统正在生成新密码...");
        String newPassword = currentCustomer.createRandomPassword();
        currentCustomer.setPassword(newPassword);
        UserManager.saveCustomers();
        System.out.println("新密码为：" + newPassword);
        System.out.println("实际项目中应该通过邮箱发送。");
    }
    private void shoppingMenu() {
        while (true) {
            System.out.println();
            System.out.println("===== 商品菜单 =====");
            System.out.println("1. 添加商品到购物车");
            System.out.println("2. 删除购物车商品");
            System.out.println("3. 结算商品");
            System.out.println("0. 返回顾客菜单");
            System.out.print("请选择：");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addGoodsTowardsCart();
                    break;
                case "2":
                    deleteGoodsTowardsCart();
                    break;
                case "3":
                    checkGoods();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("输入错误！");
            }
        }
    }
    private void addGoodsTowardsCart() {
        System.out.print("请输入商品名称：");
        String goodName =
                scanner.nextLine();
        System.out.print(
                "请输入商品数量："
        );
        String num =
                scanner.nextLine();
        System.out.println(
                "已添加 " + num + " 个 " + goodName + " 到购物车。");
    }
    private void deleteGoodsTowardsCart() {
        System.out.print("请输入商品名称：");
        String goodName = scanner.nextLine();
        System.out.print("请输入删除数量：");
        String num = scanner.nextLine();
        System.out.print("确认删除？y/n：");
        String answer = scanner.nextLine();
        if (answer.equals("y"))
        {
            System.out.println("已删除 " + num + " 个 " + goodName);
        } else {
            System.out.println("已取消操作。");
        }
    }
    private void checkGoods() {
        System.out.println("购物车结算功能正在完善。");
        System.out.println("当前版本重点实现用户管理系统。");
    }
    private void adminMenu() {
        while (true) {
            System.out.println();
            System.out.println("===== 管理员菜单 =====");
            System.out.println("1. 查看所有顾客");
            System.out.println("2. 查看所有管理员");
            System.out.println("3. 重置顾客密码");
            System.out.println("4. 修改自己的密码");
            System.out.println("0. 登出");
            System.out.print("请选择：");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    showAllCustomers();
                    break;
                case "2":
                    showAllAdmins();
                    break;
                case "3":
                    resetCustomerPassword();
                    break;
                case "4":
                    adminChangePassword();
                    break;
                case "0":
                    currentAdmin = null;
                    System.out.println("管理员已登出。");
                    return;
                default:
                    System.out.println("输入错误！");
            }
        }
    }
    private void showAllCustomers() {
        ArrayList<Customer> list =
                UserManager.getCustomerList();
        System.out.println();
        System.out.println("===== 所有顾客 =====");
        if (list.isEmpty())
        {
            System.out.println("当前没有顾客。");
            return;
        }
        for (Customer customer : list)
        {
            System.out.println("编号：" + customer.getCustomerId());
            System.out.println("用户名：" + customer.getUsername());
            System.out.println("等级："+ customer.getLevel());
            System.out.println("电话：" + customer.getPhone());
            System.out.println("邮箱：" + customer.getEmail());
            System.out.println("------------------");
        }
    }
    private void showAllAdmins() {
        ArrayList<Admin> list = UserManager.getAdminList();
        System.out.println();
        System.out.println("===== 所有管理员 =====");
        for (Admin admin : list)
        {
            System.out.println("管理员用户名：" + admin.getUsername());
        }
    }
    private void resetCustomerPassword() {
        System.out.print("请输入顾客用户名：");String username = scanner.nextLine();
        ArrayList<Customer> list = UserManager.getCustomerList();
        Customer targetCustomer = null;
        for (Customer customer : list)
        {
            if (customer.getUsername().equals(username))
            {
                targetCustomer = customer;
                break;
            }
        }
        if (targetCustomer == null)
        {
            System.out.println("没有找到该顾客！");
            return;
        }
        String newPassword = targetCustomer.createRandomPassword();
        targetCustomer.setPassword(newPassword);
        UserManager.saveCustomers();
        System.out.println("密码已经重置！");
        System.out.println("新密码：" + newPassword);
    }
    private void adminChangePassword() {
        System.out.print("请输入旧密码：");
        String oldPassword = scanner.nextLine();
        if (!currentAdmin.getPassword().equals(oldPassword))
        {
            System.out.println("旧密码错误！");
            return;
        }
        System.out.print("请输入新密码：");
        String newPassword = scanner.nextLine();
        currentAdmin.setPassword(newPassword);
        UserManager.saveAdmins();
        System.out.println("管理员密码修改成功！");
    }
}
