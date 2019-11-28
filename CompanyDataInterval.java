/**
 * Stock data for a particular company
 **/

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class CompanyDataInterval {

    private int intervalNum;
    private Deque<MarketDay> days;
    private Deque<MarketDay> adjustedDays;
    private ArrayList<Split> splits;
    private int numDays;

    public CompanyDataInterval(int intervalNum) {
        this.intervalNum = intervalNum;
        days = new LinkedList<MarketDay>();
        splits = new ArrayList<Split>();
        adjustedDays = new LinkedList<MarketDay>();
    }

    public int getIntervalNum() { return intervalNum; }

    public void addDay(MarketDay m) {
        days.add(m);
    }

    //Adjust data to account for price variance (splits)
    public void adjustForSplits() {
        double divisor = 1;
        boolean canPull = true;
        while (canPull) {
            MarketDay current = days.pollFirst();
            MarketDay older = days.pollFirst();
            if (older != null) {
                double splitRatio = older.getClosing() / current.getOpening();
                if (Math.abs(splitRatio - 2.0) < 0.20) {
                    divisor *= 2;
                    splits.add(new Split(older, current, splitRatio, "2:1"));
                }
                if (Math.abs(splitRatio - 3.0) < 0.30) {
                    divisor *= 3;
                    splits.add(new Split(older, current, splitRatio, "3:1"));
                }
                if (Math.abs(splitRatio - 1.5) < 0.15) {
                    divisor *= 1.5;
                    splits.add(new Split(older, current, splitRatio, "3:2"));
                }
                days.push(older);
            } else {
                canPull = false;
            }
            current.adjust(divisor);
            adjustedDays.add(current);
            numDays++;
        }
    }

    public void printAdjustedSplits() {
        for (Split s : splits) {
            s.print();
        }
        System.out.printf("%d splits in %d trading days\n", splits.size(), numDays);
    }
}