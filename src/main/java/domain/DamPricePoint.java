package domain;

import com.fasterxml.jackson.databind.node.ObjectNode;
import util.TimeConversionUtils;

public final class DamPricePoint extends MarketPoint {
    private static final double MIN_PRICE = -500.0;
    private static final double MAX_PRICE = 4000.0;

    private Double priceAmount;

    public DamPricePoint(final String date, final String interval,
                         final int intervalIndex, final Double priceAmount) {
        super(date, interval, intervalIndex);
        this.priceAmount = priceAmount;
    }

    public Double getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(final Double priceAmount) {
        this.priceAmount = priceAmount;
    }

    @Override
    public String validate(final Double prevQuantity) {
        if (priceAmount == null) {
            return "PriceAmount is null; interval skipped.";
        }
        if (priceAmount < MIN_PRICE || priceAmount > MAX_PRICE) {
            return "PriceAmount " + priceAmount
                    + " is outside the market range [-500, 4000] EUR/MWh;"
                    + " interval skipped.";
        }
        return null;
    }

    @Override
    public void fillJsonPoint(final ObjectNode node) {
        node.put("interval",
                TimeConversionUtils.indexToInterval(this.intervalIndex));
        node.put("intervalIndex", this.intervalIndex);
        node.put("priceAmount", this.priceAmount);
    }
}
