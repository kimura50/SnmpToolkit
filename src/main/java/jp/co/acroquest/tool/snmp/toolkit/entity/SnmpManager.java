//SnmpManager.java ----
// History: 2009/08/18 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

/**
 * SNMP��Trap���M��ƂȂ�}�l�[�W���̐ݒ��ێ�����G���e�B�e�B�B
 * 
 * @author akiba
 */
public class SnmpManager
{
    /** SNMP�}�l�[�W���̃A�h���X�w��B */
    private String address_;
    
    /**
     * SNMP�}�l�[�W���̃A�h���X��ݒ肷��B
     * 
     * @param address SNMP�}�l�[�W���̃A�h���X�B
     */
    public void setManagerAddress(String address)
    {
        this.address_ = address;
    }
    
    /**
     * SNMP�}�l�[�W���̃A�h���X���擾����B
     * 
     * @return SNMP�}�l�[�W���̃A�h���X�B
     */
    public String getManagerAddress()
    {
        return this.address_;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        String retStr = "SnmpManager{address=" + this.address_ + "}";
        return retStr;
    }
}
