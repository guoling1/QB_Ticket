package com.jkm.util.reactor;

import org.apache.log4j.Logger;
import reactor.event.Event;
import reactor.function.Consumer;

/**
 * Created by hutao on 15/7/10.
 * 下午7:09
 */
public abstract class AbstractTaskProcessor<T extends TaskEvent> implements Consumer<Event<T>> {

    private static Logger log = Logger.getLogger(TaskReactor.class);
    /**
     * 模版方法，适配接口
     *
     * @param tEvent
     */
    @Override
    public final void accept(final Event<T> tEvent) {
        try {
            process(tEvent.getData());
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * 处理异常 有默认实现 可覆盖
     *
     * @param e
     */
    protected void handleException(final Exception e) {
        log.error("reactor consumer task handle task event error", e);
    }

    /**
     * 处理事件
     *
     * @param data
     */
    protected abstract void process(final T data);
}
