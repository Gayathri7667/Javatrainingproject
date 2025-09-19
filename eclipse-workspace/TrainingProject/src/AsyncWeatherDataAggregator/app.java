package AsyncWeatherDataAggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class app {

    private static final Logger logger = LoggerFactory.getLogger(app.class);

    public static void main(String[] args) {
        weatheraggregator aggregator = new weatheraggregator();
        try {
            weatherreport report = aggregator.aggregateWeatherData();
            logger.info("Final Weather Report: " + report);
            System.out.println("Final Weather Report: " + report);
        } catch (weatherdataunavailableexception e) {
            logger.error("Failed to retrieve any weather data: " + e.getMessage());
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
