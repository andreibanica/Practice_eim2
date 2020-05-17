package ro.pub.cs.systems.eim.practice_eim2;

public class Info {

    public String temperature;
    public String windSpeed;
    public String condition;
    public String pressure;
    public String humidity;

    @Override
    public String toString() {
        return "Info{" +
                "temperature='" + temperature + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", condition='" + condition + '\'' +
                ", pressure='" + pressure + '\'' +
                ", humidity='" + humidity + '\'' +
                '}';
    }
}
