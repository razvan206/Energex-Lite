package domain;

import com.fasterxml.jackson.databind.node.ObjectNode;
import util.TimeConversionUtils;

public final class GridLoadPoint extends MarketPoint {
    private static final double MAX_CHANGE = 10000.0;

    private Double quantity;
    private Double reservesRequiredMW;

    public GridLoadPoint(final String date, final String interval,
                         final int intervalIndex, final Double quantity,
                         final Double reservesRequiredMW) {
        super(date, interval, intervalIndex);
        this.quantity = quantity;
        this.reservesRequiredMW = reservesRequiredMW;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(final Double quantity) {
        this.quantity = quantity;
    }

    public Double getReservesRequiredMW() {
        return reservesRequiredMW;
    }

    public void setReservesRequiredMW(final Double reservesRequiredMW) {
        this.reservesRequiredMW = reservesRequiredMW;
    }

    @Override
    public String validate(final Double prevQuantity) {
        if (this.quantity == null) {
            return "Quantity is null; interval skipped.";
        }
        if (this.quantity < 0) {
            return "Quantity " + quantity
                    + " is below the minimum allowed value 0; interval skipped.";
        }
        if (prevQuantity != null
                && Math.abs(this.quantity - prevQuantity) > MAX_CHANGE) {
            return "Quantity change exceeds 10000 MW compared with the previous"
                    + " interval; interval skipped.";
        }
        return null;
    }

    @Override
    public void fillJsonPoint(final ObjectNode node) {
        node.put("interval",
                TimeConversionUtils.indexToInterval(this.intervalIndex));
        node.put("intervalIndex", this.intervalIndex);
        node.put("quantity", this.quantity);
    }
}
