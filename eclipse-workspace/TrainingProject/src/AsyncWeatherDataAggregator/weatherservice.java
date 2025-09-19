package AsyncWeatherDataAggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class weatherservice {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private static final Random random = new Random();

    public weatherreport fetchWeatherData(String serviceName) throws Exception {
        int delay = 500 + random.nextInt(1500); // 0.5s to 2s delay
        Thread.sleep(delay);

        if (random.nextDouble() < 0.3) { // Simulate 30% chance of failure
            throw new Exception(serviceName + " failed due to network error.");
        }

        // Generate random weather data
        double temperature = -10 + random.nextDouble() * 40; // -10°C to 30°C
        double humidity = 10 + random.nextDouble() * 80;    // 10% to 90%
        double windSpeed = random.nextDouble() * 100;       // 0 to 100 km/h

        logger.info(serviceName + " fetched data successfully.");
        return new weatherreport(temperature, humidity, windSpeed);
    }
}
