package com.piotrwalkusz.timemarker;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

class JustOneAppLock {

    private static final String LOCK_PATH = System.getProperty("user.home") + "/.timemarker/.lock";

    private FileLock lock;

    boolean tryLock() {
        File file = new File(LOCK_PATH);
        try {
            FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
            lock = channel.tryLock();
            return lock != null;
        }
        catch (Exception ex) {
            return false;
        }
    }

    void unlock() {
        if (lock == null)
            throw new RuntimeException("File hasn't been locked");

        try {
            lock.close();
            lock = null;
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
