package com.example.leroy.world;

/**
 * Created by leroy on 9/26/17.
 */

public class CountryInformation {
    private String mTopLevelDomain;
    private String mAlpha2Code;private String mCallingCodes;
    private String mCapital;
    private String mRegion;
    private String mSubregion;
    private String mPopulation;
    private String mDemonym;
    private String mArea;
    private String mGini;
    private String mNativeName;
    private String mNumericCode;

    public CountryInformation(String topLevelDomain,
                              String alpha2Code,
                              String callingCodes,
                              String capital,
                              String region,
                              String subregion,
                              String population,
                              String demonym,
                              String area,
                              String gini,
                              String nativeName,
                              String numericCode){
        mTopLevelDomain = topLevelDomain;
        mAlpha2Code = alpha2Code;
        mCallingCodes = callingCodes;
        mCapital = capital;
        mRegion = region;
        mSubregion = subregion;
        mPopulation = population;
        mDemonym = demonym;
        mArea = area;
        mGini = gini;
        mNativeName = nativeName;
        mNumericCode = numericCode;
    }

    public String getTopLevelDomain() {
        return mTopLevelDomain;
    }

    public String getAlpha2Code() {
        return mAlpha2Code.toLowerCase();
    }

    public String getCallingCodes() {
        return mCallingCodes;
    }

    public String getCapital() {
        return mCapital;
    }

    public String getRegion() {
        return mRegion;
    }

    public String getSubregion() {
        return mSubregion;
    }

    public String getPopulation() {
        return mPopulation;
    }

    public String getDemonym() {
        return mDemonym;
    }

    public String getArea() {
        return mArea;
    }

    public String getGini() {
        return mGini;
    }

    public String getNativeName() {
        return mNativeName;
    }

    public String getNumericCode() {
        return mNumericCode;
    }
}
