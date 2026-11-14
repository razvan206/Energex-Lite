package util;

import domain.DataType;
import domain.MarketPoint;
import repository.MarketRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SimulationClock {
    // we will use a pending points simulation so we cannot read date from the future time
    private record PointKey(DataType type, String date, int intervalIndex) {}
    private final Map<PointKey, MarketPoint> pendingPoints;
    private LocalDateTime currentTime;
    private boolean importActive = false;

    public SimulationClock(){
        this.currentTime = null;
        this.importActive = false;
        this.pendingPoints = new HashMap<>();
    }

    public void addPendingPoint(DataType type, String date, int intervalIndex, MarketPoint point){
        this.pendingPoints.put(new PointKey(type, date, intervalIndex), point);
    }

    public void startImport(String timestamp){
        this.currentTime = LocalDateTime.parse(timestamp);
        this.importActive = true;
    }
    public void stopImport(){
        this.importActive = false;
    }

    public LocalDateTime getCurrentTime(){
        return currentTime;
    }

    public boolean isImportActive(){
        return importActive;
    }
    // we use our structure like a queue for future points
    public void timeSimulation(LocalDateTime targetTime, MarketRepository repository){
        if(this.currentTime == null) {
            this.currentTime = targetTime;
            return ;
        }
        while(this.currentTime.isBefore(targetTime)){
            this.currentTime = this.currentTime.plusMinutes(1);

            if(this.isImportActive() && this.currentTime.getMinute() % 15 == 0){
                LocalDateTime intervalStart = currentTime.minusMinutes(15);
                String date = intervalStart.toLocalDate().toString();
                int intervalIndex = TimeConversionUtils.timestampToInterval(intervalStart.toString());
                for(DataType type : DataType.values()){
                    MarketPoint point = pendingPoints.remove(new PointKey(type, date, intervalIndex));
                    if(point != null){
                        repository.savePoint(type, date, point);
                    }
                }
            }
        }
    }


}
