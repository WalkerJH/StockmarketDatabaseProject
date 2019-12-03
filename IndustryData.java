import java.util.ArrayList;

public class IndustryData {

    private ArrayList<CompanyData> tickerList;

    private String name;
    private String startDate;
    private String endDate;
    private int commonDays;

    public IndustryData(String name, ArrayList<CompanyData> tickerList, String startDate, String endDate, int commonDays) {
        this.tickerList = tickerList;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.commonDays = commonDays;
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

    public double getIndustryReturn(CompanyData ignoredCompany) {
        double industryReturn = 0;
        double s = 0;
        for (CompanyData c : tickerList) {
            s += (c.getCompanyClosePrice()/c.getCompanyOpenPrice());
        }
        industryReturn *= s;
        return industryReturn;
    }
}
