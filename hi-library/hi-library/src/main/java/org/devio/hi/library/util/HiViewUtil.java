package org.devio.hi.library.util;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayDeque;

public class HiViewUtil{
    public static <T> T findTypeView(@Nullable ViewGroup group, Class<T>cls){
        if (group==null) {
            return null;
        }
        ArrayDeque<View> deque = new ArrayDeque<>();//双端队列
        deque.add(group);
        while (!deque.isEmpty()) {
            View node = deque.removeFirst();
            if (cls.isInstance(node)) {
                return cls.cast(node);
            }else if (node instanceof ViewGroup){
                ViewGroup container = (ViewGroup) node;
                int count = container.getChildCount();
                for (int i = 0; i < count; i++) {
                    deque.add(container.getChildAt(i));
                }
            }
        }
        return null;
    }
}
