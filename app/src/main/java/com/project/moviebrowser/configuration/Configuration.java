package com.project.moviebrowser.configuration;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Configuration {
    static Configuration configuration;
    private int certificationLevel = 0;


    private String certificationRegion = "US";

    private int nextPage = 0;
    private int nextOffset = 0;

    public static Configuration getInstance(){
        if(configuration == null) {
            configuration = new Configuration();
        }

        return configuration;
    }

    public int getCertificationLevel() {
        return certificationLevel;
    }


    public String getCertificationRegion() {
        return certificationRegion;
    }

    public void setCertificationLevel(int certificationLevel) {
        this.certificationLevel = certificationLevel;
    }

    public void resetNextPage(){
        this.nextPage = 1;
        this.nextOffset = 0;
    }
    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getNextOffset() {
        return nextOffset;
    }

    public void setNextOffset(int nextOffset) {
        this.nextOffset = nextOffset;
    }

}
