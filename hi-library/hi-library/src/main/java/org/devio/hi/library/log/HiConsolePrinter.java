package org.devio.hi.library.log;

import android.util.Log;

import androidx.annotation.NonNull;

public class HiConsolePrinter implements HiLogPrinter{
    @Override
    public void print(@NonNull HiLogConfig config, int level, String tag, @NonNull String printString) {
        int len = printString.length();
        int countOfSub = len / HiLogConfig.MAX_LEN;
        if (countOfSub>0) {
            int index=0;
            for (int i = 0; i < countOfSub; i++) {
                Log.println(level,tag,printString.substring(index,index+ HiLogConfig.MAX_LEN));
                index+= HiLogConfig.MAX_LEN;
            }
            if (index!=len){
                Log.println(level,tag,printString.substring(index,len));
            }
        }else {
            Log.println(level,tag,printString);
        }
    }
}
