package online.dinghuiye.example.validator;

import online.dinghuiye.api.validation.Validator;
import online.dinghuiye.example.entity.ExcelUtil4JUserEntity;
import online.dinghuiye.example.util.SessionFactoryUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link UsernameUniqueValidator}将判重字段逐一查表检查
 * 区别于{@link UniqueValidator}，后者是缓存检查，效率更高
 *
 * @author Strangeen on 2017/09/03
 * @version 2.1.0
 */
public class UsernameUniqueValidator implements Validator {

    // 缓存用于保存验证通过的即将导入的数据，因为从数据库里查出的数据只是数据库里的，如果导入文件中有重复会在存库时报错
    private Set<Object> usernameCache = new HashSet<>();

    @Override
    public <T> boolean validate(Object fieldValue, Field field, T obj) {
        Session session = null;
        try {
            ExcelUtil4JUserEntity user = (ExcelUtil4JUserEntity) obj;
            if (usernameCache.contains(user.getUsername())) return false;

            SessionFactory factory = SessionFactoryUtil.getSessionFactory();
            session = factory.openSession();
            Criteria criteria = session.createCriteria(ExcelUtil4JUserEntity.class);
            criteria.add(Restrictions.eq("username", user.getUsername()));
            if (criteria.list().size() > 0) return false;

            // 验证通过加入到缓存
            usernameCache.add(user.getUsername());
            return true;

        } catch (Exception e) {
            throw e;

        } finally {
            if (session != null) {
                session.close();
            }
        }

    }
}
