public class Counter {
    private int value;

    public Counter() {
        this.value = 0;
    }

    public void increase(int number) {
        this.value += number;
    }

    public void decrease(int number) {
        this.value -= number;
    }

    public int getValue() {
        return this.value;
    }
}