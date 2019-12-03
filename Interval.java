/**
 * Stock data for a particular company, containing intervals
 **/

import java.util.*;

public class Interval {

    private String startDay;
    private String endDay;
    private double tickerReturn;
    private double openPrice;

    public Interval(String startDay) { this.startDay = startDay; }

    public void setEndDay(String endDay) { this.endDay = endDay; }

    public void setOpenPrice(double openPrice) { this.openPrice = openPrice; }

    public void calcTickerReturn(double closePrice) {
        tickerReturn = (closePrice/openPrice) - 1;
    }

    public String getStartDay() { return startDay; }

    public String getEndDay() { return endDay; }

    public double getTickerReturn() { return tickerReturn; }
}