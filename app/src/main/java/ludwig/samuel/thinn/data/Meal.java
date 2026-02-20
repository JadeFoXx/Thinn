package ludwig.samuel.thinn.data;

public class Meal {
    private String name;
    private int calories;
    private int color;
    private String type = "food";

    public static final int DEFAULT_COLOR = 0xFF3B82F6;

    public Meal(String name, int calories, int color) {
        this.name = name;
        this.calories = calories;
        this.color = color;
    }

    public Meal(String name, int calories, int color, String type) {
        this.name = name;
        this.calories = calories;
        this.color = color;
        this.type = type;
    }

    public Meal(String name, int calories) {
        this(name, calories, DEFAULT_COLOR);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getColor() {
        return color == 0 ? DEFAULT_COLOR : color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getType() {
        return type == null ? "food" : type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
