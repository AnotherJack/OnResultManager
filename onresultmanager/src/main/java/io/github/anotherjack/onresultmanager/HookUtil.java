package io.github.anotherjack.onresultmanager;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class HookUtil {
    public static void hookActivityThreadHandler() throws Exception {
        // 先获取到当前的ActivityThread对象
        final Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field currentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
        currentActivityThreadField.setAccessible(true);
        final Object currentActivityThread = currentActivityThreadField.get(null);

        // 由于ActivityThread一个进程只有一个,我们获取这个对象的mH
        Field mHField = activityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        Handler mH = (Handler) mHField.get(currentActivityThread);

        Handler.Callback mHCallback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == 108){
                    Log.d("hook-------","onActivityResult");

                    try{
                        Object resultData = msg.obj;

                        Field mActivitiesField = activityThreadClass.getDeclaredField("mActivities");
                        mActivitiesField.setAccessible(true);
                        ArrayMap mActivities = (ArrayMap) mActivitiesField.get(currentActivityThread);

                        Class<?> resultDataClass = Class.forName("android.app.ActivityThread$ResultData");
                        Field tokenField = resultDataClass.getDeclaredField("token");
                        tokenField.setAccessible(true);
                        IBinder token = (IBinder) tokenField.get(resultData);

                        //r是ActivityClientRecord类型的
                        Object r = mActivities.get(token);
                        Class<?> ActivityClientRecordClass = Class.forName("android.app.ActivityThread$ActivityClientRecord");
                        Field activityField = ActivityClientRecordClass.getDeclaredField("activity");
                        activityField.setAccessible(true);
                        Activity activity = (Activity) activityField.get(r); //至此，终于拿到activity了

                        Field resultsField = resultDataClass.getDeclaredField("results");
                        resultsField.setAccessible(true);
                        List results = (List) resultsField.get(resultData);

                        //ResultInfo类型
                        Object resultInfo = results.get(0);

                        Class<?> resultInfoClass = Class.forName("android.app.ResultInfo");
                        Field mRequestCodeField = resultInfoClass.getDeclaredField("mRequestCode");
                        mRequestCodeField.setAccessible(true);
                        int mRequestCode = (int) mRequestCodeField.get(resultInfo); //拿到requestCode

                        Field mResultCodeField = resultInfoClass.getDeclaredField("mResultCode");
                        mResultCodeField.setAccessible(true);
                        int mResultCode = (int) mResultCodeField.get(resultInfo); //拿到resultCode

                        Field mDataField = resultInfoClass.getDeclaredField("mData");
                        mDataField.setAccessible(true);
                        Intent mData = (Intent) mDataField.get(resultInfo); //拿到intent

                        new OnResultManager(activity).trigger(mRequestCode,mResultCode,mData);



                    }catch (Exception e){
                        e.printStackTrace();
                    }




                }

                return false;
            }
        };
        Field mCallBackField = Handler.class.getDeclaredField("mCallback");
        mCallBackField.setAccessible(true);

        mCallBackField.set(mH, mHCallback);

    }

}