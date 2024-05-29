package com.project.moviebrowser.networking;

import com.project.moviebrowser.R;
import com.project.moviebrowser.configuration.Configuration;

public class Certification {
    public static String[] certificationList = new String[]{"NR", "PG", "PG-13", "R", "NC-17"};

    public static String getCertification(int certificationLevel){
        return certificationList[certificationLevel];
    }
    public static int getDrawableId(String certification){
        if(certification == null) return -1;
        switch (certification){
            case "PG": return R.mipmap.ic_rating_g;
            case "PG-13": return R.mipmap.ic_rating_p;
            case "R": return R.mipmap.ic_rating_r;
            case "NC-17": return R.mipmap.ic_rating_x;
            default: return -1;
        }
    }
}
