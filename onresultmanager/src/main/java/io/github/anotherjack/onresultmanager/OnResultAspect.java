package io.github.anotherjack.onresultmanager;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by zhengj on 2018/1/3.
 */

//注意！！！这个文件是我倒腾aspectj的时候搞的，无视就好

//@Aspect
public class OnResultAspect {
    private static final String TAG = "OnResultAspect";

    @After("execution(* android.app.Activity.onActivityResult(..))")
    public void onActivityResultAfter(JoinPoint joinPoint) throws Throwable {
        Log.d(TAG,"-------------onActivityResult hooked");
        Object[] args = joinPoint.getArgs();
        int requestCode = (int) args[0];
        int resultCode = (int) args[1];
        Intent data = (Intent) args[2];
        Activity activity = (Activity) joinPoint.getTarget(); //这里我也试了使用joinPoint.getThis()，发现也能触发，
                                                                // 打断点发现两者好像是同一个对象，这里还不太清楚

        new OnResultManager(activity).trigger(requestCode,resultCode,data);

    }
}
