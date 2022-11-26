public class ConcurrentCount {
    private int count = 0;

    public synchronized void incrementCount() {
        count++;
    }

    public synchronized void incrementBy(int num) {
        count+=num;
    }

    public synchronized int getCount() {
        return count;
    }
}