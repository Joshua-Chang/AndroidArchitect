package org.devio.as.hi.hi_concurrent_demo.coroutine;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlinx.coroutines.DelayKt;

public class CoroutineScene2_decompiled {
    private static final String TAG = "CoroutineScene2";
    public static final Object request2(Continuation preCallback){
        ContinuationImpl request2CallBack;
        if (!(preCallback instanceof ContinuationImpl)||(((ContinuationImpl) preCallback).label&Integer.MIN_VALUE)==0){
            request2CallBack=new ContinuationImpl(preCallback) {
                @Override
                Object invokeSuspend(@NonNull Object resumeResult) {
                    this.result=resumeResult;
                    this.label|=Integer.MIN_VALUE;
                    return request2(this);
                }
            };
        }else {
            request2CallBack= (ContinuationImpl) preCallback;
        }
        switch (request2CallBack.label) {
            case 0:{
                Object delay = DelayKt.delay(2000, request2CallBack);
                if (delay== IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    return IntrinsicsKt.getCOROUTINE_SUSPENDED();
                }
            }
        }
        Log.e(TAG, "request1 completed");

        return "result from request2";
    }

    static abstract class ContinuationImpl<T>implements Continuation<T>{
        private Continuation preCallback;
        int label;
        Object result;

        public ContinuationImpl(Continuation preCallback) {
            this.preCallback = preCallback;
        }

        @NotNull
        @Override
        public CoroutineContext getContext() {
            return preCallback.getContext();
        }

        @Override
        public void resumeWith(@NotNull Object resumeResult) {
            Object suspend = invokeSuspend(resumeResult);
            if (suspend== IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return;
            }
            preCallback.resumeWith(suspend);
        }
        abstract Object invokeSuspend(@NonNull Object resumeResult);
    }
}
