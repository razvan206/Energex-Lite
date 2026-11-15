package util;


import repository.MarketRepository;

import java.time.LocalDateTime;


public class SimulationClock {
    private LocalDateTime currentTime;
    private boolean importActive = false;

    public SimulationClock(){
        this.currentTime = null;
        this.importActive = false;
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

    public void timeSimulation(LocalDateTime targetTime, MarketRepository repository){
        if(this.currentTime == null) {
            this.currentTime = targetTime;
            return ;
        }
        while(this.currentTime.isBefore(targetTime)){
            this.currentTime = this.currentTime.plusMinutes(1);
        }
    }


}
