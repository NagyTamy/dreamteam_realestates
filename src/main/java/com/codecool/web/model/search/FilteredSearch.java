package com.codecool.web.model.search;

import com.codecool.web.model.AbstractModel;

import java.time.LocalDateTime;

public class FilteredSearch extends Search {

    private String realEstateName;
    private String country;
    private String city;
    private int bedCount;
    private int priceMin;
    private int priceMax;
    private String[] listOfExtras;
    private boolean isSaved = false;
    private String extras;


    public FilteredSearch(int id, LocalDateTime date, String realEstateName, String country, String city, int bedCount, int priceMax, int priceMin, String extras) {
        super(id, date);
        if(!realEstateName.isEmpty()) {
            this.realEstateName = realEstateName;
        } else {
            this.realEstateName = null;
        }
        if(!country.isEmpty()) {
            this.country = country;
        } else {
            this.country = null;
        }
        if(!city.isEmpty()) {
            this.city = city;
        } else {
            this.city = null;
        }
        if(!extras.isEmpty()) {
            this.extras = extras;
            extras = extras.replaceAll(",\\s", ",");
            listOfExtras = extras.split(",");
        } else {
            listOfExtras = null;
        }
        this.bedCount = bedCount;
        this.priceMax = priceMax;
        this.priceMin = priceMin;
    }

    public String getExtras() {
        return extras;
    }

    public String[] getListOfExtras() {
        return listOfExtras;
    }

    public String getRealEstateName() {
        return realEstateName;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public int getBedCount() {
        return bedCount;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public int getPriceMin() {
        return priceMin;
    }

    public boolean getIsSaved(){
        return isSaved;
    }

    public void setIsSaved(boolean isSaved){
        this.isSaved = isSaved;
    }
}
