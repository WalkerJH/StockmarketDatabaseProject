import java.util.ArrayList;

public class IndustryData {

    private ArrayList<CompanyData> tickerList;

    private String name;
    private String startDate;
    private String endDate;
    private int commonDays;

    public IndustryData(String name, ArrayList<CompanyData> tickerList, String startDate, String endDate) {
        this.tickerList = tickerList;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ArrayList<CompanyData> getTickerList() {
        return tickerList;
    }

    public String getName() {
        return name;
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
