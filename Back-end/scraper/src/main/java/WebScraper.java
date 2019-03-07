public class WebScraper extends Thread{

    public int crawlDelay = 1;
    
    //Allows us to shut down our application cleanly
    volatile public boolean runThread = false;
    
    //Create an instance of the CarDao class
    CarDao carDao;
   
    //Getters and setters
    public int getCrawlDelay() {
        return crawlDelay;
    }

    public void setCrawlDelay(int crawlDelay) {
        this.crawlDelay = crawlDelay;
    }

    public boolean isRunThread() {
        return runThread;
    }

    public void setRunThread(boolean runThread) {
        this.runThread = runThread;
    }

    public CarDao getCarDao() {
        return carDao;
    }

    public void setCarDao(CarDao carDao) {
        this.carDao = carDao;
    }
    
    //Other classes can use this method to terminate the thread.
    public void stopThread(){
        runThread = false;
    }
}
