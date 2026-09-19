import java.util.Random;

public class Customer extends User {
    private String customerId;
    private String level;
    private String registerTime;
    private double totalConsume;
    private String phone;
    private String email;
    private int failedAttempt;
    private boolean locked;
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getRegisterTime() {
        return registerTime;
    }
    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
    public double getTotalConsume() {
        return totalConsume;
    }
    public void setTotalConsume(double totalConsume) {
        this.totalConsume = totalConsume;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getFailedAttempt() {
        return failedAttempt;
    }
    public void setFailedAttempt(int failedAttempt) {
        this.failedAttempt = failedAttempt;
    }
    public boolean isLocked() {
        return locked;
    }
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    public String createRandomPassword() {
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String symbols = "!@#$%^&*";
        String all = lowerCase + upperCase + digits + symbols;
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(symbols.charAt(random.nextInt(symbols.length())));
        for (int i = 4; i < 10; i++) {
            password.append(all.charAt(random.nextInt(all.length())));
        }
        return password.toString();
    }
}
