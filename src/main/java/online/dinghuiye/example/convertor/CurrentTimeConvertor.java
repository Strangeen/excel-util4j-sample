package online.dinghuiye.example.convertor;

import online.dinghuiye.api.resolution.Convertor;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

/**
 * 当前时间转换器，该转换器为自定义转换器，用于适应字段为当前时间的情况
 * 自定义转换器需要实现online.dinghuiye.api.resolution.Convertor
 *
 * @author Strangeen on 2017/09/04
 * @version 2.1.0
 */
public class CurrentTimeConvertor implements Convertor {

    // convet方法参数会传入所有可能用到的值
    // obj 需要转换的值
    // field pojo属性字段
    // excelRecordMap excel数据map<表头名称, 单元格值>
    @Override
    public Object convert(Object obj, Field field, Map<String, Object> excelRecordMap) {

        // 返回当前时间即可，自定义转换器也可以用于其他特定值得转换
        return new Date();
    }
}
