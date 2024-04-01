package org.example.threadPool;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ThreadPool {
    private final int numThreads;
    private final WorkerThread[] threads;
    private final Queue<FutureTask> taskQueue;

    public ThreadPool(int numThreads) {
        this.numThreads = numThreads;
        taskQueue = new LinkedList<>();
        threads = new WorkerThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new WorkerThread();
            threads[i].start();
        }
    }

    public <T> Future<T> submit(Callable<T> task) {
        FutureTask<T> futureTask = new FutureTask<>(task);
        synchronized (taskQueue) {
            taskQueue.add(futureTask);
            taskQueue.notify();
        }
        return futureTask;
    }

    private class WorkerThread extends Thread {
        public void run() {
            FutureTask task;

            while (true) {
                synchronized (taskQueue) {
                    while (taskQueue.isEmpty()) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            System.out.println("An error occurred while queue is waiting: " + e.getMessage());
                        }
                    }
                    task = taskQueue.poll();
                }

                try {
                    task.run();
                } catch (RuntimeException e) {
                    System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
                }
            }
        }
    }
}