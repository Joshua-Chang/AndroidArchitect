package org.devio.as.hi.hi_concurrent_demo.concurrent;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class ConcurrentTest {
    private static final String TAG = "ConcurrentTest";
    private static final int MSG_WHAT_1 = 1;

    public static void main(String[] args) {
        test();
    }
    public static void test(){
//        testAsyncTask();
//        testHandlerThread();
//        testMainToChild();
        testWaitNotify();
//        testWaitNotify2();
//        testThreadJoin();
//        testThreadSleep();
//        testPriority();
    }



    //wait()：
    // 用法：挂起当前线程来等待一个条件的成立（条件成立时会调notify/all）
    // 原理：当一个线程在执行synchronized的方法内部，一旦调用wait()，
    // 该线程会释放该对象的锁，并把该线程添加到该对象的等待队列中(waiting queue),
    // 等待队列中的线程一直处于阻塞状态，不会被调度执行

    //notify()：
    // 原理： 调度器会从该对象等待队列(waiting queue)的线程中取出任意一个线程，添加到入口队列(entry queue)中。
    // 入口队列中的多个线程竞争对象的锁，得到锁的线程就可以继续执行
    // notifyAll()会将等待队列(waiting queue)中所有的线程都添加到入口队列中(entry queue)


    //wait(), notify(), notifyAll() 和 synchronized 需要搭配使用
    //wait()强迫线程释放锁操作，所以在调wait()时，该线程必须已经获得锁，否则异常。
    //wait()在synchronized的内部被执行，锁一定已经获得，不会异常

    private static void testPriority() {
        //        Thread thread1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//        Thread thread2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
        //原则：对于耗时较长的后台任务 设置优先级低一些。
        // 耗时短但是频繁的任务，可以适当提高优先级。但不能高于主线程优先级。优先级并不能保证一定优先执行

        //线程优先级具有继承性，即若不设置优先级，则继承创建时所在线程的优先级

        //两套优先级互不干涉

        //基于Linux的优先级，对应nice值，[-20,19] -20 优先级高
//        android.os.Process.setThreadPriority(10);//设置调用线程的优先级
//        thread1.setPriority(10);//[1,10] 10优先级高
    }

    private static void testThreadSleep() {
        //适用于线程暂时休眠，让出CPU使用权，也可用于多线程的同步
        final Object object = new Object();
        class Runnable1 implements Runnable {
            @Override
            public void run() {
                synchronized (object) {
                    Log.e(TAG, "run: thread1 start");
                    try {
                        Thread.sleep(1000);
//                        Thread.yield();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "run: thread1 end");
                }

            }
        }


        class Runnable2 implements Runnable {
            @Override
            public void run() {
                synchronized (object) {
                    Log.e(TAG, "run: thread2 start");
                    Log.e(TAG, "run: thread2 end");
                }
            }
        }
        Thread thread1 = new Thread(new Runnable1());
        Thread thread2 = new Thread(new Runnable2());
        thread1.start();
        thread2.start();
    }

    private static void testThreadJoin() {
        Thread threadA=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "runA: 1: " + System.currentTimeMillis());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "runA: 2: " + System.currentTimeMillis());
            }
        });
        Thread threadB=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "runB: 1: " + System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "runB: 2: " + System.currentTimeMillis());
            }
        });
        threadA.start();
        threadB.start();
        try {
            threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "test: 3: " + System.currentTimeMillis());
    }

    private static void testWaitNotify() {
        Object object=new Object();
        //每一个对象都可以成为一个监视器(Monitor),
        //该Monitor由一个锁(lock), 一个等待队列(waiting queue) , 一个入口队列(entry queue)组成
        //等待池中的线程不能获取锁，而是需要被唤醒进入锁池，才有获取到锁的机会
        class Runnable1 implements Runnable{
            @Override
            public void run() {
                Log.e(TAG, "run: thread1 start");
                synchronized (object){//包裹的代码：持有资源对象的monitor才能执行。否则进入blocked状态
                    try {
                        object.wait();//进入waiting状态，释放monitor
                        //waiting状态：
                        //object.wait()/otherThread.join() ｜ object.wait(long)/otherThread.join(long)
                        //到时恢复/otherThread结束恢复running状态
                        //thread.sleep(long)到时恢复running状态 释放资源对象的monitor
                        //thread.yield 暂停，但不释放资源对象的monitor
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.e(TAG, "run: thread1 end");
            }
        }
        class Runnable2 implements Runnable{
            @Override
            public void run() {
                Log.e(TAG, "run: thread2 start");
                synchronized (object){
                    object.notify();//唤醒此资源对象monitor的上等待的线程
                }
                Log.e(TAG, "run: thread2 end");
            }
        }

        Thread thread1 = new Thread(new Runnable1());
        Thread thread2 = new Thread(new Runnable2());
        thread1.start();
        thread2.start();
    }

    static volatile boolean hasNotify=false;
    private static void testWaitNotify2() {
        Object object=new Object();
        class Runnable1 implements Runnable{
            @Override
            public void run() {
                Log.e(TAG, "run: thread1 start");
                synchronized (object){
                    try {
                        if (!hasNotify){
                            object.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.e(TAG, "run: thread1 end");
            }
        }
        class Runnable2 implements Runnable{
            @Override
            public void run() {
                synchronized (object){
                    Log.e(TAG, "run: thread2 start");
                    object.notify();
                    hasNotify=true;
                    Log.e(TAG, "run: thread2 end");
                }
            }
        }

        Thread thread1 = new Thread(new Runnable1());
        Thread thread2 = new Thread(new Runnable2());
        thread1.start();
        thread2.start();
    }

    private static void testMainToChild() {
        class LooperThread extends Thread{
            private Looper looper;

            public LooperThread(String name) {
                super(name);
            }

            public Looper getLooper() {
                synchronized (this){
                    if (looper==null&&isAlive()) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return looper;
            }

            @Override
            public void run() {
                Looper.prepare();
                synchronized (this){
                    looper=Looper.myLooper();
                    notify();
                }
                Looper.loop();
            }
        }

        LooperThread thread=new LooperThread("child-looper");
        thread.start();

        Handler handler = new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.e(TAG, "handleMessage: " + msg.what);
                Log.e(TAG, "handleMessage: " + Thread.currentThread().getName());
            }
        };
        handler.sendEmptyMessage(MSG_WHAT_1);
    }

    private static void testHandlerThread() {
        //1、先开启handlerThread
        // 适用于主线程需要和子线程通信的场景，
        // 应用于持续性任务，比如轮训，
        HandlerThread handlerThread=new HandlerThread("handler-thread");
        handlerThread.start();
        MyHandler myHandler=new MyHandler(handlerThread.getLooper());
        myHandler.sendEmptyMessage(MSG_WHAT_1);//主线程向子线程handler-thread发送消息

        //只能quit
//        handlerThread.quit();
//        handlerThread.quitSafely();
    }

    private static void testAsyncTask() {
        MyAsyncTask asyncTask = new MyAsyncTask();
        asyncTask.execute("execute mytask");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: AsyncTask execute");
            }
        });
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: THREAD_POOL_EXECUTOR AsyncTask execute");
            }
        });
    }

    static class MyAsyncTask extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 10; i++) {
                publishProgress(i*10);
            }
            return params[0];
        }

        @Override
        protected void onProgressUpdate(Integer... values) {//主线程回调
            Log.e(TAG, "onProgressUpdate: " + values[0]);
        }

        @Override
        protected void onPostExecute(String result) {//执行结果
            Log.e(TAG, "onPostExecute: " + result);
        }
    }

    static class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.e(TAG, "handleMessage: " + msg.what);
            Log.e(TAG, "handleMessage: " + Thread.currentThread().getName());
        }

        public MyHandler(@NonNull Looper looper) {
            super(looper);
        }
    }



}
