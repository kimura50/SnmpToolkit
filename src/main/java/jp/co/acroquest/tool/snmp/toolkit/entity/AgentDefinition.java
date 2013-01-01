//AgentDefinition.java ----
// History: 2009/05/07 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

/**
 * �P�m�[�h����Agent��`��\���G���e�B�e�B�B
 * 
 * @author akiba
 */
public class AgentDefinition
{
    /** Agent��MIB�f�[�^���`���Ă���t�@�C�����B�@*/
    private String agentMIBFile_;
    
    /** ����Agent��IP�A�h���X�B�@*/
    private String address_;
    
    /** ����Agent�̑Ή�����SNMP�o�[�W�����B */
    private String version_;
    
    /** ����Agent��GET/SET�pSNMP�|�[�g�ԍ��B�@*/
    private int snmpPort_;
    
    /** ����Agent��Trap�pSNMP�|�[�g�ԍ��B */
    private int trapPort_;
    
    /** ����Agent�̓ǂݎ���p�R�~���j�e�B���B */
    private String roCommunity_;
    
    /** ����Agent�̏������݉\�R�~���j�e�B���B */
    private String rwCommunity_;
    
    /** ����Agent��Trap�R�~���j�e�B���B */
    private String trapCommunity_;
    
    /**
     * @return the version
     */
    public String getVersion()
    {
        return this.version_;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version)
    {
        this.version_ = version;
    }

    /**
     * @return the snmpPort
     */
    public int getSnmpPort()
    {
        return this.snmpPort_;
    }

    /**
     * @param snmpPort the snmpPort to set
     */
    public void setSnmpPort(int snmpPort)
    {
        this.snmpPort_ = snmpPort;
    }

    /**
     * @return the trapPort
     */
    public int getTrapPort()
    {
        return this.trapPort_;
    }

    /**
     * @param trapPort the trapPort to set
     */
    public void setTrapPort(int trapPort)
    {
        this.trapPort_ = trapPort;
    }

    /**
     * @return the roCommunity
     */
    public String getRoCommunity()
    {
        return this.roCommunity_;
    }

    /**
     * @param roCommunity the roCommunity to set
     */
    public void setRoCommunity(String roCommunity)
    {
        this.roCommunity_ = roCommunity;
    }

    /**
     * @return the rwCommunity
     */
    public String getRwCommunity()
    {
        return this.rwCommunity_;
    }

    /**
     * @param rwCommunity the rwCommunity to set
     */
    public void setRwCommunity(String rwCommunity)
    {
        rwCommunity_ = rwCommunity;
    }

    /**
     * @return the trapCommunity
     */
    public String getTrapCommunity()
    {
        return this.trapCommunity_;
    }

    /**
     * @param trapCommunity the trapCommunity to set
     */
    public void setTrapCommunity(String trapCommunity)
    {
        this.trapCommunity_ = trapCommunity;
    }

    /**
     * @return the agentMIBFile
     */
    public String getAgentMIBFile()
    {
        return this.agentMIBFile_;
    }
    
    /**
     * @param agentMIBFile the agentMIBFile to set
     */
    public void setAgentMIBFile(String agentMIBFile)
    {
        this.agentMIBFile_ = agentMIBFile;
    }
    
    /**
     * @return the address
     */
    public String getAddress()
    {
        return this.address_;
    }
    
    /**
     * @param address the address to set
     */
    public void setAddress(String address)
    {
        this.address_ = address;
    }
    
    /**
     * AgentInfo�̕�����\�����擾����B
     * 
     * @return ���̃I�u�W�F�N�g�̕�����\���B
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append('{');
        buf.append("addres=");
        buf.append(this.address_);
        buf.append(',');
        buf.append("version=");
        buf.append(this.version_);
        buf.append(',');
        buf.append("ro-community=");
        buf.append(this.roCommunity_);
        buf.append(',');
        buf.append("rw-community=");
        buf.append(this.rwCommunity_);
        buf.append(',');
        buf.append("trap-community=");
        buf.append(this.trapCommunity_);
        buf.append(',');
        buf.append("snmp-port=");
        buf.append(this.snmpPort_);
        buf.append(',');
        buf.append("trap-port=");
        buf.append(this.trapPort_);
        buf.append(',');
        buf.append("file=");
        buf.append(this.agentMIBFile_);
        buf.append('}');
        
        return buf.toString();
    }
}
