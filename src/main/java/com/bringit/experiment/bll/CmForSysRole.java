package com.bringit.experiment.bll;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by Administrator on 4/15/2017.
 */
@Entity
@Table(name = "CmForSysRole")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entity")
@Cacheable
public class CmForSysRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CmRoleId")
    private Integer cmRoleId;


    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="UserRole", unique=false, updatable=true)
    private SysRole userRole;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ContractManufacturer", unique=false, updatable=true)
    private ContractManufacturer contractManufacturer;

    public Integer getCmRoleId() {
        return cmRoleId;
    }

    public void setCmRoleId(Integer cmRoleId) {
        this.cmRoleId = cmRoleId;
    }

    public SysRole getUserRole() {
        return userRole;
    }

    public void setUserRole(SysRole userRole) {
        this.userRole = userRole;
    }

    public ContractManufacturer getContractManufacturer() {
        return contractManufacturer;
    }

    public void setContractManufacturer(ContractManufacturer contractManufacturer) {
        this.contractManufacturer = contractManufacturer;
    }

    public CmForSysRole() {
    }

    public CmForSysRole(SysRole userRole, ContractManufacturer contractManufacturer) {
        this.userRole = userRole;
        this.contractManufacturer = contractManufacturer;
    }
}
