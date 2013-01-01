// SnmpVarbind.java ----
// History: 2004/03/07 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.io.Serializable;

/**
 * Varbind�I�u�W�F�N�g�B
 * 
 * @author akiba
 */
public class SnmpVarbind implements Serializable
{
    /** �V���A��UID�B */
    private static final long  serialVersionUID             = 2664529399476520952L;

    /** READ-WRITE */
    public static final String ACCESSIBILITY_READ_WRITE     = "READ-WRITE";

    /** READ-ONLY */
    public static final String ACCESSIBILITY_READ_ONLY      = "READ-ONLY";

    /** NOT-ACCESSIBLE */
    public static final String ACCESSIBILITY_NOT_ACCESSIBLE = "NOT-ACCESSIBLE";

    /** Varbind��ObjectID�B */
    private String             oid_;

    /** Varbind�̒l�B */
    private String             value_;

    /** Varbind�̒l�̌^�B */
    private String             type_;

    /** Access����\���l�B */
    private String             accessibility_ = ACCESSIBILITY_READ_WRITE;

    /**
     * �R���X�g���N�^�B
     */
    public SnmpVarbind()
    {
        super();
    }

    /**
     * Varbind��ObjectID���擾����B
     * 
     * @return Varbind��ObjectID�B
     */
    public String getOid()
    {
        return this.oid_;
    }

    /**
     * Varbind�̒l���擾����B
     * 
     * @return Varbind�̒l�B
     */
    public String getValue()
    {
        return this.value_;
    }

    /**
     * Varbind��ObjectID��ݒ肷��B
     * 
     * @param oid ObjectID�B
     */
    public void setOid(String oid)
    {
        this.oid_ = oid;
    }

    /**
     * Varbind�̒l���擾����B
     * 
     * @param value Varbind�̒l�B
     */
    public void setValue(String value)
    {
        this.value_ = value;
    }

    /**
     * Varbind�̒l�̌^���擾����B
     * 
     * @return Varbind�̒l�̌^�B
     */
    public String getType()
    {
        return this.type_;
    }

    /**
     * Varbind�̒l�̌^��ݒ肷��B
     * 
     * @param type Varbind�̒l�̌^�B
     */
    public void setType(String type)
    {
        this.type_ = type;
    }

    /**
     * Access����\���l���擾����B
     * 
     * @return Access����\���l�B
     */
    public String getAccessibility()
    {
        return this.accessibility_;
    }

    /**
     * Access����\���l��ݒ肷��B
     * 
     * @param ccessibility Access����\���l�B
     */
    public void setAccessibility(String accessibility)
    {
        if (accessibility == null || accessibility.trim().equals(""))
        {
            this.accessibility_ = ACCESSIBILITY_READ_WRITE;
        }
        else
        {
            this.accessibility_ = accessibility;
        }
    }

    /**
     * �w�肳�ꂽ�I�u�W�F�N�g�����g�Ɠ��������ɂ��Č��؂���B
     * 
     * @return �w�肳�ꂽ�I�u�W�F�N�g��null�łȂ��ASnmpVarbind�N���X�̃I�u�W�F�N�g�ł���A ���S�Ă̑������������ꍇ��true��Ԃ��B
     */
    public boolean equals(Object obj)
    {
        boolean isEqual = false;
        if (obj != null && obj instanceof SnmpVarbind)
        {
            SnmpVarbind given = (SnmpVarbind) obj;

            boolean oidEquals = given.getOid().equals(this.oid_);
            boolean typeEquals = given.getType().equals(this.type_);
            boolean valueEquals = given.getValue().equals(this.value_);
            boolean accessEquals = given.getAccessibility().equals(this.accessibility_);
            isEqual = (oidEquals && typeEquals && valueEquals && accessEquals);
        }

        return isEqual;
    }

    /**
     * ���̃I�u�W�F�N�g�̕�����\�����擾����B
     * 
     * @return ���̃I�u�W�F�N�g�̕�����\���B
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuilder buf = new StringBuilder();

        buf.append("{oid=");
        buf.append(this.oid_);
        buf.append(",access=");
        buf.append(this.accessibility_);
        buf.append(",value=<");
        buf.append(this.type_);
        buf.append(">");
        buf.append(this.value_);
        buf.append("}");

        return buf.toString();
    }
}
