package online.dinghuiye.example;

import online.dinghuiye.api.entity.Process;
import online.dinghuiye.api.entity.ResultStatus;
import online.dinghuiye.api.entity.RowRecord;
import online.dinghuiye.api.entity.TransactionMode;
import online.dinghuiye.core.ImportHandler;
import online.dinghuiye.core.persistence.RowRecordPersistencorHibernateImpl;
import online.dinghuiye.core.resolution.torowrecord.RowRecordHandlerImpl;
import online.dinghuiye.core.validation.RowRecordValidatorImpl;
import online.dinghuiye.example.entity.ExcelUtil4JUserEntity;
import online.dinghuiye.example.repairer.PasswordRepairer;
import online.dinghuiye.example.util.SessionFactoryUtil;
import online.dinghuiye.excel.ExcelFactory;
import org.hibernate.SessionFactory;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Strangeen on 2017/08/30
 */
public class ExcelImportor {

    public static void main(String[] args) {

        SessionFactory factory = null;
        try {

            // 获取SessionFactory
            factory = SessionFactoryUtil.getSessionFactory();
            // 设置mode：SINGLETON为单条存储事务，MULTIPLE为整体事务，详见文档
            TransactionMode mode = TransactionMode.SINGLETON;

            // 创建导入器handler
            ImportHandler handler = new ImportHandler();
            handler.setHandler(new RowRecordHandlerImpl()); // 一对一关系解析器
            handler.setValidator(new RowRecordValidatorImpl()); // 验证器
            handler.setPersistencor(new RowRecordPersistencorHibernateImpl(factory)); // 持久化器hibernate实现
            handler.setRepairer(new PasswordRepairer()); // 密码存储修正器
            handler.setMode(mode);

            // 执行excel导入
            List<RowRecord> resultList = handler.importExcel(
                    ExcelFactory.newExcel(new File("D:/test_template.xlsx")), // 创建AbstractExcel对象读取excle
                    0, // 读取sheet序号为0的sheet
                    new Observer() {
                        @Override
                        public void update(Observable o, Object arg) {
                            // 创建导入进度观察者，arg为导入进度百分数（没有%）
                            Process process = (Process) arg;
                            System.out.println("进度：" + process.getProcess() + "，当前阶段：" + process.getNode());
                        }
                    },
                    ExcelUtil4JUserEntity.class); // 传入POJO

            // 打印结果，如果有错误可以在resultList中得到
            int successCount = 0;
            int errorCount = 0;
            for (RowRecord rr : resultList) {
                if (rr.getResult().getResult() != ResultStatus.SUCCESS) { // 导入不成功
                    System.out.println(rr.getRowNo() + "行 - " + rr.getResult().getMsg()); // 打印行号和错误信息
                    errorCount ++; // 记录错误数
                } else
                    successCount ++; // 记录成功数
            }
            // 注意：MULTIPLE为整体事务，successCount依然可能不为0，仅作为标识，实际上没有任何数据存入数据库的
            System.out.println("success " + successCount + ", error " + errorCount);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SessionFactoryUtil.closeSessionFactory(factory);
        }
    }
}
