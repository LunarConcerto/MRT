package com.github.lunarconcerto.mrt.component;

import javafx.concurrent.Task;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class Worker {

    public static Task<Void> startWork(Runnable runnable) {
        Task<Void> backgroundTask = new Task<>() {
            @Contract(pure = true)
            @Override
            protected @Nullable Void call() {
                return null;
            }

            @Override
            public void run() {
                runnable.run();
            }
        };

        Thread backgroundThread = new Thread(backgroundTask);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
        return backgroundTask ;
    }

}
