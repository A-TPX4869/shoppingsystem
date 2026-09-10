public class Main {
    public static void main(String[] args) {
        UserManager.loadData();
        ShoppingSystem sys = new ShoppingSystem();
        sys.StartMenu();
    }
}
