package AsyncWeatherDataAggregator;

public class weatherreport {
    private double temperature;
    private double humidity;
    private double windSpeed;

    public weatherreport(double temperature, double humidity, double windSpeed) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    @Override
    public String toString() {
        return String.format("Temperature=%.2f°C, Humidity=%.2f%%, WindSpeed=%.2f km/h",
                temperature, humidity, windSpeed);
    }
}
