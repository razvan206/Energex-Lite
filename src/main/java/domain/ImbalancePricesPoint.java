package domain;

import com.fasterxml.jackson.databind.node.ObjectNode;
import util.TimeConversionUtils;

public final class ImbalancePricesPoint extends MarketPoint {
    private static final double MAX_PRICE = 15000.0;

    private Double positiveImbalancePrice;
    private Double negativeImbalancePrice;

    public ImbalancePricesPoint(final String date, final String interval,
                                final int intervalIndex,
                                final Double positiveImbalancePrice,
                                final Double negativeImbalancePrice) {
        super(date, interval, intervalIndex);
        this.positiveImbalancePrice = positiveImbalancePrice;
        this.negativeImbalancePrice = negativeImbalancePrice;
    }

    public Double getPositiveImbalancePrice() {
        return positiveImbalancePrice;
    }

    public void setPositiveImbalancePrice(final Double positiveImbalancePrice) {
        this.positiveImbalancePrice = positiveImbalancePrice;
    }

    public Double getNegativeImbalancePrice() {
        return negativeImbalancePrice;
    }

    public void setNegativeImbalancePrice(final Double negativeImbalancePrice) {
        this.negativeImbalancePrice = negativeImbalancePrice;
    }

    @Override
    public String validate(final Double prevQuantity) {
        if (this.positiveImbalancePrice == null) {
            return "PositiveImbalancePrice is null; interval skipped.";
        }
        if (Math.abs(this.positiveImbalancePrice) > MAX_PRICE) {
            return "PositiveImbalancePrice " + this.positiveImbalancePrice
                    + " exceeds the maximum allowed value 15000 EUR/MWh;"
                    + " interval skipped.";
        }
        if (this.negativeImbalancePrice == null) {
            return "NegativeImbalancePrice is null; interval skipped.";
        }
        if (Math.abs(this.negativeImbalancePrice) > MAX_PRICE) {
            return "NegativeImbalancePrice " + this.negativeImbalancePrice
                    + " exceeds the maximum allowed value 15000 EUR/MWh;"
                    + " interval skipped.";
        }
        return null;
    }

    @Override
    public void fillJsonPoint(final ObjectNode node) {
        node.put("interval",
                TimeConversionUtils.indexToInterval(this.intervalIndex));
        node.put("intervalIndex", this.intervalIndex);
        node.put("positiveImbalancePrice", this.positiveImbalancePrice);
        node.put("negativeImbalancePrice", this.negativeImbalancePrice);
    }
}
