public class CartItem {
    private Goods goods;
    private int count;
    public CartItem(Goods goods, int count) {
        this.goods = goods;
        this.count = count;
    }
    public Goods getGoods() {
        return goods;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
