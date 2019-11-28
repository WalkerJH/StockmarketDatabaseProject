/**
 * Stock data for a particular company
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

    //Adjust data to account for price variance (splits)
    public void adjustForSplits() {
        for (CompanyDataInterval i : intervals) {
            i.adjustForSplits();
        }
    }
}