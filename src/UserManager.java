import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
public class UserManager {
    private static ArrayList<Customer> customerList = new ArrayList<>();
    private static ArrayList<Admin> adminList = new ArrayList<>();
    private static final String CUSTOMER_FILE = "customers.txt";
    private static final String ADMIN_FILE = "admins.txt";
    private static final String SEP = "~";
    public static void loadData() {
        loadCustomers();
        loadAdmins();
        if (adminList.isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            adminList.add(admin);
            saveAdmins();
            System.out.println("系统已创建默认管理员：admin / admin123");}}
    public static boolean registerCustomer(
            String username,
            String password,
            String phone,
            String email)
    {
        if (usernameExists(username)) {
            return false;
        }
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setCustomerId(
                "C" + (customerList.size() + 1)
        );
        customer.setLevel("普通会员");
        customer.setRegisterTime(
                LocalDate.now().toString());
        customer.setTotalConsume(0);
        customer.setPhone(phone);
        customer.setEmail(email);
        customerList.add(customer);
        saveCustomers();
        return true;
    }
    public static boolean registerAdmin(
            String username,
            String password)
    {
        if (usernameExists(username)) {
            return false;
        }
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        adminList.add(admin);
        saveAdmins();
        return true;
    }
    public static Customer loginCustomer(
            String username,
            String password)
    {
        for (Customer customer : customerList) {
            if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }
    public static Admin loginAdmin(
            String username,
            String password)
    {
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password))
            {
                return admin;
            }
        }
        return null;
    }
    public static boolean usernameExists(
            String username)
    {
        for (Customer customer : customerList) {
            if (
                    customer.getUsername().equals(username)) {
                return true;
            }
        }
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username))
            {
                return true;
            }
        }
        return false;
    }
    public static ArrayList<Customer> getCustomerList() {
        return customerList;
    }
    public static ArrayList<Admin> getAdminList() {
        return adminList;
    }
    public static void saveAll() {
        saveCustomers();
        saveAdmins();
    }
    private static void loadCustomers() {
        File file = new File(CUSTOMER_FILE);
        if (!file.exists()) {
            return;
        }
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String line;
            while (
                    (line = reader.readLine()) != null)
            {
                line = line.trim();
                if (line.equals("")) {
                    continue;
                }
                String[] parts = line.split(SEP);
                if (parts.length >= 8) {Customer customer = new Customer();
                    customer.setUsername(parts[0]);
                    customer.setPassword(parts[1]);
                    customer.setCustomerId(parts[2]);
                    customer.setLevel(parts[3]);
                    customer.setRegisterTime(parts[4]);
                    customer.setTotalConsume(Double.parseDouble(parts[5]));
                    customer.setPhone(parts[6]);
                    customer.setEmail(parts[7]);
                    customerList.add(customer);
                }
            }
        } catch (IOException e) {
            System.out.println("读取顾客文件失败：" + e.getMessage());
        }
    }
    public static void saveCustomers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
            for (Customer customer : customerList)
            {
                writer.write(
                        customer.getUsername()
                                + SEP
                                + customer.getPassword()
                                + SEP
                                + customer.getCustomerId()
                                + SEP
                                + customer.getLevel()
                                + SEP
                                + customer.getRegisterTime()
                                + SEP
                                + customer.getTotalConsume()
                                + SEP
                                + customer.getPhone()
                                + SEP
                                + customer.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("保存顾客文件失败："+ e.getMessage());
        }
    }
    private static void loadAdmins() {
        File file = new File(ADMIN_FILE);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String line;
            while (
                    (line = reader.readLine())!= null)
            {
                line = line.trim();
                if (line.equals("")) {
                    continue;
                }
                String[] parts =
                        line.split(SEP);
                if (parts.length >= 2) {
                    Admin admin = new Admin();
                    admin.setUsername(parts[0]);
                    admin.setPassword(parts[1]);
                    adminList.add(admin);
                }
            }
        } catch (IOException e) {
            System.out.println("读取管理员文件失败：" + e.getMessage());
        }
    }
    public static void saveAdmins() {
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(ADMIN_FILE)))
        {
            for (Admin admin
                    : adminList) {
                writer.write(admin.getUsername() + SEP + admin.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("保存管理员文件失败：" + e.getMessage());
        }
    }
}
