package org.devio.as.hi.hi_concurrent_demo.concurrent;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo2 {

    static class ReentrantLockTask {
        //公平锁，即进入阻塞的线程依次排队去获取锁，均有机会执行
        //默认是非公平锁 后加入的线程，尝试插队一次去获取锁，若获取到直接执行，不用进入阻塞。
        // 允许线程插队，避免每个线程都阻塞再唤醒的资源消耗。但也有可能线程饿死（即一直得不到锁无法执行）
        ReentrantLock lock = new ReentrantLock(true);
//        ReentrantLock lock = new ReentrantLock();//默认非公平锁
//        当线程刚释放锁，再去申请锁，会优先获得锁（插队一次去获取锁，不用进入阻塞）
        void print(){
            String name = Thread.currentThread().getName();
            try {
                lock.lock();
                //打印两次
                System.out.println(name + "第一次打印");
                Thread.sleep(1000);
                lock.unlock();

                lock.lock();
                System.out.println(name + "第二次打印");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        final ReentrantLockTask task=new ReentrantLockTask();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                task.print();
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
    }
}
