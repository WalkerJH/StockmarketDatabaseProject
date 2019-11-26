/**
 * Stores moving average of n doubles
 */

import java.util.*;

public class MovingAverage {

    private ArrayList<Double> nums;
    private int maxSize;
    private int currentSize;
    private double sum;
    private double avg;

    public MovingAverage(int maxSize) {
        this.nums = new ArrayList<Double>();
        this.maxSize = maxSize;
        this.currentSize = 0;
        this.sum = 0;
        this.avg = 0;
    }

    public double getAvg() {
        return avg;
    }

    //Recalculate average as a number is added
    public void addNum(double num) {
        nums.add(num);
        sum += num;
        if (currentSize < maxSize) {
            currentSize++;
        } else {
            sum -= nums.get(0);
            nums.remove(0);
        }
        avg = sum/currentSize;
    }

    public boolean isFull() {
        if (currentSize < maxSize)
            return false;
        else
            return true;
    }
}
