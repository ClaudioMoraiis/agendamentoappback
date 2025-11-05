package com.example.demo.Util;

import java.util.HashMap;
import java.util.Map;

public class ApiResponseUtil {
    public static HashMap response (String mStatus, String mMensagem){
        Map<String, String> mResponse = new HashMap<>();
        mResponse.put(mStatus, mMensagem);
        return new HashMap<>(mResponse);
    }
}
