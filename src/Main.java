public class Main {
    public static void main(String[] args) {
        UserManager.loadData();
        ShoppingSystem system = new ShoppingSystem();
        system.startMenu();

    }
}
