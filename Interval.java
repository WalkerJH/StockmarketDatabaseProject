/**
 * Stock data for a particular company, containing intervals
 **/

import java.util.*;

public class Interval {

    private String startDay;
    private String endDay;

    public Interval(String startDay) {
        this.startDay = startDay;
    }

    public void end(String endDay) {
        this.endDay = endDay;
    }

    public String getStartDay() { return startDay; }

    public String getEndDay() { return endDay; }
}