package com.londonappbrewery.bitcointicker;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class BitcoinDataModel {
    String mPrice;

    public static BitcoinDataModel fromJson(JSONObject jsonObject){
        try {
            BitcoinDataModel bitcoinData = new BitcoinDataModel();
            bitcoinData.mPrice = jsonObject.getString("last");
            return bitcoinData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
