/*
* CS 330 Project 3
* Walker Herring
*/

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.io.*;

public class Assignment3Main {

    static final String defaultReaderParams = "readerparams.txt";
    static final String defaultWriterParams = "writerparams.txt";
    static Connection readerConn = null;
    static Connection writerConn = null;
	// Other variables
	    
    static final String getDatesQuery =
        "select max(startDate) as first, min(endDate) as last" +
        "  from (select Ticker, min(TransDate) as StartDate, max(TransDate) as endDate," +
        "            count(distinct TransDate) as tradingDays" +
        "          from Company natural join PriceVolume" +
        "          where Industry = ?" +
        "          group by Ticker" +
        "          having tradingDays >= ?) as TickerDates";

    static final String getTickerDatesQuery = 
        "select Ticker, min(TransDate) as StartDate, max(TransDate) as endDate," +
        "      count(distinct TransDate) as tradingDays" +
        "  from Company natural join PriceVolume" +
        "  where Industry = ?" +
        "    and TransDate >= ? and TransDate <= ?" +
        "  group by Ticker" +
        "  having tradingDays >= ?" +
        "  order by Ticker";

    static final String getMinTradingDaysQuery =
        "select min(tradingDays)" +
            " from (select count(distinct TransDate) as tradingDays" +
                    "  from Company natural join PriceVolume" +
                    "  where Industry = ?" +
                    "    and TransDate >= ? and TransDate <= ?" +
                    "  group by Ticker" +
                    "  having tradingDays >= ?" +
                    "  order by Ticker) as tradingDaysNums";
        
    static final String getIndustryPriceDataQuery =
        "select Ticker, TransDate, OpenPrice, ClosePrice" +
        "  from PriceVolume natural join Company" +
        "  where Industry = ?" +
        "    and TransDate >= ? and TransDate <= ?" +
        "  order by TransDate, Ticker";

	static final String getAllIndustries = 
            "select distinct Industry" +
            "  from Company" +
            "  order by Industry";		

	static final String getTickersByIndustry =
            "select ticker from Company" +
            "   where industry = ?";

    static final String dropPerformanceTable =
        "drop table if exists Performance;";

    static final String createPerformanceTable =
        "create table Performance (" +
        "  Industry char(30)," +
        "  Ticker char(6)," +
        "  StartDate char(10)," +
        "  EndDate char(10)," +
        "  TickerReturn char(12)," +
        "  IndustryReturn char(12)" +
        "  );";
    
    static final String insertPerformance =
        "insert into Performance(Industry, Ticker, StartDate, EndDate, TickerReturn, IndustryReturn)" +
        "  values(?, ?, ?, ?, ?, ?);";

    public static void main(String[] args) throws Exception {
    
        // Get connection properties
        Properties readerProps = new Properties();
        readerProps.load(new FileInputStream(defaultReaderParams));
        Properties writerProps = new Properties();
        writerProps.load(new FileInputStream(defaultWriterParams));

        try {
            // Setup Reader and Writer Connection
            setupReader(readerProps);
            setupWriter(writerProps);
            
            List<String> industries = getIndustries();
            System.out.printf("%d industries found%n", industries.size());
            for (String industry : industries) {
                System.out.printf("%s%n", industry);
            }
            System.out.println();
            
			for (String industry : industries) {
                System.out.printf("Processing %s%n", industry);
                IndustryData iData = processIndustry(industry);
                if (iData != null && iData.getTickerList().size() > 2) {
                    System.out.printf("%d accepted tickers for %s(%s - %s), %d common dates%n",
                        iData.getTickerList().size(), industry, iData.getStartDate(), iData.getEndDate(), iData.getCommonDays());
                    processIndustryGains(industry, iData);
                }
                else
                    System.out.printf("Insufficient data for %s => no analysis%n", industry);
                System.out.println();
            }
            
            // Close everything you don't need any more 
			
            System.out.println("Database connections closed");
        } catch (SQLException ex) {
            System.out.printf("SQLException: %s%nSQLState: %s%nVendorError: %s%n",
                                    ex.getMessage(), ex.getSQLState(), ex.getErrorCode());
            ex.printStackTrace();
        }
    }
    
    static void setupReader(Properties connectProps) throws SQLException {
        String dburl = connectProps.getProperty("dburl");
        String username = connectProps.getProperty("user");
        readerConn = DriverManager.getConnection(dburl, connectProps);
        System.out.printf("Reader connection %s %s established.%n", dburl, username);
    }
    
    
    static void setupWriter(Properties connectProps) throws SQLException {
        String dburl = connectProps.getProperty("dburl");
        String username = connectProps.getProperty("user");
        writerConn = DriverManager.getConnection(dburl, connectProps);
        System.out.printf("Writer connection %s %s established.%n", dburl, username);

        // Create Performance Table; If this table already exists, drop it first
        Statement tstmt = writerConn.createStatement();
        tstmt.execute(dropPerformanceTable);
        tstmt.execute(createPerformanceTable);
        tstmt.close();
    } 
    
