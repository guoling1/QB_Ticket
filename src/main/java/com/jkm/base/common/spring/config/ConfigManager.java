package com.jkm.base.common.spring.config;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hutao on 15/8/20.
 * 下午3:43
 */
@Component
@Slf4j
public class ConfigManager {
    private static final ImmutableMap<Class, ConfigValueConvertor> CONVERTOR_IMMUTABLE_MAP;
    private static List<ConfigResource> configResources = Lists.newArrayList();
    private static List<Pair<ConfigListener, ConfigEventFilter>> listeners = new ArrayList<>();
    private static AtomicBoolean isInit = new AtomicBoolean(false);

    static {
        CONVERTOR_IMMUTABLE_MAP = new ImmutableMap.Builder<Class, ConfigValueConvertor>()
                .put(Integer.class, new ConfigValueConvertor<Integer>() {
                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public Integer convert(final String value) {
                        return NumberUtils.toInt(value);
                    }
                })
                .put(Double.class, new ConfigValueConvertor<Double>() {
                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public Double convert(final String value) {
                        return NumberUtils.toDouble(value);
                    }
                })
                .put(Long.class, new ConfigValueConvertor<Long>() {
                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public Long convert(final String value) {
                        return NumberUtils.toLong(value);
                    }
                })
                .put(Float.class, new ConfigValueConvertor<Float>() {
                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public Float convert(final String value) {
                        return NumberUtils.toFloat(value);
                    }
                })
                .put(Short.class, new ConfigValueConvertor<Short>() {
                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    public Short convert(final String value) {
                        return NumberUtils.toShort(value);
                    }
                })
                .build();
    }

