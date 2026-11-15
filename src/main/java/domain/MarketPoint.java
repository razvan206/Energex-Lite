package domain;

import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class MarketPoint {
    protected String date;
    protected int intervalIndex;
    protected String interval;

    public final String getDate() {
        return date;
    }

    public final int getIntervalIndex() {
        return intervalIndex;
    }

    public final String getInterval() {
        return interval;
    }

    public final void setDate(final String date) {
        this.date = date;
    }

    public final void setInterval(final String interval) {
        this.interval = interval;
    }

    public final void setIntervalIndex(final int intervalIndex) {
        this.intervalIndex = intervalIndex;
    }

    public MarketPoint(final String date, final String interval,
                       final int intervalIndex) {
        this.date = date;
        this.interval = interval;
        this.intervalIndex = intervalIndex;
    }

    public abstract String validate(Double prevQuantity);

    public abstract void fillJsonPoint(ObjectNode node);
}
