/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cm.android.thread;

import android.os.Process;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A thread factory that creates threads with a given thread priority.
 */
public class PriorityThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    private final int mPriority;

    public PriorityThreadFactory(String name, int processPriority) {
        mPriority = processPriority;

        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();

        namePrefix = new StringBuilder().append(name).append(":pool-").append(poolNumber.getAndIncrement()).append("-thread-").toString();
//        namePrefix = name + ":pool-" + poolNumber.getAndIncrement() + "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0) {
            @Override
            public void run() {
                Process.setThreadPriority(mPriority);
                super.run();
            }
        };
//        if (t.isDaemon()) {
//            t.setDaemon(false);
//        }

//        if (t.getPriority() != Thread.NORM_PRIORITY) {
//            t.setPriority(Thread.NORM_PRIORITY);
//        }

        return t;
    }

}
