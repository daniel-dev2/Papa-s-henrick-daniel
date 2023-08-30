public class Item {

    private String description;
    private float  weigth;

    public Item(String description, float weigth) {
        this.description = description;
        this.weigth = weigth;
    }

    public String getDescription() {
        return description;
    }

    public float getWeigth() {
        return weigth;
    }
}
