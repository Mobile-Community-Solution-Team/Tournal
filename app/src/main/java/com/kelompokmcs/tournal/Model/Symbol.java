package com.kelompokmcs.tournal.Model;

public class Symbol {
    private int id, groupid, symbolId;
    private double lat, lng,alt;

    public Symbol(int id, int groupid, double lat, double lng, double alt, int symbolId) {
        this.id = id;
        this.groupid = groupid;
        this.symbolId = symbolId;
        this.lat = lat;
        this.lng = lng;
        this.alt = alt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public int getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(int symbolId) {
        this.symbolId = symbolId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }
}
