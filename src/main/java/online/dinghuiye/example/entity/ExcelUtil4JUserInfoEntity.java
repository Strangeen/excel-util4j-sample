package online.dinghuiye.example.entity;

import online.dinghuiye.api.annotation.validate.Validate;
import online.dinghuiye.core.annotation.convert.BlankToNull;
import online.dinghuiye.core.annotation.convert.DateFormat;
import online.dinghuiye.core.annotation.convert.ValueMap;
import online.dinghuiye.core.annotation.excel.SheetTitleName;
import online.dinghuiye.example.validator.PhoneUniqueValidator;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author Strangeen on 2017/08/27
 */
@Entity
@Table(name = "excel_util4j_user_info")
public class ExcelUtil4JUserInfoEntity {

    @Transient
    private Long id;

    @OneToOne // 必须使用hibernate的双向绑定，否则无法生成hibernate的POJO对象
    @JoinColumn(name = "user_id")
    private ExcelUtil4JUserEntity user;

    @SheetTitleName("姓名")
    @NotBlank
    @Size(max = 20, message = "输入最大{max}个字")
    private String name;

    @SheetTitleName("性别")
    @ValueMap("{'男':1,'女':0}") // Map值转换器，将excel的只按照Map映射进行转换
    private Integer gender;

    @SheetTitleName("生日")
    @BlankToNull // 空串转NULL转换器，防止生日字段为空串转换为Date时报错
    @DateFormat("yyyy-MM-dd") // 时间格式转换器，将时间转换为指定格式，如果单元格为“文本”就会使用
    private Date birthday;

    @SheetTitleName("电话")
    // hibernate validator的正则验证，这里大概写一个电话的验证正则
    @Pattern(regexp = "(^(\\+|0)[0-9]{2}[0-9]{11}$)|(^[0-9]{11}$)", message = "填写不正确")
    @Validate(validator = PhoneUniqueValidator.class, message = "已被注册")
    private String phone;

    @SheetTitleName("地址")
    @Size(max = 50, message = "输入最大{max}个字")
    private String address;

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
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "gender")
    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    public ExcelUtil4JUserEntity getUser() {
        return user;
    }

    public void setUser(ExcelUtil4JUserEntity user) {
        this.user = user;
    }
}
