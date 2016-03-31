package com.youku.opensdk.impl.first;

import android.content.Context;

import com.youku.opensdk.YoukuAPIFactory;
import com.youku.opensdk.YoukuOpenAPI;

/**
 * Created by smy on 2016/3/31.
 */
public class ApiFirstFactory extends YoukuAPIFactory {

    @Override
    protected YoukuOpenAPI createApiInstance(Context context, String appKey) {
        return new ApiFirstVersion(context, appKey);
    }
}
