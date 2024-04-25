package org.example.threadPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ThreadPool {
    private final int numThreads;
    private final Thread[] threads;
    private final BlockingQueue<FutureTask> taskQueue;
    private volatile boolean isRunning = true;

    private static final Logger log = LoggerFactory.getLogger(ThreadPool.class);

    public ThreadPool(int numThreads) {
        this.numThreads = numThreads;
        taskQueue = new LinkedBlockingQueue<>();
        threads = new Thread[numThreads];


        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted() && (isRunning || !taskQueue.isEmpty())) {
                    try {
                        FutureTask task = taskQueue.poll(1, TimeUnit.SECONDS); // 使用poll()避免永久阻塞
                        if (task != null) {
                            task.run();
                        }
                    } catch (InterruptedException e) {
                        // 捕获中断异常
                        Thread.currentThread().interrupt();
                    }
                }
            });
            threads[i].setDaemon(true);
            threads[i].start();
        }
    }

    public <T> Future<T> submit(Callable<T> task) {
        if (!isRunning) {
            throw new IllegalStateException("ThreadPool has been shutdown.");
        }
        FutureTask<T> futureTask = new FutureTask<>(task);
        taskQueue.add(futureTask);
        return futureTask;
    }

    public void shutdown() {
        isRunning = false;
        for (Thread thread : threads) {
            thread.interrupt();
        }
        for (Thread thread : threads) {
            try {
                thread.join(); // 等待线程结束
            } catch (InterruptedException e) {
                // 忽略异常
            }
        }
    }
    public void shutdownNow() {
        isRunning = false;
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}