package com.example.snatch;

public class Record implements Comparable<Record>{
    private String name;
    private int score;
    private double latitude;
    private double longitude;

    public Record(String name, int score, double latitude, double longitude) {
        this.name = name;
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int compareTo(Record rec) {
        return rec.score - this.score;
    }

    @Override
    public String toString() {
        return String.format("Name: %-20s", name) + String.format("Score: %-20s", score);
    }


}
