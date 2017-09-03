package online.dinghuiye.example.validator;

import online.dinghuiye.api.validation.Validator;
import online.dinghuiye.example.entity.ExcelUtil4JUserInfoEntity;
import online.dinghuiye.example.util.SessionFactoryUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link PhoneUniqueValidator}将判重字段逐一查表检查
 * 区别于{@link UniqueValidator}，后者是缓存检查，效率更高
 *
 * @author Strangeen on 2017/09/03
 */
public class PhoneUniqueValidator implements Validator {

    // 缓存用于保存验证通过的即将导入的数据，因为从数据库里查出的数据只是数据库里的，如果导入文件中有重复会在存库时报错
    private Set<Object> phoneCache = new HashSet<>();

    @Override
    public <T> boolean validate(Object fieldValue, Field field, T obj) {

        Session session = null;
        try {
            ExcelUtil4JUserInfoEntity info = (ExcelUtil4JUserInfoEntity) obj;
            if (phoneCache.contains(info.getPhone())) return false;

            SessionFactory factory = SessionFactoryUtil.getSessionFactory();
            session = factory.openSession();
            Criteria criteria = session.createCriteria(ExcelUtil4JUserInfoEntity.class);
            criteria.add(Restrictions.eq("phone", info.getPhone()));
            if (criteria.list().size() > 0) return false;

            // 验证通过加入到缓存
            phoneCache.add(info.getPhone());
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
