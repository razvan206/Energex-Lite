package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * I implemented this class to describe a Document of a certain day ...
 * with some helper method as well : addOrUpdatePoint, Get/Set/Remove points
 */
public final class MarketDocument {
    private static final int MAX_INTERVAL_INDEX = 96;

    private DataType dataType;
    private String biddingZoneDomain;
    private String periodStart;
    private String periodEnd;
    private String resolution;
    private String priceMeasureUnit;
    private String quantityMeasureUnit;
    private String currencyUnit;
    private String date;
    private Map<Integer, MarketPoint> points = new TreeMap<>();

    public void addOrUpdatePoint(final MarketPoint point) {
        if (point != null && point.getIntervalIndex() >= 1
                && point.getIntervalIndex() <= MAX_INTERVAL_INDEX) {
            this.points.put(point.getIntervalIndex(), point);
        }
    }

    public MarketPoint getPoint(final int intervalIndex) {
        return this.points.get(intervalIndex);
    }

    public void setPointsFromList(final List<MarketPoint> pointsList) {
        if (pointsList != null) {
            for (MarketPoint p : pointsList) {
                if (p != null) {
                    this.points.put(p.getIntervalIndex(), p);
                }
            }
        }
    }

    public List<MarketPoint> getPointsAsList() {
        return new ArrayList<>(this.points.values());
    }

    public MarketDocument(final DataType dataType,
                          final String biddingZoneDomain,
                          final String periodStart, final String periodEnd,
                          final String resolution,
                          final String priceMeasureUnit,
                          final String quantityMeasureUnit,
                          final String currencyUnit,
                          final Map<Integer, MarketPoint> points) {
        this.dataType = dataType;
        this.biddingZoneDomain = biddingZoneDomain;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.resolution = resolution;
        this.priceMeasureUnit = priceMeasureUnit;
        this.quantityMeasureUnit = quantityMeasureUnit;
        this.currencyUnit = currencyUnit;
        this.points = points;
    }

    public MarketDocument(final DataType type, final String date) {
        this.dataType = type;
        this.date = date;
        this.points = new TreeMap<>();
    }

    public String getBiddingZoneDomain() {
        return biddingZoneDomain;
    }

    public String getPeriodStart() {
        return periodStart;
    }

    public String getPeriodEnd() {
        return periodEnd;
    }

    public String getResolution() {
        return resolution;
    }

    public String getPriceMeasureUnit() {
        return priceMeasureUnit;
    }

    public String getQuantityMeasureUnit() {
        return quantityMeasureUnit;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(final DataType dataType) {
        this.dataType = dataType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public Map<Integer, MarketPoint> getPointsMap() {
        return points;
    }

    public void setPoints(final Map<Integer, MarketPoint> points) {
        this.points = points;
    }

    public void removePoint(final int intervalIndex) {
        this.points.remove(intervalIndex);
    }
}
