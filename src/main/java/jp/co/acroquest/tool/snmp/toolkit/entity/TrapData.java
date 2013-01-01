// TrapData.java ----
// History: 2004/03/07 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Trap���B
 * 
 * @author akiba
 * @version 1.0
 */
public class TrapData implements Serializable
{
    private static final long serialVersionUID = -2767281869930767183L;

    /** Trap�ŗLOID�B */
    private String            trapOid_;

    /** Trap-Enterprise OID�B */
    private String            enterprise_;

    /** Trap���e��Varbind���i�[���郊�X�g�B */
    private List<SnmpVarbind> varbindList_;

    /** RequestID���ݒ肳��Ă��邩�B */
    private boolean           hasReqId_;

    /** RequestID�B */
    private int               reqId_           = -1;

    /** SNMPTrap Version�B */
    private String            version_;

    /** V1Trap��Generic�l�B */
    private int               generic_         = -1;

    /** V1Trap��Specific�l�B */
    private int               specific_        = -1;

    /**
     * �R���X�g���N�^�B
     */
    public TrapData()
    {
        super();

        this.varbindList_ = new ArrayList<SnmpVarbind>();
    }

    /**
     * SNMPTrap version���擾����B
     * 
     * @return the version
     */
    public String getVersion()
    {
        return this.version_;
    }

    /**
     * SNMPTrap version��ݒ肷��B
     * 
     * @param version the version to set
     */
    public void setVersion(String version)
    {
        this.version_ = version;
    }

    /**
     * RequetID�̎w�肪���邩�B
     * 
     * @return RequetID���ݒ肳��Ă����true�B
     */
    public boolean hasReqId()
    {
        return this.hasReqId_;
    }

    /**
     * RequestID���擾����B
     * 
     * @return RequestID�B
     */
    public int getReqId()
    {
        return this.reqId_;
    }

    /**
     * RequestID��ݒ肷��B
     * 
     * @param reqId RequestID�B
     * @throws IllegalArgumentException RequestID��0�����̏ꍇ�B
     */
    public void setReqId(int reqId)
    {
        // �͈͊O��RequetID�͗�O���X���[����
        if (reqId < 0)
        {
            throw new IllegalArgumentException("RequetID is out of range: " + reqId);
        }

        this.reqId_ = reqId;
        // RequestID���ݒ肳��Ă��邱�Ƃ��}�[�N����
        this.hasReqId_ = true;
    }

    /**
     * Trap-OID��ݒ肷��B
     * 
     * @param oid Trap-OID�B
     */
    public void setTrapOid(String oid)
    {
        this.trapOid_ = oid;
    }

    /**
     * Trap-OID���擾����B
     * 
     * @return Trap-OID�B
     */
    public String getTrapOid()
    {
        return this.trapOid_;
    }

    /**
     * V1Trap��Generic�l���擾����B
     * 
     * @return the generic
     */
    public int getGeneric()
    {
        return this.generic_;
    }

    /**
     * V1Trap��Generic�l��ݒ肷��B
     * 
     * @param generic the generic to set
     */
    public void setGeneric(int generic)
    {
        this.generic_ = generic;
    }

    /**
     * V1Trap��Specific�l���擾����B
     * 
     * @return the specific
     */
    public int getSpecific()
    {
        return this.specific_;
    }

    /**
     * V1Trap��Specific�l��ݒ肷��B
     * 
     * @param specific the specific to set
     */
    public void setSpecific(int specific)
    {
        this.specific_ = specific;
    }

    /**
     * Trap-Enterprise��ݒ肷��B
     * 
     * @param enterprise Trap-Enterprise�B
     */
    public void setEnterprise(String enterprise)
    {
        this.enterprise_ = enterprise;
    }

    /**
     * Trap-Enterprise���擾����B
     * 
     * @return Trap-Enterprise�B
     */
    public String getEnterprise()
    {
        return this.enterprise_;
    }

    /**
     * Varbind��ǉ�����B
     * 
     * @param varbind Varbind�B
     */
    public void addVarbind(SnmpVarbind varbind)
    {
        this.varbindList_.add(varbind);
    }

    /**
     * �ǉ�����Ă���Varbind��z��Ƃ��Ď擾����B
     * 
     * @return Varbind�̔z��B
     */
    public SnmpVarbind[] getVarbinds()
    {
        SnmpVarbind[] varbinds = new SnmpVarbind[this.varbindList_.size()];
        varbinds = (SnmpVarbind[]) this.varbindList_.toArray(varbinds);

        return varbinds;
    }

    /**
     * �f�o�b�O�p�ɁA����Trap�f�[�^�̓��e�𕶎��񉻂���B
     * 
     * @return TrapData�I�u�W�F�N�g�̕�����\���B
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer(512);

        buf.append(this.getClass().getName());
        buf.append("{");
        if ("v1".equals(this.version_) == true)
        {
            buf.append("version=");
            buf.append(this.version_);
            buf.append(",generic=");
            buf.append(this.generic_);
            buf.append(",specific=");
            buf.append(this.specific_);
            buf.append(",enterprise=");
            buf.append(this.enterprise_);
        }
        else if ("v2c".equals(this.version_) == true)
        {
            buf.append("version=");
            buf.append(this.version_);
            buf.append(", trap-oid=");
            buf.append(this.trapOid_);
            buf.append(",enterprise=");
            buf.append(this.enterprise_);
        }
        else
        {
            buf.append("**invalid snmp version : ");
            buf.append(this.version_);
            buf.append('}');
            return buf.toString();
        }

        buf.append(",varbind{");
        SnmpVarbind[] array = this.getVarbinds();
        for (int index = 0; index < array.length; index++)
        {
            if (index > 0)
            {
                buf.append(",");
            }
            buf.append("[<");
            buf.append(index);
            buf.append(">");
            buf.append(array[index].toString());
            buf.append("]");
        }
        buf.append("}");

        return buf.toString();
    }
}
