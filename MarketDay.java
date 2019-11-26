/**
 * Day on the market for a particular stock
 */

public class MarketDay {

    private String ticker;
    private String date;
    private double opening;
    private double high;
    private double low;
    private double closing;
    private int shares;
    private double adjustedClosing;
    private double newOpening;
    private double newHigh;
    private double newLow;
    private double newClosing;

    public MarketDay(String ticker, String date, double opening, double high, double low,
                     double closing, int shares, double adjustedClosing) {
        this.ticker = ticker;
        this.date = date;
        this.opening = opening;
        this.high = high;
        this.low = low;
        this.closing = closing;
        this.shares = shares;
        this.adjustedClosing = adjustedClosing;
    }

    public double getOpening() { return opening; }

    public double getClosing() { return closing; }

    public double getNewOpening() { return newOpening; }

    public double getNewClosing() { return newClosing; }

    public String getDate() { return date; }

    public void adjust(double divisor) {
        newOpening = opening / divisor;
        newHigh  = high / divisor;
        newLow  = low / divisor;
        newClosing = closing / divisor;
    }

    public void print() {
        System.out.printf("opening %f high %f low %f closing %f\n",
                opening, high, low, closing);
    }
}
