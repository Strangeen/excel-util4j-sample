package online.dinghuiye.example.validator;

import online.dinghuiye.api.validation.Validator;
import online.dinghuiye.example.util.SessionFactoryUtil;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 判重Validator
 *
 * @author Strangeen on 2017/08/16
 */
public class UniqueValidator implements Validator {

    /*
    实现原理是将数据库中唯一值全部读取出来缓存到cache中，
    然后将导入的只和cache比对，如果重复则返回false，如果不重复则加入到cache，返回true

    这样做可以提高检验效率，但是必须要考虑并发问题
     */

    private static Set<Object> usernameCache = new HashSet<>();
    private static Set<Object> phoneCache = new HashSet<>();

    public UniqueValidator() {
        // 设置username的cache
        setCache(usernameCache, "excel_util4j_user", "username");
        // 设置phone的cache
        setCache(phoneCache, "excel_util4j_user_info", "phone");
    }

    public static void setCache(Set<Object> cache, String tableName, String columnName) {
        SessionFactory factory = SessionFactoryUtil.getSessionFactory();
        Session session = factory.openSession();
        SQLQuery query = session.createSQLQuery("select " + columnName + " from " + tableName);
        List<Object> list = query.list();
        for (Object obj : list) {
            cache.add(obj);
        }
        session.close();
    }

    @Override
    public <T> boolean validate(Object fieldValue, Field field, T obj) {

        // 判断是username还是phone，这里只是演示，所以将cache写在一起，常规思路应该是分开的2个类
        if ("username".equals(field.getName())) {
            if (usernameCache.contains(fieldValue)) return false;
            usernameCache.add(fieldValue);
            return true;
        } else if ("phone".equals(field.getName())) {
            if (phoneCache.contains(fieldValue)) return false;
            phoneCache.add(fieldValue);
            return true;
        }

        // 其他字段不用检测，直接返回true
        return true;
    }
}
