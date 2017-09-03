package online.dinghuiye.example.repairer;

import online.dinghuiye.api.entity.Process;
import online.dinghuiye.api.entity.ResultStatus;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.api.persistence.RowRecordPerPersistentRepairer;
import online.dinghuiye.example.entity.ExcelUtil4JUserEntity;
import online.dinghuiye.example.util.MD5Util;

import java.util.List;

/**
 * 对密码进行MD5加密处理
 * 由于密码需要验证长度，所以不能在验证前就MD5加密，否则验证是不正确的
 * 所以需要在存表前进行修正
 *
 * 通过实现RowRecordPerPersistentRepairer可以获得hibernate的POJO对象，从而进行修正
 *
 * v2.1.0 进度监控更佳精确，接口提供了进度对象
 *        如果遍历了List<RowRecord> list，可以对每一次循环执行process.updateProcess(1)
 *        需要注意的是，使用前必须判断`process`是否为`null`，
 *        如果入口方法出传入的`ProcessObserver`为`null`，那么`process`就会为`null`
 *        如果没有遍历或者不执行上述方法，当repairer执行完毕，程序会自动修正进度，
 *           进度展示效果会立即变更到repairer方法执行完毕的进度状态
 *
 * @author Strangeen on 2017/9/3
 * @version 2.1.0
 */
public class PasswordRepairer implements RowRecordPerPersistentRepairer {

    @Override
    public void repaire(List<RowRecord> list, Process process) {
        for (RowRecord rr : list) {
            if (rr.getResult().getResult() != ResultStatus.SUCCESS) continue;
            ExcelUtil4JUserEntity obj =
                    (ExcelUtil4JUserEntity) rr.getPojoRecordMap().get(ExcelUtil4JUserEntity.class);
            obj.setPassword(MD5Util.encode(obj.getPassword()));

            // 精确的进度展示，可以操作process对象
            if (process != null)
                process.updateProcess(1);
        }
    }
}
