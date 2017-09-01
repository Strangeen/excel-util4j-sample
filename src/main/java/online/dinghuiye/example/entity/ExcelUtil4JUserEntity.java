package online.dinghuiye.example.entity;

import online.dinghuiye.api.annotation.validate.Validate;
import online.dinghuiye.api.validation.Validator;
import online.dinghuiye.core.annotation.convert.ConstValue;
import online.dinghuiye.core.annotation.excel.SheetTitleName;
import online.dinghuiye.example.validator.UniqueValidator;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * @author Strangeen on 2017/08/27
 */
@Entity
@DynamicInsert(true)
@Table(name = "excel_util4j_user")
public class ExcelUtil4JUserEntity {

    @Transient // 不需要执行转换和验证，但并不影响hibernate存表的操作
    private Long id;

    @SheetTitleName("账号") // excel表字段对应
    @NotBlank
    @Size(max = 20, message = "输入最大{max}个字")
    @Validate(validator = UniqueValidator.class, message = "已被注册") // 自定义检验器，判断重复
    private String username;

    @SheetTitleName("密码")
    // 如果后续需要repaire的属性，需要将repaire可能影响的验证加上groups={Validator.class}
    // 否则可能会导致比如字符串长度改变而无法再存表时通过hibernate的验证
    @NotBlank
    @Size(max = 20, min = 6, message = "输入{min}~{max}个字", groups = {Validator.class})
    private String password;

    @ConstValue("1") // 常量值转换器，导入时会被设置为1
    private Integer enable;

    @Valid // 执行hibernate validator支持的对象属性检测，不注释@Valid则不会对info对象的属性进行检测
    private ExcelUtil4JUserInfoEntity info;

    private ExcelUtil4JUserRoleEntity userRole;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "enable")
    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @OneToOne(mappedBy = "user", cascade = {CascadeType.ALL})
    public ExcelUtil4JUserInfoEntity getInfo() {
        return info;
    }

    public void setInfo(ExcelUtil4JUserInfoEntity info) {
        this.info = info;
    }

    // 这里定义为OneToOne并不太合适，只是为了演示
    // 常规应该使用OneToMany，现阶段无法实现OneToMany的导入，就只能使用RowRecordPerPersistentRepairer在存表前进行修正了
    @OneToOne(mappedBy = "user", cascade = {CascadeType.ALL})
    public ExcelUtil4JUserRoleEntity getUserRole() {
        return userRole;
    }

    public void setUserRole(ExcelUtil4JUserRoleEntity userRole) {
        this.userRole = userRole;
    }
}
