import java.util.ArrayList;

public class IndustryData {

    private ArrayList<String> tickerList;
    private String industry;
    private String startDate;
    private String endDate;
    private int commonDays;

    public IndustryData(ArrayList<String> tickerList, String industry, String startDate, String endDate) {
        this.tickerList = tickerList;
        this.industry = industry;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ArrayList<String> getTickerList() {
        return tickerList;
    }

    public String getIndustry() {
        return industry;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getCommonDays() {
        return commonDays;
    }
}
