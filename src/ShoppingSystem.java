import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class ShoppingSystem {
    private Scanner scanner = new Scanner(System.in);
    private Customer currentCustomer;
    private Admin currentAdmin;
    private ArrayList<CartItem> cart = new ArrayList<>();
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
    //手机号校验：11位数字，1开头，第二位3-9
    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^1[3-9]\\d{9}$");
    }
    //邮箱格式校验
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    //商品编号校验：G开头+3位数字
    private boolean isValidProductId(String id) {
        return id != null && id.matches("^G\\d{3}$");
    }
    //生产日期格式校验：yyyy-MM-dd
    private boolean isValidDate(String date) {
        if (date == null || !date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return false;
        }
        try {
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(5, 7));
            int day = Integer.parseInt(date.substring(8, 10));
            return year >= 2000 && year <= 2100 && month >= 1 && month <= 12 && day >= 1 && day <= 31;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void customerRegister() {
        System.out.println();
        System.out.println("===== 顾客注册 =====");
        System.out.print("请输入用户名：");
        String username = scanner.nextLine();
        if (username.length() < 5) {
            System.out.println("用户名长度不少于5个字符！");
            return;
        }
        System.out.print("请输入密码：");
        String password = scanner.nextLine();
        if (!User.isValidPassword(password)) {
            System.out.println("密码长度需大于8位，且必须同时包含大写字母、小写字母、数字和标点符号！");
            return;
        }
        String phone;
        while (true) {
            System.out.print("请输入电话号码：");
            phone = scanner.nextLine();
            if (isValidPhone(phone)) {//按国家标准校验手机号
                break;
            }
            System.out.println("电话号码格式不正确（应为11位手机号，1开头），请重新输入！");
        }
        String email;
        while (true) {
            System.out.print("请输入邮箱：");
            email = scanner.nextLine();
            if (isValidEmail(email)) {//按标准邮箱格式校验
                break;
            }
            System.out.println("邮箱格式不正确，请重新输入！");
        }
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
        if (username.length() < 5) {//管理员用户名长度同样不少于5个字符
            System.out.println("用户名长度不少于5个字符！");
            return;
        }
        System.out.print("请输入管理员密码：");
        String password = scanner.nextLine();
        if (!User.isValidPassword(password)) {//管理员密码强度与顾客一致
            System.out.println("密码长度需大于8位，且必须同时包含大写字母、小写字母、数字和标点符号！");
            return;
        }
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
        for (int attempt = 0; attempt < 3; attempt++) {
            System.out.print("密码：");
            String password = scanner.nextLine();
            Customer customer = UserManager.loginCustomer(username, password);
            if (customer != null) {
                if (customer.isLocked()) {
                    System.out.println("账户已锁定，请联系管理员重置密码！");
                    return;
                }
                customer.setFailedAttempt(0);
                UserManager.saveCustomers();
                cart = new ArrayList<>();
                System.out.println();
                System.out.println("登录成功！");
                System.out.println("欢迎你：" + customer.getUsername());
                currentCustomer = customer;
                customerMenu();
                return;
            }
            Customer target = UserManager.findCustomerByUsername(username);
            if (target != null) {
                target.setFailedAttempt(target.getFailedAttempt() + 1);
                if (target.getFailedAttempt() >= 3) {
                    target.setLocked(true);
                }
                UserManager.saveCustomers();
            }
            int remain = 3 - attempt - 1;
            if (remain > 0) {
                System.out.println("用户名或密码错误！还可尝试 " + remain + " 次。");
            }
            else {
                System.out.println("连续错误3次，账户已锁定！");
            }
        }
    }
    private void adminLogin() {
        System.out.println();
        System.out.println("===== 管理员登录 =====");
        System.out.print("管理员用户名：");
        String username = scanner.nextLine();
        for (int attempt = 0; attempt < 3; attempt++) {
            System.out.print("管理员密码：");
            String password = scanner.nextLine();
            currentAdmin = UserManager.loginAdmin(username, password);
            if (currentAdmin != null) {
                System.out.println("管理员登录成功！");
                System.out.println("欢迎管理员：" + currentAdmin.getUsername());
                adminMenu();
                return;
            }
            int remain = 2 - attempt;
            if (remain > 0) {
                System.out.println("管理员密码错误！还可尝试 " + remain + " 次，请重新输入密码：");
            }
        }
        System.out.println("连续错误3次，登录失败！");
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
                    cart = new ArrayList<>();
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
        if (!currentCustomer.getPassword().equals(oldPassword)) {
            System.out.println("旧密码错误！");
            return;
        }
        System.out.print("请输入新密码：");
        String newPassword = scanner.nextLine();
        if (!User.isValidPassword(newPassword)) {
            System.out.println("新密码长度需大于8位，且必须同时包含大写字母、小写字母、数字和标点符号！");
            return;
        }
        currentCustomer.setPassword(newPassword);
        UserManager.saveCustomers();
        System.out.println("密码修改成功！");
    }
    private void customerForgetPassword() {
        System.out.println();
        System.out.println("===== 忘记密码 =====");
        System.out.print("请输入用户名：");
        String username = scanner.nextLine();
        System.out.print("请输入注册时使用的邮箱：");
        String email = scanner.nextLine();
        Customer c = UserManager.findCustomerByUsername(username);
        if (c == null || !c.getEmail().equals(email)) {
            System.out.println("用户名与邮箱不匹配，无法重置密码！");
            return;
        }
        String newPassword = c.createRandomPassword();
        c.setPassword(newPassword);
        c.setFailedAttempt(0);
        c.setLocked(false);
        UserManager.saveCustomers();
        System.out.println("新密码已生成（模拟发送至邮箱）：" + newPassword);
        System.out.println("请使用新密码登录。");
    }
    private void shoppingMenu() {
        while (true) {
            System.out.println();
            System.out.println("===== 商品菜单 =====");
            System.out.println("1. 浏览商品并加入购物车");
            System.out.println("2. 从购物车移除商品");
            System.out.println("3. 修改购物车商品数量");
            System.out.println("4. 结算");
            System.out.println("5. 查看购物历史");
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
                    modifyCartQuantity();
                    break;
                case "4":
                    checkGoods();
                    break;
                case "5":
                    viewOrderHistory();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("输入错误！");
            }
        }
    }
    private void addGoodsTowardsCart() {
        System.out.println();
        System.out.println("===== 加入购物车 =====");
        ArrayList<Goods> goodsList = UserManager.getGoodsList();
        if (goodsList.isEmpty()) {
            System.out.println("暂无商品可购买！");
            return;
        }
        for (Goods g : goodsList) {
            printGoods(g);
        }
        System.out.print("请输入商品编号：");
        String productId = scanner.nextLine();
        Goods target = UserManager.findGoodsByProductId(productId);
        if (target == null) {
            System.out.println("商品不存在！");
            return;
        }
        int num = readInt("请输入购买数量：");
        if (num <= 0) {
            System.out.println("数量必须大于0！");
            return;
        }
        for (CartItem item : cart) {
            if (item.getGoods().getProductId().equals(productId)) {
                if (item.getCount() + num > target.getNum()) {
                    System.out.println("库存不足！当前库存：" + target.getNum());
                    return;
                }
                item.setCount(item.getCount() + num);
                System.out.println("购物车中已有该商品，当前数量：" + item.getCount());
                return;
            }
        }
        if (num > target.getNum()) {
            System.out.println("库存不足！当前库存：" + target.getNum());
            return;
        }
        cart.add(new CartItem(target, num));
        System.out.println("已加入购物车！");
    }
    private void deleteGoodsTowardsCart() {
        if (cart.isEmpty()) {
            System.out.println("购物车为空！");
            return;
        }
        System.out.print("请输入要移除的商品编号：");
        String productId = scanner.nextLine();
        CartItem target = null;
        for (CartItem item : cart) {
            if (item.getGoods().getProductId().equals(productId)) {
                target = item;
                break;
            }
        }
        if (target == null) {
            System.out.println("购物车中没有该商品！");
            return;
        }
        System.out.println("警告：请确认是否将该商品从购物车移除？(y/n)");
        String answer = scanner.nextLine();
        if (answer.equals("y")) {
            cart.remove(target);
            System.out.println("已从购物车移除（商品库存不变）。");
        }
        else {
            System.out.println("已取消操作。");
        }
    }
    private void modifyCartQuantity() {
        if (cart.isEmpty()) {
            System.out.println("购物车为空！");
            return;
        }
        System.out.print("请输入要修改的商品编号：");
        String productId = scanner.nextLine();
        CartItem target = null;
        for (CartItem item : cart) {
            if (item.getGoods().getProductId().equals(productId)) {
                target = item;
                break;
            }
        }
        if (target == null) {
            System.out.println("购物车中没有该商品！");
            return;
        }
        int num = readInt("请输入新的数量（不大于0则从购物车清除）：");
        if (num <= 0) {
            cart.remove(target);
            System.out.println("数量不大于0，该商品已从购物车清除。");
        }
        else {
            if (num > target.getGoods().getNum()) {
                System.out.println("库存不足！当前库存：" + target.getGoods().getNum());
                return;
            }
            target.setCount(num);
            System.out.println("数量已修改为：" + num);
        }
    }
    private void checkGoods() {
        if (cart.isEmpty()) {
            System.out.println("购物车为空，无法结账！");
            return;
        }
        double total = 0;
        System.out.println("===== 购物车清单 =====");
        for (CartItem item : cart) {
            double subTotal = item.getGoods().getRetailPrice() * item.getCount();
            total += subTotal;
            System.out.printf("商品：%s，单价：%.2f，数量：%d，小计：%.2f%n",
                    item.getGoods().getProductName(),
                    item.getGoods().getRetailPrice(),
                    item.getCount(),
                    subTotal);
        }
        System.out.printf("订单总金额：%.2f%n", total);
        System.out.println("请选择支付渠道：");
        System.out.println("1.支付宝  2.微信  3.银行卡");
        int payType = readInt("请选择：");
        String payName;
        switch (payType) {
            case 1:
                payName = "支付宝";
                break;
            case 2:
                payName = "微信";
                break;
            case 3:
                payName = "银行卡";
                break;
            default:
                System.out.println("支付渠道无效，取消结账！");
                return;
        }
        System.out.printf("正在使用【%s】发起支付，金额%.2f...%n", payName, total);
        System.out.println("支付成功！");
        for (CartItem item : cart) {
            Goods g = item.getGoods();
            g.setNum(g.getNum() - item.getCount());
        }
        currentCustomer.setTotalConsume(currentCustomer.getTotalConsume() + total);
        updateLevel(currentCustomer);
        StringBuilder detail = new StringBuilder();
        for (CartItem item : cart) {
            if (detail.length() > 0) {
                detail.append("; ");
            }
            detail.append(item.getGoods().getProductName()).append(" x").append(item.getCount());
        }
        Order order = new Order();
        order.setOrderId("O" + (UserManager.getOrderCount() + 1));
        order.setUsername(currentCustomer.getUsername());
        order.setTime(LocalDateTime.now().toString());
        order.setDetail(detail.toString());
        order.setTotalAmount(total);
        UserManager.addOrder(order);
        System.out.println("已更新商品库存！");
        cart.clear();
        System.out.println("购物车已清空，订单完成！");
    }
    private void viewOrderHistory() {
        System.out.println();
        System.out.println("===== 我的购物历史 =====");
        ArrayList<Order> list = UserManager.getOrdersByUsername(currentCustomer.getUsername());
        if (list.isEmpty()) {
            System.out.println("暂无订单记录。");
            return;
        }
        for (Order o : list) {
            System.out.println("订单号：" + o.getOrderId());
            System.out.println("时间：" + o.getTime());
            System.out.println("商品：" + o.getDetail());
            System.out.printf("金额：%.2f%n", o.getTotalAmount());
            System.out.println("------------------");
        }
    }
    private void updateLevel(Customer c) {
        double t = c.getTotalConsume();
        if (t >= 1000) {
            c.setLevel("金牌顾客");
        }
        else if (t >= 500) {
            c.setLevel("银牌顾客");
        }
        else {
            c.setLevel("铜牌顾客");
        }
    }
    private void adminMenu() {
        while (true) {
            System.out.println();
            System.out.println("===== 管理员菜单 =====");
            System.out.println("1. 查看所有顾客");
            System.out.println("2. 查看所有管理员");
            System.out.println("3. 重置顾客密码");
            System.out.println("4. 修改自己的密码");
            System.out.println("5. 商品管理");
            System.out.println("6. 删除顾客");
            System.out.println("7. 查询顾客");
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
                case "5":
                    goodsMenu();
                    break;
                case "6":
                    deleteCustomer();
                    break;
                case "7":
                    queryCustomer();
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
        ArrayList<Customer> list = UserManager.getCustomerList();
        System.out.println();
        System.out.println("===== 所有顾客 =====");
        if (list.isEmpty()) {
            System.out.println("当前没有顾客。");
            return;
        }
        for (Customer customer : list) {
            printCustomer(customer);
        }
    }
    private void showAllAdmins() {
        ArrayList<Admin> list = UserManager.getAdminList();
        System.out.println();
        System.out.println("===== 所有管理员 =====");
        for (Admin admin : list) {
            System.out.println("管理员用户名：" + admin.getUsername());
        }
    }
    private void resetCustomerPassword() {
        System.out.print("请输入顾客用户名：");
        String username = scanner.nextLine();
        Customer targetCustomer = UserManager.findCustomerByUsername(username);
        if (targetCustomer == null) {
            System.out.println("没有找到该顾客！");
            return;
        }
        String newPassword = targetCustomer.createRandomPassword();
        targetCustomer.setPassword(newPassword);
        targetCustomer.setFailedAttempt(0);
        targetCustomer.setLocked(false);
        UserManager.saveCustomers();
        System.out.println("密码已经重置！");
        System.out.println("新密码：" + newPassword);
    }
    private void adminChangePassword() {
        System.out.print("请输入旧密码：");
        String oldPassword = scanner.nextLine();
        if (!currentAdmin.getPassword().equals(oldPassword)) {
            System.out.println("旧密码错误！");
            return;
        }
        System.out.print("请输入新密码：");
        String newPassword = scanner.nextLine();
        if (!User.isValidPassword(newPassword)) {//管理员新密码同样强度要求
            System.out.println("新密码长度需大于8位，且必须同时包含大写字母、小写字母、数字和标点符号！");
            return;
        }
        currentAdmin.setPassword(newPassword);
        UserManager.saveAdmins();
        System.out.println("管理员密码修改成功！");
    }
    private void deleteCustomer() {
        System.out.print("请输入要删除的顾客用户名：");
        String username = scanner.nextLine();
        Customer target = UserManager.findCustomerByUsername(username);
        if (target == null) {
            System.out.println("没有找到该顾客！");
            return;
        }
        System.out.println("警告：删除顾客后无法恢复，请确认是否继续删除操作？(y/n)");
        String answer = scanner.nextLine();
        if (answer.equals("y")) {
            UserManager.deleteCustomer(target);
            System.out.println("顾客已删除。");
        }
        else {
            System.out.println("已取消删除操作。");
        }
    }
    private void queryCustomer() {
        System.out.print("请输入顾客编号或用户名：");
        String key = scanner.nextLine();
        Customer c = UserManager.findCustomerByCustomerId(key);
        if (c == null) {
            c = UserManager.findCustomerByUsername(key);
        }
        if (c == null) {
            System.out.println("没有找到该顾客！");
            return;
        }
        printCustomer(c);
    }
    private void printCustomer(Customer c) {
        System.out.println("编号：" + c.getCustomerId());
        System.out.println("用户名：" + c.getUsername());
        System.out.println("等级：" + c.getLevel());
        System.out.println("注册时间：" + c.getRegisterTime());
        System.out.println("总消费：" + c.getTotalConsume());
        System.out.println("电话：" + c.getPhone());
        System.out.println("邮箱：" + c.getEmail());
        System.out.println("------------------");
    }
    private void goodsMenu() {
        while (true) {
            System.out.println();
            System.out.println("===== 商品管理 =====");
            System.out.println("1. 列出所有商品");
            System.out.println("2. 添加商品");
            System.out.println("3. 修改商品");
            System.out.println("4. 删除商品");
            System.out.println("5. 查询商品");
            System.out.println("0. 返回管理员菜单");
            System.out.print("请选择：");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    showAllGoods();
                    break;
                case "2":
                    addGoods();
                    break;
                case "3":
                    updateGoods();
                    break;
                case "4":
                    deleteGoods();
                    break;
                case "5":
                    searchGoods();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("输入错误！");
            }
        }
    }
    private void showAllGoods() {
        System.out.println();
        System.out.println("===== 所有商品 =====");
        ArrayList<Goods> list = UserManager.getGoodsList();
        if (list.isEmpty()) {
            System.out.println("当前没有商品。");
            return;
        }
        for (Goods g : list) {
            printGoods(g);
        }
    }
    private void addGoods() {
        System.out.println();
        System.out.println("===== 添加商品 =====");
        String productId;
        while (true) {
            System.out.print("请输入商品编号（G开头+3位数字，如G001）：");
            productId = scanner.nextLine();
            if (!isValidProductId(productId)) {
                System.out.println("商品编号格式不正确（应为G开头加3位数字，如G001），请重新输入！");
                continue;
            }
            if (UserManager.findGoodsByProductId(productId) != null) {
                System.out.println("该商品编号已存在，请重新输入！");
                continue;
            }
            break;
        }
        System.out.print("请输入商品名称：");
        String productName = scanner.nextLine();
        System.out.print("请输入生产厂家：");
        String manufacturer = scanner.nextLine();
        String produceDate;
        while (true) {
            System.out.print("请输入生产日期（格式yyyy-MM-dd，如2026-01-10）：");
            produceDate = scanner.nextLine();
            if (isValidDate(produceDate)) {
                break;
            }
            System.out.println("生产日期格式不正确，请重新输入！");
        }
        System.out.print("请输入型号：");
        String model = scanner.nextLine();
        double purchasePrice = readDouble("请输入进货价：");
        while (purchasePrice < 0) {
            System.out.println("进货价不能为负数！");
            purchasePrice = readDouble("请输入进货价：");
        }
        double retailPrice = readDouble("请输入零售价格：");
        while (retailPrice < 0) {
            System.out.println("零售价不能为负数！");
            retailPrice = readDouble("请输入零售价格：");
        }
        int num = readInt("请输入数量：");
        while (num < 0) {
            System.out.println("数量不能为负数！");
            num = readInt("请输入数量：");
        }
        Goods g = new Goods();
        g.setProductId(productId);
        g.setProductName(productName);
        g.setManufacturer(manufacturer);
        g.setProduceDate(produceDate);
        g.setModel(model);
        g.setPurchasePrice(purchasePrice);
        g.setRetailPrice(retailPrice);
        g.setNum(num);
        UserManager.addGoods(g);
        System.out.println("商品添加成功！");
    }
    private void updateGoods() {
        System.out.print("请输入要修改的商品编号：");
        String productId = scanner.nextLine();
        Goods target = UserManager.findGoodsByProductId(productId);
        if (target == null) {
            System.out.println("没有找到该商品！");
            return;
        }
        System.out.print("请输入新的商品名称：");
        target.setProductName(scanner.nextLine());
        System.out.print("请输入新的生产厂家：");
        target.setManufacturer(scanner.nextLine());
        String newDate;
        while (true) {
            System.out.print("请输入新的生产日期（格式yyyy-MM-dd，如2026-01-10）：");
            newDate = scanner.nextLine();
            if (isValidDate(newDate)) {
                break;
            }
            System.out.println("生产日期格式不正确，请重新输入！");
        }
        target.setProduceDate(newDate);
        System.out.print("请输入新的型号：");
        target.setModel(scanner.nextLine());
        double purchasePrice = readDouble("请输入新的进货价：");
        while (purchasePrice < 0) {
            System.out.println("进货价不能为负数！");
            purchasePrice = readDouble("请输入新的进货价：");
        }
        target.setPurchasePrice(purchasePrice);
        double retailPrice = readDouble("请输入新的零售价格：");
        while (retailPrice < 0) {
            System.out.println("零售价不能为负数！");
            retailPrice = readDouble("请输入新的零售价格：");
        }
        target.setRetailPrice(retailPrice);
        int num = readInt("请输入新的数量：");
        while (num < 0) {
            System.out.println("数量不能为负数！");
            num = readInt("请输入新的数量：");
        }
        target.setNum(num);
        UserManager.updateGoods(target);
        System.out.println("商品信息修改成功！");
    }
    private void deleteGoods() {
        System.out.print("请输入要删除的商品编号：");
        String productId = scanner.nextLine();
        Goods target = UserManager.findGoodsByProductId(productId);
        if (target == null) {
            System.out.println("没有找到该商品！");
            return;
        }
        System.out.println("警告：删除后无法恢复，请确认是否继续删除操作？(y/n)");
        String answer = scanner.nextLine();
        if (answer.equals("y")) {
            UserManager.deleteGoods(target);
            System.out.println("商品删除成功！");
        }
        else {
            System.out.println("已取消删除操作。");
        }
    }
    private void searchGoods() {
        System.out.println();
        System.out.println("===== 查询商品 =====");
        System.out.print("请输入商品名称（不限制直接回车）：");
        String name = scanner.nextLine();
        System.out.print("请输入生产厂家（不限制直接回车）：");
        String manufacturer = scanner.nextLine();
        double minPrice = readDouble("请输入零售价格下限（不限制输入0）：");
        ArrayList<Goods> result = UserManager.searchGoods(name, manufacturer, minPrice);
        System.out.println("===== 查询结果 =====");
        if (result.isEmpty()) {
            System.out.println("没有符合条件的商品！");
            return;
        }
        for (Goods g : result) {
            printGoods(g);
        }
    }
    private void printGoods(Goods g) {
        System.out.println("商品编号：" + g.getProductId());
        System.out.println("商品名称：" + g.getProductName());
        System.out.println("生产厂家：" + g.getManufacturer());
        System.out.println("生产日期：" + g.getProduceDate());
        System.out.println("型号：" + g.getModel());
        System.out.println("进货价：" + g.getPurchasePrice());
        System.out.println("零售价格：" + g.getRetailPrice());
        System.out.println("数量：" + g.getNum());
        System.out.println("------------------");
    }
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            try {
                return Integer.parseInt(s.trim());
            }
            catch (NumberFormatException e) {
                System.out.println("请输入整数！");
            }
        }
    }
    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            try {
                return Double.parseDouble(s.trim());
            }
            catch (NumberFormatException e) {
                System.out.println("请输入数字！");
            }
        }
    }
}
