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
        int numDays = intervals.get(0).getDays().size();
        for (int i = 0; i < numDays; i++) {
            int j = 0;
            int currentInterval = 0;
            addInterval();
            while (intervals.get(0).getDays().size() > 0) {
                if (j < 60) {
                    
                }
                else {
                    j++;
                }
            }
        }
    }
}