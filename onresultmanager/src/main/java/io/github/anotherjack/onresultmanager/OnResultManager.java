package io.github.anotherjack.onresultmanager;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Created by jack on 2017/12/29.
 */

public class OnResultManager {
    private static final String TAG = "OnResultManager";
    //HashMap的key Integer为requestCode
    private static WeakHashMap<Activity,HashMap<Integer,Callback>> mCallbacks = new WeakHashMap<>();
    private WeakReference<Activity> mActivity;

    public OnResultManager(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
    }

    public void startForResult(Intent intent, int requestCode, Callback callback){
        Activity activity = getActivity();
        if(activity == null){
            return;
        }

        addCallback(activity,requestCode,callback);
        activity.startActivityForResult(intent,requestCode);
    }

    public void trigger(int requestCode, int resultCode, Intent data){
        Log.d(TAG,"----------- trigger");
        Activity activity = getActivity();
        if(activity == null){
            return;
        }

        Callback callback = findCallback(activity,requestCode);
        if(callback != null){
            callback.onActivityResult(requestCode,resultCode,data);
        }
    }

    //获取该activity、该requestCode对应的callback
    private Callback findCallback(Activity activity,int requestCode){
        HashMap<Integer,Callback> map = mCallbacks.get(activity);
        if(map != null){
            return map.remove(requestCode);
        }
        return null;
    }

    private void addCallback(Activity activity,int requestCode,Callback callback){
        HashMap<Integer,Callback> map = mCallbacks.get(activity);
        if(map == null){
            map = new HashMap<>();
            mCallbacks.put(activity,map);
        }
        map.put(requestCode,callback);
    }

    private Activity getActivity(){
        return mActivity.get();
    }

    public interface Callback{
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
