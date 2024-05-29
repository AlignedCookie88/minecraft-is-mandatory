package com.alignedcookie88.minecraft_is_mandatory;

public class TimeFormatter {

    int time;

    private TimeFormatter(int seconds) {
        time = seconds;
    }

    public static TimeFormatter fromSeconds(int seconds) {
        return new TimeFormatter(seconds);
    }

    public static TimeFormatter fromTicks(int ticks) {
        return new TimeFormatter(ticks / 20);
    }


    public int getSeconds(int exclude) {
        return time - exclude;
    }

    public int excludeSeconds(int seconds) {
        return seconds;
    }

    public int getMinutes(int exclude) {
        return (time - exclude) / 60;
    }

    public int excludeMinutes(int minutes) {
        return minutes * 60;
    }

    public int getHours(int exclude) {
        return (time - exclude) / 3600;
    }

    public int excludeHours(int hours) {
        return hours * 3600;
    }

    public int getDays(int exclude) {
        return (time - exclude) / 86400;
    }

    public int excludeDays(int days) {
        return days * 86400;
    }

    public String formatStandard() {
        int days = getDays(0);
        int hours = getHours(excludeDays(days));
        int minutes = getMinutes(excludeHours(hours));
        int seconds = getSeconds(excludeMinutes(minutes));
        return String.format("%01d:%02d:%02d:%02d", days, hours, minutes, seconds);
    }

}
