import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserManager {
    private static ArrayList<Customer> customerList = new ArrayList<>();
    private static ArrayList<Admin> adminList = new ArrayList<>();
    private static ArrayList<Goods> goodsList = new ArrayList<>();
    private static ArrayList<Order> orderList = new ArrayList<>();
    private static final String DB_URL = "jdbc:sqlite:shopping.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("加载SQLite驱动失败：" + e.getMessage());
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void loadData() {
        initTables();
        loadCustomers();
        loadAdmins();
        loadGoods();
        loadOrders();
        if (adminList.isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword("ynuinfo#777");
            adminList.add(admin);
            System.out.println("系统已创建默认管理员：admin / ynuinfo#777");
        }
        if (goodsList.isEmpty()) {
            seedGoods();
        }
        saveAll();
    }

    private static void initTables() {
        String customerSql = "CREATE TABLE IF NOT EXISTS customers ("
                + "username TEXT PRIMARY KEY, "
                + "password TEXT, "
                + "customerId TEXT, "
                + "level TEXT, "
                + "registerTime TEXT, "
                + "totalConsume REAL, "
                + "phone TEXT, "
                + "email TEXT, "
                + "failedAttempt INTEGER, "
                + "locked INTEGER)";
        String adminSql = "CREATE TABLE IF NOT EXISTS admins ("
                + "username TEXT PRIMARY KEY, "
                + "password TEXT)";
        String goodsSql = "CREATE TABLE IF NOT EXISTS goods ("
                + "productId TEXT PRIMARY KEY, "
                + "productName TEXT, "
                + "manufacturer TEXT, "
                + "produceDate TEXT, "
                + "model TEXT, "
                + "purchasePrice REAL, "
                + "retailPrice REAL, "
                + "num INTEGER)";
        String orderSql = "CREATE TABLE IF NOT EXISTS orders ("
                + "orderId TEXT PRIMARY KEY, "
                + "username TEXT, "
                + "time TEXT, "
                + "detail TEXT, "
                + "totalAmount REAL)";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute(customerSql);
            st.execute(adminSql);
            st.execute(goodsSql);
            st.execute(orderSql);
        } catch (SQLException e) {
            System.out.println("初始化数据库失败：" + e.getMessage());
        }
    }

    public static boolean registerCustomer(String username, String password, String phone, String email) {
        if (usernameExists(username)) {
            return false;
        }
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setCustomerId("C" + (customerList.size() + 1));
        customer.setLevel("铜牌顾客");
        customer.setRegisterTime(LocalDate.now().toString());
        customer.setTotalConsume(0);
        customer.setPhone(phone);
        customer.setEmail(email);
        customer.setFailedAttempt(0);
        customer.setLocked(false);
        customerList.add(customer);
        saveCustomers();
        return true;
    }

    public static boolean registerAdmin(String username, String password) {
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

    public static Customer loginCustomer(String username, String password) {
        for (Customer customer : customerList) {
            if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }

    public static Admin loginAdmin(String username, String password) {
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        return null;
    }

    public static boolean usernameExists(String username) {
        for (Customer customer : customerList) {
            if (customer.getUsername().equals(username)) {
                return true;
            }
        }
        for (Admin admin : adminList) {
            if (admin.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static Customer findCustomerByUsername(String username) {
        for (Customer customer : customerList) {
            if (customer.getUsername().equals(username)) {
                return customer;
            }
        }
        return null;
    }

    public static Customer findCustomerByCustomerId(String customerId) {
        for (Customer customer : customerList) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    public static void deleteCustomer(Customer customer) {
        customerList.remove(customer);
        saveCustomers();
    }

    public static ArrayList<Customer> getCustomerList() {
        return customerList;
    }

    public static ArrayList<Admin> getAdminList() {
        return adminList;
    }

    public static ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    public static Goods findGoodsByProductId(String id) {
        for (Goods g : goodsList) {
            if (g.getProductId().equals(id)) {
                return g;
            }
        }
        return null;
    }

    public static Goods findGoodsByProductName(String name) {
        for (Goods g : goodsList) {
            if (g.getProductName().equals(name)) {
                return g;
            }
        }
        return null;
    }

    public static boolean addGoods(Goods g) {
        if (findGoodsByProductId(g.getProductId()) != null) {
            return false;
        }
        goodsList.add(g);
        saveAll();
        return true;
    }

    public static void updateGoods(Goods g) {
        saveAll();
    }

    public static void deleteGoods(Goods g) {
        goodsList.remove(g);
        saveAll();
    }

    public static ArrayList<Goods> searchGoods(String name, String manufacturer, double minPrice) {
        ArrayList<Goods> result = new ArrayList<>();
        for (Goods g : goodsList) {
            boolean match = true;
            if (!name.isEmpty() && !g.getProductName().contains(name)) {
                match = false;
            }
            if (match && !manufacturer.isEmpty() && !g.getManufacturer().contains(manufacturer)) {
                match = false;
            }
            if (match && minPrice > 0 && g.getRetailPrice() < minPrice) {
                match = false;
            }
            if (match) {
                result.add(g);
            }
        }
        return result;
    }

    public static void addOrder(Order order) {
        orderList.add(order);
        saveAll();
    }

    public static int getOrderCount() {
        return orderList.size();
    }

    public static ArrayList<Order> getOrdersByUsername(String username) {
        ArrayList<Order> result = new ArrayList<>();
        for (Order o : orderList) {
            if (o.getUsername().equals(username)) {
                result.add(o);
            }
        }
        return result;
    }

    public static void saveAll() {
        syncCustomers();
        syncAdmins();
        syncGoods();
        syncOrders();
    }

    public static void saveCustomers() {
        saveAll();
    }

    public static void saveAdmins() {
        saveAll();
    }

    private static void loadCustomers() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM customers")) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setUsername(rs.getString("username"));
                customer.setPassword(rs.getString("password"));
                customer.setCustomerId(rs.getString("customerId"));
                customer.setLevel(rs.getString("level"));
                customer.setRegisterTime(rs.getString("registerTime"));
                customer.setTotalConsume(rs.getDouble("totalConsume"));
                customer.setPhone(rs.getString("phone"));
                customer.setEmail(rs.getString("email"));
                customer.setFailedAttempt(rs.getInt("failedAttempt"));
                customer.setLocked(rs.getInt("locked") == 1);
                customerList.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("读取顾客数据失败：" + e.getMessage());
        }
    }

    private static void loadAdmins() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM admins")) {
            while (rs.next()) {
                Admin admin = new Admin();
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                adminList.add(admin);
            }
        } catch (SQLException e) {
            System.out.println("读取管理员数据失败：" + e.getMessage());
        }
    }

    private static void loadGoods() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM goods")) {
            while (rs.next()) {
                Goods g = new Goods();
                g.setProductId(rs.getString("productId"));
                g.setProductName(rs.getString("productName"));
                g.setManufacturer(rs.getString("manufacturer"));
                g.setProduceDate(rs.getString("produceDate"));
                g.setModel(rs.getString("model"));
                g.setPurchasePrice(rs.getDouble("purchasePrice"));
                g.setRetailPrice(rs.getDouble("retailPrice"));
                g.setNum(rs.getInt("num"));
                goodsList.add(g);
            }
        } catch (SQLException e) {
            System.out.println("读取商品数据失败：" + e.getMessage());
        }
    }

    private static void loadOrders() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM orders")) {
            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getString("orderId"));
                o.setUsername(rs.getString("username"));
                o.setTime(rs.getString("time"));
                o.setDetail(rs.getString("detail"));
                o.setTotalAmount(rs.getDouble("totalAmount"));
                orderList.add(o);
            }
        } catch (SQLException e) {
            System.out.println("读取订单数据失败：" + e.getMessage());
        }
    }

    private static void syncCustomers() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute("DELETE FROM customers");
            String sql = "INSERT INTO customers VALUES (?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Customer c : customerList) {
                    ps.setString(1, c.getUsername());
                    ps.setString(2, c.getPassword());
                    ps.setString(3, c.getCustomerId());
                    ps.setString(4, c.getLevel());
                    ps.setString(5, c.getRegisterTime());
                    ps.setDouble(6, c.getTotalConsume());
                    ps.setString(7, c.getPhone());
                    ps.setString(8, c.getEmail());
                    ps.setInt(9, c.getFailedAttempt());
                    ps.setInt(10, c.isLocked() ? 1 : 0);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        } catch (SQLException e) {
            System.out.println("保存顾客数据失败：" + e.getMessage());
        }
    }

    private static void syncAdmins() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute("DELETE FROM admins");
            String sql = "INSERT INTO admins VALUES (?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Admin a : adminList) {
                    ps.setString(1, a.getUsername());
                    ps.setString(2, a.getPassword());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        } catch (SQLException e) {
            System.out.println("保存管理员数据失败：" + e.getMessage());
        }
    }

    private static void syncGoods() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute("DELETE FROM goods");
            String sql = "INSERT INTO goods VALUES (?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Goods g : goodsList) {
                    ps.setString(1, g.getProductId());
                    ps.setString(2, g.getProductName());
                    ps.setString(3, g.getManufacturer());
                    ps.setString(4, g.getProduceDate());
                    ps.setString(5, g.getModel());
                    ps.setDouble(6, g.getPurchasePrice());
                    ps.setDouble(7, g.getRetailPrice());
                    ps.setInt(8, g.getNum());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        } catch (SQLException e) {
            System.out.println("保存商品数据失败：" + e.getMessage());
        }
    }

    private static void syncOrders() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute("DELETE FROM orders");
            String sql = "INSERT INTO orders VALUES (?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Order o : orderList) {
                    ps.setString(1, o.getOrderId());
                    ps.setString(2, o.getUsername());
                    ps.setString(3, o.getTime());
                    ps.setString(4, o.getDetail());
                    ps.setDouble(5, o.getTotalAmount());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        } catch (SQLException e) {
            System.out.println("保存订单数据失败：" + e.getMessage());
        }
    }

    private static void seedGoods() {
        goodsList.add(makeGoods("G001", "可口可乐", "可口可乐公司", "2026-01-10", "COLA500", 2.0, 3.5, 100));
        goodsList.add(makeGoods("G002", "薯片", "乐事公司", "2026-02-01", "LSS125", 4.0, 6.0, 80));
        goodsList.add(makeGoods("G003", "牛奶", "蒙牛公司", "2026-03-15", "MN250", 4.5, 5.5, 50));
        goodsList.add(makeGoods("G004", "矿泉水", "农夫山泉", "2026-04-01", "NFS550", 1.0, 2.0, 200));
    }

    private static Goods makeGoods(String id, String name, String manufacturer, String date, String model, double buyPrice, double salePrice, int num) {
        Goods g = new Goods();
        g.setProductId(id);
        g.setProductName(name);
        g.setManufacturer(manufacturer);
        g.setProduceDate(date);
        g.setModel(model);
        g.setPurchasePrice(buyPrice);
        g.setRetailPrice(salePrice);
        g.setNum(num);
        return g;
    }
}
