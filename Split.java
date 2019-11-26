/**
 * Two MarketDays with a significant split in share price
 */

public class Split {
    private MarketDay d1;
    private MarketDay d2;
    private double ratio;
    private String type;

    public Split(MarketDay d1, MarketDay d2, double ratio, String type) {
        this.d1 = d1;
        this.d2 = d2;
        this.ratio = ratio;
        this.type = type;
    }

    public void print() {
        System.out.printf("%s split on %s %.2f --> %.2f\n",
                type, d1.getDate(), d1.getClosing(), d2.getOpening());
    }
}
