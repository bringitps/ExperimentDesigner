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
import java.util.Date;

/**
 * Created by vf-root on 14/4/17.
 */

@Entity
@Table(name="SMTP")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="entity")
@Cacheable
public class SysSmtp {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="idSMTP")
    private Integer idSmtp;

    @Column(name="PrimarySMTPGateway")
    String smtpGateway;

    @Column(name="SMTPPort")
    Integer smtpPort;

    @Column(name="SMTPLoginUser")
    String smtpLoginUser;

    @Column(name="SMTPLoginPassword")
    String smtpLoginPassword;

    @Column(name="Pop3ServerAddress")
    String pop3ServerAddress;

    @Column(name="Pop3PortNumber")
    Integer pop3PortNumber;

    @Column(name="Pop3UserName")
    String pop3UserName;

    @Column(name="Pop3UserPassword")
    String pop3UserPassword;

    @Column(name="ReplyAddress")
    String replyAddress;

    @Column(name="CreatedDate")
    private Date createdDate;

    @Column(name="ModifiedDate")
    private Date modifiedDate;

    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="CreatedBy", unique=false, updatable=false)
    private SysUser createdBy;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LastModifiedBy", unique=false, updatable=true)
    private SysUser lastModifiedBy;

    public SysSmtp() {}

    public SysSmtp(String smtpGateway, Integer smtpPort, String smtpLoginUser, String smtpLoginPassword,
                   String pop3ServerAddress, Integer pop3PortNumber, String pop3UserName, String pop3UserPassword,
                   String replyAddress, Date createdDate, Date modifiedDate, SysUser createdBy, SysUser lastModifiedBy) {

        this.smtpGateway = smtpGateway;
        this.smtpPort = smtpPort;
        this.smtpLoginUser = smtpLoginUser;
        this.smtpLoginPassword = smtpLoginPassword;
        this.pop3ServerAddress = pop3ServerAddress;
        this.pop3PortNumber = pop3PortNumber;
        this.pop3UserName = pop3UserName;
        this.pop3UserPassword = pop3UserPassword;
        this.replyAddress = replyAddress;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
    }

    public Integer getIdSmtp() {
        return idSmtp;
    }

    public void setIdSmtp(Integer idSmtp) {
        this.idSmtp = idSmtp;
    }

    public String getSmtpGateway() {
        return smtpGateway;
    }

    public void setSmtpGateway(String smtpGateway) {
        this.smtpGateway = smtpGateway;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getSmtpLoginUser() {
        return smtpLoginUser;
    }

    public void setSmtpLoginUser(String smtpLoginUser) {
        this.smtpLoginUser = smtpLoginUser;
    }

    public String getSmtpLoginPassword() {
        return smtpLoginPassword;
    }

    public void setSmtpLoginPassword(String smtpLoginPassword) {
        this.smtpLoginPassword = smtpLoginPassword;
    }

    public String getPop3ServerAddress() {
        return pop3ServerAddress;
    }

    public void setPop3ServerAddress(String pop3ServerAddress) {
        this.pop3ServerAddress = pop3ServerAddress;
    }

    public Integer getPop3PortNumber() {
        return pop3PortNumber;
    }

    public void setPop3PortNumber(Integer pop3PortNumber) {
        this.pop3PortNumber = pop3PortNumber;
    }

    public String getPop3UserName() {
        return pop3UserName;
    }

    public void setPop3UserName(String pop3UserName) {
        this.pop3UserName = pop3UserName;
    }

    public String getPop3UserPassword() {
        return pop3UserPassword;
    }

    public void setPop3UserPassword(String pop3UserPassword) {
        this.pop3UserPassword = pop3UserPassword;
    }

    public String getReplyAddress() {
        return replyAddress;
    }

    public void setReplyAddress(String replyAddress) {
        this.replyAddress = replyAddress;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public SysUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SysUser createdBy) {
        this.createdBy = createdBy;
    }

    public SysUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(SysUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
