package com.youku.opensdk;

import android.content.Context;

import com.youku.opensdk.impl.first.ApiFirstFactory;
import com.youku.opensdk.util.Constants;
import com.youku.opensdk.util.Logger;

/**
 * Created by smy on 2016/3/30.
 */
public abstract class YoukuAPIFactory {

    private static YoukuAPIFactory sYoukuApiFactory;
    private static YoukuOpenAPI sYoukuOpenAPI;

    protected YoukuAPIFactory() {

    }

    public static YoukuOpenAPI createYoukuApi(Context context) {
        if (null == context) {
            throw new NullPointerException("createYoukuApi context is null !!!");
        }
        if (null != sYoukuOpenAPI) {
            return sYoukuOpenAPI;
        }
        if (null == sYoukuApiFactory) {
            sYoukuApiFactory = createYoukuApiFactory();
        }
        sYoukuOpenAPI = sYoukuApiFactory.createApiInstance(context.getApplicationContext());
        return sYoukuOpenAPI;
    }

    private static YoukuAPIFactory createYoukuApiFactory() {
        if (null == sYoukuApiFactory) {
            switch (Constants.OPEN_SDK_VERSION) {
                case 1:
                    return new ApiFirstFactory();
            }
        }
        return sYoukuApiFactory;
    }

    protected abstract YoukuOpenAPI createApiInstance(Context context);
}
