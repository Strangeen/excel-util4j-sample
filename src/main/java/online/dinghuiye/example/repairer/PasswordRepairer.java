package online.dinghuiye.example.repairer;

import online.dinghuiye.api.entity.ResultStatus;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.api.persistence.RowRecordPerPersistentRepairer;
import online.dinghuiye.example.entity.ExcelUtil4JUserEntity;
import online.dinghuiye.example.util.MD5Util;

import java.util.List;

/**
 * @author Strangeen on 2017/08/30
 *
 * 对密码进行MD5加密处理
 * 由于密码需要验证长度，所以不能在验证前就MD5加密，否则验证是不正确的
 * 所以需要在存表前进行修正
 *
 * 通过实现RowRecordPerPersistentRepairer可以获得hibernate的POJO对象，从而进行修正
 */
public class PasswordRepairer implements RowRecordPerPersistentRepairer {

    @Override
    public void repaire(List<RowRecord> list) {
        for (RowRecord rr : list) {
            if (rr.getResult().getResult() != ResultStatus.SUCCESS) continue;
            ExcelUtil4JUserEntity obj =
                    (ExcelUtil4JUserEntity) rr.getPojoRecordMap().get(ExcelUtil4JUserEntity.class);
            obj.setPassword(MD5Util.encode(obj.getPassword()));
        }
    }
}
