/**
 * Stock data for a particular company, containing intervals
 **/

import java.util.*;

public class CompanyData {

    private String ticker;
    private ArrayList<CompanyDataInterval> intervals;

    public CompanyData(String ticker) {
        this.ticker = ticker;
        intervals = new ArrayList<CompanyDataInterval>();
    }

    public String getTicker() { return ticker; }

    public void addInterval() {
        intervals.add(new CompanyDataInterval());
    }

    public CompanyDataInterval getInterval(int index) {
        return intervals.get(index);
    }

    //Split up one interval into as many as needed
    public void splitIntervals() {
        this.addInterval();
        int currentInterval = 0;
        for (int i = 0; i < 60; i++) {
            MarketDay day = intervals.get(currentInterval).getDays().removeLast();
            //intervals.get(currentInterval+1).getDays().addFirst();
        }
    }
}