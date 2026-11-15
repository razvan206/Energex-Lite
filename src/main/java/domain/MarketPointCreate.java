package domain;

import fileio.MarketDataInput;
import util.TimeConversionUtils;

import java.sql.Time;

public class MarketPointCreate {
    public static MarketPoint createPoint(DataType type, MarketDataInput input){
        Double quantity = input.quantity() != null ? input.quantity().doubleValue() : null;
        return switch (type){
            case DAM_PRICE -> new DamPricePoint(input.date(),input.interval(), TimeConversionUtils.intervalToIndex(input.interval()),input.priceAmount());
            case DAM_PRICE_PREDICTION -> new DamPricePredictionPoint(input.date(),input.interval(),TimeConversionUtils.intervalToIndex(input.interval()),input.predictedPriceEurMwh(), input.spikeProbability());
            case GRID_LOAD -> new GridLoadPoint(input.date(),input.interval(),TimeConversionUtils.intervalToIndex(input.interval()),quantity,input.reservesRequiredMW());
            case GRID_LOAD_PREDICTION -> new GridLoadPredictionPoint(input.date(), input.interval(), TimeConversionUtils.intervalToIndex(input.interval()),quantity,input.reservesRequiredMW());
            case IMBALANCE_PRICE -> new ImbalancePricesPoint(input.date(), input.interval(), TimeConversionUtils.intervalToIndex(input.interval()), input.positiveImbalancePrice(), input.negativeImbalancePrice());
            case IMBALANCE_VOLUME -> new ImbalanceVolumesPoint(input.date(), input.interval(), TimeConversionUtils.intervalToIndex(input.interval()), input.positiveImbalanceVol(), input.negativeImbalanceVol());
        };
    }
}
