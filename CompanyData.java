/**
 * Stock data for an interval
 **/

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class CompanyData {

    private String ticker;
    private Deque<MarketDay> days;
    private Deque<MarketDay> adjustedDays;
    private ArrayList<Split> splits;
    private ArrayList<Interval> intervals;
    private int numDays;
    private double companyOpenPrice;
    private double companyClosePrice;


    public CompanyData(String ticker) {
        this.ticker = ticker;
        this.days = new LinkedList<MarketDay>();
        this.adjustedDays = new LinkedList<MarketDay>();
        this.splits = new ArrayList<Split>();
        this.intervals = new ArrayList<Interval>();
    }

    public Deque<MarketDay> getDays() { return days; }

    public Deque<MarketDay> getAdjustedDays() { return adjustedDays; }


    public double getCompanyOpenPrice() { return companyOpenPrice; }

    public double getCompanyClosePrice() { return companyClosePrice; }

    public void addDay(MarketDay m) {
        if (days.size() == 0)
            companyOpenPrice = m.getNewOpening();
        days.add(m);
    }

    public void setCompanyClosePrice(double price) {
        this.companyClosePrice = price;
    }

    public void openInterval(String startDate, double openPrice) {
        intervals.add(new Interval(startDate));
    }

    public void closeInterval(String endDate, double closePrice) {
        intervals.get(intervals.size()-1).setEndDay(endDate);
        intervals.get(intervals.size()-1).calcTickerReturn(closePrice);
    }

    //Adjust data to account for price variance (splits)
    public double adjustForSplits() {
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
        return divisor;
    }

    public String getTicker() { return ticker; }

    public int getNumDays() { return numDays; }

    public void printAdjustedSplits() {
        for (Split s : splits) {
            s.print();
        }
        System.out.printf("%d splits in %d trading days\n", splits.size(), numDays);
    }
}