package AsyncWeatherDataAggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class weatheraggregator {

    private static final Logger logger = LoggerFactory.getLogger(weatheraggregator.class);

    public weatherreport aggregateWeatherData() throws weatherdataunavailableexception {
        weatherservice service = new weatherservice();

        List<CompletableFuture<weatherreport>> futures = new ArrayList<>();
        String[] serviceNames = {"WeatherAPI1", "WeatherAPI2", "WeatherAPI3"};

        for (String serviceName : serviceNames) {
            CompletableFuture<weatherreport> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return service.fetchWeatherData(serviceName);
                } catch (Exception e) {
                    logger.error(serviceName + " failed: " + e.getMessage());
                    return null;
                }
            });
            futures.add(future);
        }

        // Wait for all futures to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        List<weatherreport> successfulReports = new ArrayList<>();
        for (CompletableFuture<weatherreport> future : futures) {
            weatherreport report = future.join();
            if (report != null) {
                successfulReports.add(report);
            }
        }

        if (successfulReports.isEmpty()) {
            throw new weatherdataunavailableexception("All weather services failed.");
        } else if (successfulReports.size() < serviceNames.length) {
            logger.warn("Partial data received. Aggregating available data.");
        } else {
            logger.info("All services succeeded. Aggregating data.");
        }

        // Compute averages
        double avgTemp = successfulReports.stream().mapToDouble(WeatherReport::getTemperature).average().orElse(0);
        double avgHumidity = successfulReports.stream().mapToDouble(WeatherReport::getHumidity).average().orElse(0);
        double avgWindSpeed = successfulReports.stream().mapToDouble(WeatherReport::getWindSpeed).average().orElse(0);

        return new weatherreport(avgTemp, avgHumidity, avgWindSpeed);
    }
}
