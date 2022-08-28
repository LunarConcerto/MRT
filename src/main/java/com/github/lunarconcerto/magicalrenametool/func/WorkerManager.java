package com.github.lunarconcerto.magicalrenametool.func;

import javafx.concurrent.Task;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;

public class WorkerManager {

    public void startWork(Runnable runnable) {
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
    }

    public <T> T startCallbackWork(@NotNull Callable<T> callback) throws Exception {
        return callback.call() ;
    }

}
