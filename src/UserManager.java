import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserManager {
    private static ArrayList<Customer> customerList = new ArrayList<>();
    private static ArrayList<Admin> adminList = new ArrayList<>();
    private static final String CUSTOMER_FILE = "customers.txt";
    private static final String ADMIN_FILE = "admins.txt";
    private static final String Mid = "~";
    private static void loadCustomers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(Mid);
                if (parts.length >= 4) {
                    Customer customer = new Customer();
                    customer.setCustomerId(parts[0]);
                    customer.setEmail(parts[1]);
                    customer.setPhone(parts[2]);
                    customer.setTotalConsumet(Double.parseDouble(parts[3]));
                    customerList.add(customer);
                }
            }
        } catch (IOException e) {
        }
    }

    private static void loadAdmins() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(Mid);
                if (parts.length >= 2) {
                    Admin a = new Admin();
                    a.setUsername(parts[0]);
                    a.setPassword(parts[1]);
                    adminList.add(a);
                }
            }
        } catch (IOException e) {
        }
    }

    private static void saveAdmins() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMIN_FILE))) {
            for (Admin a : adminList) {
                writer.write(a.getUsername() + Mid + a.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData() {
        loadCustomers();
        loadAdmins();

        if (adminList.isEmpty()) {

            Admin defaultAdmin = new Admin();

            defaultAdmin.setUsername("admin");

            defaultAdmin.setPassword("admin123");

            adminList.add(defaultAdmin);

            saveAdmins();

            System.out.println("已创建默认管理员：admin / admin123");
        }
    }
}
