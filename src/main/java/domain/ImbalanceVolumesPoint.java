package domain;

import com.fasterxml.jackson.databind.node.ObjectNode;
import util.TimeConversionUtils;

public final class ImbalanceVolumesPoint extends MarketPoint {
    private Double positiveImbalanceVolume;
    private Double negativeImbalanceVolume;

    public ImbalanceVolumesPoint(final String date, final String interval,
                                 final int intervalIndex,
                                 final Double positiveImbalanceVolume,
                                 final Double negativeImbalanceVolume) {
        super(date, interval, intervalIndex);
        this.positiveImbalanceVolume = positiveImbalanceVolume;
        this.negativeImbalanceVolume = negativeImbalanceVolume;
    }

    public Double getPositiveImbalanceVolume() {
        return positiveImbalanceVolume;
    }

    public void setPositiveImbalanceVolume(
            final Double positiveImbalanceVolume) {
        this.positiveImbalanceVolume = positiveImbalanceVolume;
    }

    public Double getNegativeImbalanceVolume() {
        return negativeImbalanceVolume;
    }

    public void setNegativeImbalanceVolume(
            final Double negativeImbalanceVolume) {
        this.negativeImbalanceVolume = negativeImbalanceVolume;
    }

    @Override
    public String validate(final Double prevQuantity) {
        if (this.positiveImbalanceVolume == null) {
            return "PositiveImbalanceVol is null; interval skipped.";
        }
        if (this.positiveImbalanceVolume < 0) {
            return "PositiveImbalanceVol " + positiveImbalanceVolume
                    + " is below the minimum allowed value 0;"
                    + " interval skipped.";
        }
        if (this.negativeImbalanceVolume == null) {
            return "NegativeImbalanceVol is null; interval skipped.";
        }
        if (this.negativeImbalanceVolume < 0) {
            return "NegativeImbalanceVol " + negativeImbalanceVolume
                    + " is below the minimum allowed value 0;"
                    + " interval skipped.";
        }
        return null;
    }

    @Override
    public void fillJsonPoint(final ObjectNode node) {
        node.put("interval",
                TimeConversionUtils.indexToInterval(this.intervalIndex));
        node.put("intervalIndex", this.intervalIndex);
        node.put("positiveImbalanceVol", this.positiveImbalanceVolume);
        node.put("negativeImbalanceVol", this.negativeImbalanceVolume);
    }
}