    static List<String> getIndustries() throws SQLException {
		List<String> industries = new ArrayList<>();
        PreparedStatement prepst = readerConn.prepareStatement(getAllIndustries);
        ResultSet industryResults = prepst.executeQuery();
        while (industryResults.next()) {
            industries.add(industryResults.getString("Industry"));
        }
		return industries;
    }
    
    static IndustryData processIndustry(String industry) throws SQLException {

        ArrayList<CompanyData> companies = new ArrayList<CompanyData>();

        PreparedStatement prepstTickers = readerConn.prepareStatement(getTickersByIndustry);
        prepstTickers.setString(1, industry);
        ResultSet tickerResults = prepstTickers.executeQuery();
        while (tickerResults.next()) {
            companies.add(new CompanyData(tickerResults.getString("ticker")));
        }

        PreparedStatement prepstDays = readerConn.prepareStatement(getDatesQuery);
        prepstDays.setString(1, industry);
        prepstDays.setInt(2, 150);
        ResultSet daysResults = prepstDays.executeQuery();
        daysResults.next();
        String beginDateRange = daysResults.getString("first");
        String endDateRange = daysResults.getString("last");

        PreparedStatement prepstFinalDays = readerConn.prepareStatement(getTickerDatesQuery);
        prepstFinalDays.setString(1, industry);
        prepstFinalDays.setString(2, beginDateRange);
        prepstFinalDays.setString(3, endDateRange);
        prepstFinalDays.setInt(4, 150);
        ResultSet finalDaysResults = prepstFinalDays.executeQuery();
        if (finalDaysResults.next()) {
            String startDate = finalDaysResults.getString("StartDate");
            String endDate = finalDaysResults.getString("endDate");

            PreparedStatement prepstMinDays = readerConn.prepareStatement(getMinTradingDaysQuery);
            prepstMinDays.setString(1, industry);
            prepstMinDays.setString(2, beginDateRange);
            prepstMinDays.setString(3, endDateRange);
            prepstMinDays.setInt(4, 150);
            ResultSet minDaysResult = prepstMinDays.executeQuery();
            minDaysResult.next();
            int minTradingDays = minDaysResult.getInt("min(tradingDays)");

            int intervals = minTradingDays / 60;

            PreparedStatement prepstPriceData = readerConn.prepareStatement(getIndustryPriceDataQuery);
            prepstPriceData.setString(1, industry);
            prepstPriceData.setString(2, startDate);
            prepstPriceData.setString(3, startDate);

            for (CompanyData c : companies) {

                ResultSet priceDataResults = prepstPriceData.executeQuery();
                double close = 0;
                while (priceDataResults.next()) {
                    String ticker = c.getTicker();
                    String date = priceDataResults.getString("TransDate");
                    double open = priceDataResults.getDouble("OpenPrice");
                    close = priceDataResults.getDouble("ClosePrice");
                    c.addDay(new MarketDay(ticker, date, open, close));
                }
                c.setCompanyClosePrice(close);

                c.adjustForSplits();

                //System.out.printf("Processing company %s\n", c.getTicker());
            }
            return new IndustryData(industry, companies, startDate, endDate, minTradingDays);
        } else {
            return null;
        }
    }
    
    static void processIndustryGains(String industry, IndustryData data) throws SQLException {
        PreparedStatement psmtInsertPerformance = writerConn.prepareStatement(insertPerformance);
        String ticker = "";
        String startDate = "";
        String endDate = "";
        double tickerReturn = 0;
        double industryReturn = 0;

        for (CompanyData c : data.getTickerList()) {
            for (int i = 1; i < c.getNumDays()-1; i++) {
                MarketDay d = c.getAdjustedDays().removeLast();
                if (i == 1 || (i - 1) % 60 == 0) {
                    c.openInterval(d.getDate(), d.getNewOpening());
                }
                if (i % 60 == 0) {
                    c.closeInterval(d.getDate(), d.getNewClosing());
                }
            }
        industryReturn = data.getIndustryReturn(c);
        for (int i = 0; i < data.getTickerList().size(); i++) {
            for (int j = 0; j < data.getTickerList().size(); j++) {

            }
        }
        psmtInsertPerformance.setString(1, industry);
        psmtInsertPerformance.setString(2, ticker);
        psmtInsertPerformance.setString(3, startDate);
        psmtInsertPerformance.setString(4, endDate);
        psmtInsertPerformance.setString(5, String.format("%10.7f", tickerReturn));
        psmtInsertPerformance.setString(6, String.format("%10.7f", industryReturn));
        int result = psmtInsertPerformance.executeUpdate();
        }
    }
}
