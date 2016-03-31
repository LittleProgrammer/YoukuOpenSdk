package com.youku.opensdk;

import android.content.Context;

import com.youku.opensdk.impl.first.ApiFirstFactory;
import com.youku.opensdk.util.Constants;

/**
 * Created by smy on 2016/3/30.
 */
public abstract class YoukuAPIFactory {

    private static YoukuAPIFactory sYoukuApiFactory;

    protected YoukuAPIFactory() {

    }

    public static YoukuOpenAPI createYoukuApi(Context context, String appKey) {
        if (null == sYoukuApiFactory) {
            sYoukuApiFactory = createYoukuApiFactory();
        }
        return sYoukuApiFactory.createApiInstance(context, appKey);
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

    protected abstract YoukuOpenAPI createApiInstance(Context context, String appKey);
}