    private static List<ConfigResource> getConfigResources() {
        if (isNotInit()) {
            init();
        }
        return configResources;
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    public static String get(final String key) {
        for (final ConfigResource configResource : getConfigResources()) {
            if (configResource.containsKey(key)) {
                return configResource.get(key);
            }
        }
        return "";
    }

    /**
     * 获取
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String get(final String key, final String defaultValue) {
        final String result = get(key);
        return Strings.isNullOrEmpty(result) ? defaultValue : result;
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    public static double getDouble(final String key) {
        return getNumber(key, 0d, Double.class);
    }

    /**
     * 获取
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static double getDouble(final String key, final double defaultValue) {
        return getNumber(key, defaultValue, Double.class);
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    public static long getLong(final String key) {
        return getNumber(key, 0l, Long.class);
    }

    /**
     * 获取
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLong(final String key, final long defaultValue) {
        return getNumber(key, defaultValue, Long.class);
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    public static int getInt(final String key) {
        return getNumber(key, 0, Integer.class);
    }

    /**
     * 获取
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(final String key, final int defaultValue) {
        return getNumber(key, defaultValue, Integer.class);
    }

    /**
     * 获取
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getNumber(final String key, final Class<T> tClass) {
        Preconditions.checkArgument(CONVERTOR_IMMUTABLE_MAP.containsKey(tClass));
        return getT(key, (ConfigValueConvertor<T>) CONVERTOR_IMMUTABLE_MAP.get(tClass));
    }

    /**
     * @param key
     * @param defaultValue
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getNumber(final String key, final T defaultValue, final Class<T> tClass) {
        Preconditions.checkArgument(CONVERTOR_IMMUTABLE_MAP.containsKey(tClass));
        final String value = get(key);
        return Strings.isNullOrEmpty(value) ? defaultValue :
                (T) CONVERTOR_IMMUTABLE_MAP.get(tClass).convert(value);
    }

    private static <T> T getT(final String value, final ConfigValueConvertor<T> convertor) {
        return convertor.convert(value);
    }

    /**
     * @param key
     * @param convertor string->obj
     * @param <T>
     * @return
     */
    public static <T> T getObj(final String key, final ConfigValueConvertor<T> convertor) {
        final String value = get(key);
        return Strings.isNullOrEmpty(value) ? null : getT(value, convertor);
    }

    /**
     * @param key
     * @param defaultValue
     * @param convertor    string->obj
     * @param <T>
     * @return
     */
    public static <T> T getObj(final String key, final T defaultValue, final ConfigValueConvertor<T> convertor) {
        final String value = get(key);
        return Strings.isNullOrEmpty(value) ? defaultValue : getT(value, convertor);
    }

    /**
     * 重新载入
     */
    public static void refresh() {
        for (final ConfigResource configResource : getConfigResources()) {
            if (configResource instanceof RefreshableConfigResource) {
                final List<Pair<String, String>> updateKeyAndValues = ((RefreshableConfigResource) configResource).refresh();
                notify(updateKeyAndValues);
            }
        }
    }

    /**
     * 通知更新
     *
     * @param updateKeyAndValues
     */
    public static void notify(final List<Pair<String, String>> updateKeyAndValues) {
        if (!updateKeyAndValues.isEmpty()) {
            final List<ConfigChangeEvent> configChangeEvents = Lists.transform(updateKeyAndValues, new Function<Pair<String, String>, ConfigChangeEvent>() {
                /**
                 * {@inheritDoc}
                 */
                @Override
                public ConfigChangeEvent apply(final Pair<String, String> keyAndValue) {
                    return new ConfigChangeEvent(keyAndValue.getKey(), keyAndValue.getValue());
                }
            });
            notifyConfigListeners(configChangeEvents);
        }
    }

    /**
     * 通知更新
     *
     * @param updateKeyAndValue
     */
    public static void notify(final Pair<String, String> updateKeyAndValue) {
        notify(Lists.newArrayList(updateKeyAndValue));
    }

    private static void notifyConfigListeners(final List<ConfigChangeEvent> configChangeEvents) {
        for (final Pair<ConfigListener, ConfigEventFilter> listenerPair : listeners) {
            notifySingleListener(configChangeEvents, listenerPair.getKey(), listenerPair.getRight());
        }
    }

    private static void notifySingleListener(final List<ConfigChangeEvent> configChangeEvents,
                                             final ConfigListener listener,
                                             final ConfigEventFilter filter) {
        try {
            for (final ConfigChangeEvent configChangeEvent : configChangeEvents) {
                if (!filter.filter(configChangeEvent)) {
                    listener.onConfigChange(configChangeEvent);
                }
            }
        } catch (final Exception e) {
            log.error("notify config change event error:{}", e.getMessage(), e);
        }
    }

    /**
     * 订阅所有更新消息
     *
     * @param configListener
     */
    public static void subscribe(final ConfigListener configListener) {
        subscribeImpl(configListener, new NullConfigEventFilter());
    }

    private static void subscribeImpl(final ConfigListener configListener,
                                      final ConfigEventFilter eventFilter) {
        listeners.add(Pair.of(configListener, eventFilter));
    }

    /**
     * 订阅config key的更新消息
     *
     * @param configListener
     * @param key
     */
    public static void subscribe(final ConfigListener configListener,
                                 final String key) {
        subscribeImpl(configListener, new SingleConfigEventFilter(key));
    }

    /**
     * 初始化
     */
    public static void init() {
        if (isNotInit()) {
            synchronized (ConfigManager.class) {
                if (isNotInit()) {
                    for (final ConfigResource configResource : configResources) {
                        configResource.init();
                    }
                    isInit.set(true);
                }
            }
        }
    }

    private static boolean isNotInit() {
        return !isInit.get();
    }

    /**
     * spring 设置configs
     *
     * @param configResources
     */
    public void setConfigsResources(final List<ConfigResource> configResources) {
        ConfigManager.configResources = configResources;
        Collections.sort(ConfigManager.configResources, new Comparator<ConfigResource>() {
            /**
             * 比较优先级
             * 优先级最高的放在list前面
             * 数字越大优先级越高
             *
             * @param o1
             * @param o2
             * @return
             */
            @Override
            public int compare(final ConfigResource o1, final ConfigResource o2) {
                final int priority1 = getPriority(o1);
                final int priority2 = getPriority(o2);
                return priority1 < priority2 ? 1 : priority1 == priority2 ? 0 : -1;
            }

            private int getPriority(final ConfigResource configResource) {
                if (configResource instanceof PriorityConfigResource) {
                    return ((PriorityConfigResource) configResource).getPriority();
                }
                return 0;
            }
        });
        isInit = new AtomicBoolean(false);
    }
}
