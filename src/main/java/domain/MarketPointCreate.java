package domain;

import fileio.MarketDataInput;
import util.TimeConversionUtils;

public final class MarketPointCreate {
    public MarketPointCreate() {
    }

    public static MarketPoint createPoint(final DataType type,
                                          final MarketDataInput input) {
        Double quantity = input.quantity() != null
                ? input.quantity().doubleValue() : null;
        int index = TimeConversionUtils.intervalToIndex(input.interval());
        return switch (type) {
            case DAM_PRICE -> new DamPricePoint(
                    input.date(), input.interval(), index, input.priceAmount());
            case DAM_PRICE_PREDICTION -> new DamPricePredictionPoint(
                    input.date(), input.interval(), index,
                    input.predictedPriceEurMwh(), input.spikeProbability());
            case GRID_LOAD -> new GridLoadPoint(
                    input.date(), input.interval(), index,
                    quantity, input.reservesRequiredMW());
            case GRID_LOAD_PREDICTION -> new GridLoadPredictionPoint(
                    input.date(), input.interval(), index,
                    quantity, input.reservesRequiredMW());
            case IMBALANCE_PRICE -> new ImbalancePricesPoint(
                    input.date(), input.interval(), index,
                    input.positiveImbalancePrice(),
                    input.negativeImbalancePrice());
            case IMBALANCE_VOLUME -> new ImbalanceVolumesPoint(
                    input.date(), input.interval(), index,
                    input.positiveImbalanceVol(), input.negativeImbalanceVol());
        };
    }
}
