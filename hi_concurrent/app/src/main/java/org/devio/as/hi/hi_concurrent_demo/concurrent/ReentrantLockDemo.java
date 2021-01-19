package org.devio.as.hi.hi_concurrent_demo.concurrent;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    static class ReentrantLockTask {
        //公平锁，即进入阻塞的线程依次排队去获取锁，均有机会执行
        //默认是非公平锁 后加入的线程，尝试插队一次去获取锁，若获取到直接执行，不用再进入阻塞。
        // 允许线程插队，避免每个线程都阻塞再唤醒的资源消耗。但也有可能线程饿死（即一直得不到锁无法执行）
        void buyTicket(){
            String name = Thread.currentThread().getName();
            try {
                reentrantLock.lock();
                System.out.println(name + ":准备好了");
                Thread.sleep(100);
                System.out.println(name + ":买好了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }
        ReentrantLock reentrantLock = new ReentrantLock();
    }

    public static void main(String[] args) {
        final ReentrantLockTask task=new ReentrantLockTask();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                task.buyTicket();
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }

    }
}
