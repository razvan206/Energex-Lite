package domain;

import com.fasterxml.jackson.databind.node.ObjectNode;
import util.TimeConversionUtils;

public final class DamPricePredictionPoint extends MarketPoint {
    private static final double MIN_PRICE = -500.0;
    private static final double MAX_PRICE = 4000.0;
    private static final double MIN_PROBABILITY = 0.0;
    private static final double MAX_PROBABILITY = 1.0;

    private Double predictedPriceEurMhw;
    private Double spikeProbability;

    public DamPricePredictionPoint(final String date, final String interval,
                                   final int intervalIndex,
                                   final Double predictedPriceEurMhw,
                                   final Double spikeProbability) {
        super(date, interval, intervalIndex);
        this.predictedPriceEurMhw = predictedPriceEurMhw;
        this.spikeProbability = spikeProbability;
    }

    public Double getPredictedPriceEurMhw() {
        return predictedPriceEurMhw;
    }

    public void setPredictedPriceEurMhw(final Double predictedPriceEurMhw) {
        this.predictedPriceEurMhw = predictedPriceEurMhw;
    }

    public Double getSpikeProbability() {
        return spikeProbability;
    }

    public void setSpikeProbability(final Double spikeProbability) {
        this.spikeProbability = spikeProbability;
    }

    @Override
    public String validate(final Double prevQuantity) {
        if (this.predictedPriceEurMhw == null) {
            return "PredictedPriceEurMwh is null; interval skipped.";
        }
        if (this.predictedPriceEurMhw < MIN_PRICE
                || this.predictedPriceEurMhw > MAX_PRICE) {
            return "PredictedPriceEurMwh " + predictedPriceEurMhw
                    + " is outside the market range [-500, 4000] EUR/MWh;"
                    + " interval skipped.";
        }
        if (this.spikeProbability == null) {
            return "SpikeProbability is null; interval skipped.";
        }
        if (this.spikeProbability < MIN_PROBABILITY
                || this.spikeProbability > MAX_PROBABILITY) {
            return "SpikeProbability " + this.spikeProbability
                    + " is outside the valid range [0.0, 1.0];"
                    + " interval skipped.";
        }
        return null;
    }

    @Override
    public void fillJsonPoint(final ObjectNode node) {
        node.put("interval",
                TimeConversionUtils.indexToInterval(this.intervalIndex));
        node.put("intervalIndex", this.intervalIndex);
        node.put("predictedPriceEurMwh", this.predictedPriceEurMhw);
        node.put("spikeProbability", this.spikeProbability);
    }
}
