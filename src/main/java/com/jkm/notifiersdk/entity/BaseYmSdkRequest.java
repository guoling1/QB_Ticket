package com.jkm.notifiersdk.entity;

import com.jkm.notifiersdk.config.YmSdkConstants;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.validator.constraints.NotEmpty;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 16/8/2.
 *
 * @author hutao
 * @version 1.0
 */
@Data
public class BaseYmSdkRequest {
    /**
     * 用户序列号
     */
    @NotEmpty(message = "用户序列号不能为空。")
    private final String cdkey = YmSdkConstants.getYmSdkConfig().cdKey();
    /**
     * 用户密码
     */
    @NotEmpty(message = "用户密码不能为空。")
    private final String password = YmSdkConstants.getYmSdkConfig().password();

    @SneakyThrows
    public Map<String, String> serializeToMap() {
        final List<Field> fields = FieldUtils.getAllFieldsList(this.getClass());
        final Map<String, String> result = new HashMap<>();
        for (final Field field : fields) {
            final Object fieldValue = FieldUtils.readField(field, this, true);
            if (fieldValue != null) {
                result.put(field.getName(), fieldValue.toString());
            }
        }
        return result;
    }
}
