package repository;

import domain.DataType;
import domain.MarketDocument;
import domain.MarketPoint;

public interface MarketRepository {
    void savePoint(DataType type, String date, MarketPoint point);

    Double getPreviousGridLoad(String date, int currentIndex);

    MarketPoint getPoint(DataType type, String date, int intervalIndex);

    MarketPoint getLatestPoint(DataType type);

    MarketDocument getDocument(DataType type, String date);
}

