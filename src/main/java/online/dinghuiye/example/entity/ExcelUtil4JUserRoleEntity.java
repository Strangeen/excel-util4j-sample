package online.dinghuiye.example.entity;

import online.dinghuiye.core.annotation.convert.ConstValue;
import online.dinghuiye.core.annotation.excel.Transient;

import javax.persistence.*;

/**
 * @author Strangeen on 2017/08/27
 */
@Entity
@Table(name = "excel_util4j_user_role")
public class ExcelUtil4JUserRoleEntity {

    @Transient
    private Long id;

    private ExcelUtil4JUserEntity user;

    @ConstValue("3") // 导入的用户角色均为3
    private Integer roleId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    public ExcelUtil4JUserEntity getUser() {
        return user;
    }

    public void setUser(ExcelUtil4JUserEntity user) {
        this.user = user;
    }

    @Basic
    @Column(name = "role_id")
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
