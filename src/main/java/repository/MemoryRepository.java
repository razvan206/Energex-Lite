package repository;

import domain.DataType;
import domain.GridLoadPoint;
import domain.MarketDocument;
import domain.MarketPoint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class implements the storage for every type of Doc ...
 *  with getters for Points, getLatests for GetCommand or PointsInRange ...
 *  for intervals that come with get query
 */
public final class MemoryRepository implements MarketRepository {
    private static final int MINUTES_PER_INTERVAL = 15;
    private Map<DataType, Map<String, MarketDocument>> storage;

    public MemoryRepository() {
        this.storage = new HashMap<>();

        for (DataType type : DataType.values()) {
            this.storage.put(type, new TreeMap<>());
        }
    }

    @Override
    public void savePoint(final DataType type, final String date,
                          final MarketPoint point) {
        Map<String, MarketDocument> typeDocument = this.storage.get(type);
        MarketDocument dayDocument = typeDocument.computeIfAbsent(
                date, d -> new MarketDocument(type, date));
        dayDocument.addOrUpdatePoint(point);
    }

    @Override
    public Double getPreviousGridLoad(final String date,
                                      final int currentIndex) {
        Map<String, MarketDocument> gridLoadDocument =
                this.storage.get(DataType.GRID_LOAD);
        if (gridLoadDocument == null) {
            return null;
        }
        MarketDocument dayDocument = gridLoadDocument.get(date);
        if (dayDocument == null) {
            return null;
        }
        TreeMap<Integer, MarketPoint> points =
                (TreeMap<Integer, MarketPoint>) dayDocument.getPointsMap();

        if (points == null || points.isEmpty()) {
            return null;
        }

        Map.Entry<Integer, MarketPoint> previousEntry =
                points.lowerEntry(currentIndex);

        if (previousEntry != null) {
            GridLoadPoint previousPoint =
                    (GridLoadPoint) previousEntry.getValue();
            return previousPoint.getQuantity();
        }

        return null;
    }

    @Override
    public MarketPoint getPoint(final DataType type, final String date,
                                final int intervalIndex) {
        Map<String, MarketDocument> typeDocument = this.storage.get(type);
        if (typeDocument == null) {
            return null;
        }
        MarketDocument document = typeDocument.get(date);
        if (document == null) {
            return null;
        }
        Map<Integer, MarketPoint> pointMap = document.getPointsMap();
        if (pointMap == null) {
            return null;
        }
        return pointMap.get(intervalIndex);
    }

    @Override
    public MarketPoint getLatestPoint(final DataType type) {
        Map<String, MarketDocument> typeDocument = this.storage.get(type);
        if (typeDocument == null || typeDocument.isEmpty()) {
            return null;
        }
        TreeMap<String, MarketDocument> dayTree =
                (TreeMap<String, MarketDocument>) typeDocument;
        MarketDocument lastDocument = dayTree.lastEntry().getValue();

        Map<Integer, MarketPoint> dayPoints = lastDocument.getPointsMap();

        if (dayPoints == null || dayPoints.isEmpty()) {
            return null;
        }

        TreeMap<Integer, MarketPoint> sortedPoints =
                (TreeMap<Integer, MarketPoint>) dayPoints;

        return sortedPoints.lastEntry().getValue();
    }

    @Override
    public MarketDocument getDocument(final DataType type, final String date) {
        Map<String, MarketDocument> typeDocument = this.storage.get(type);
        if (typeDocument == null || typeDocument.isEmpty()) {
            return null;
        }
        return typeDocument.get(date);
    }

    public Map<String, List<MarketPoint>> getPointsInRange(
            final DataType type, final LocalDateTime fromTime,
            final LocalDateTime toTime) {
        Map<String, List<MarketPoint>> result = new TreeMap<>();
        Map<String, MarketDocument> typeDocument = this.storage.get(type);

        if (typeDocument == null || typeDocument.isEmpty()) {
            return result;
        }

        for (Map.Entry<String, MarketDocument> entry : typeDocument.entrySet()) {
            LocalDate entryDate = LocalDate.parse(entry.getKey());
            if (!entryDate.isBefore(fromTime.toLocalDate())
                    && !entryDate.isAfter(toTime.toLocalDate())) {
                for (MarketPoint p : entry.getValue().getPointsAsList()) {
                    LocalDate day = LocalDate.parse(p.getDate());
                    LocalDateTime start = day.atStartOfDay().plusMinutes(
                            (p.getIntervalIndex() - 1) * MINUTES_PER_INTERVAL);
                    LocalDateTime end = day.atStartOfDay().plusMinutes(
                            p.getIntervalIndex() * MINUTES_PER_INTERVAL);

                    if (!start.isBefore(fromTime) && !end.isAfter(toTime)) {
                        result.computeIfAbsent(
                                entry.getKey(), k -> new ArrayList<>()).add(p);
                    }
                }
            }
        }
        return result;
    }

    public void removePoint(final DataType type, final String date,
                            final int intervalIndex) {
        Map<String, MarketDocument> typeDocument = this.storage.get(type);
        if (typeDocument != null) {
            MarketDocument dayDocument = typeDocument.get(date);
            if (dayDocument != null) {
                dayDocument.removePoint(intervalIndex);
            }
        }
    }
}
