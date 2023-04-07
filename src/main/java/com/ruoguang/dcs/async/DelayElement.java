package com.ruoguang.dcs.async;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


public class DelayElement implements Delayed {
    /**
     * 延迟截止时间（单面：毫秒）
     */
    long delayTime = System.currentTimeMillis();

    public DelayElement(long delayTime) {
        this.delayTime = (this.delayTime + delayTime);
    }

    /**
     * 获取剩余时间
     *
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 队列里元素的排序依据
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        if (this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) {
            return 1;
        } else if (this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return DateFormat.getDateTimeInstance().format(new Date(delayTime));
    }
}
 