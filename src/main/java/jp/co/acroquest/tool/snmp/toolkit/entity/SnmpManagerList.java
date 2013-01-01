//SnmpManagerList.java ----
// History: 2009/08/18 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * SNMP�}�l�[�W���̃��X�g��ێ�����G���e�B�e�B�B
 * 
 * @author akiba
 */
public class SnmpManagerList
{
    /** SNMP�}�l�[�W���̃G���e�B�e�B��ێ����郊�X�g�B */
    private List<SnmpManager> managerList_;
    
    /**
     * SNMP�}�l�[�W���̃��X�g������������B
     */
    public SnmpManagerList()
    {
        this.managerList_ = new ArrayList<SnmpManager>();
    }
    
    /**
     * SNMP�}�l�[�W���̃G���e�B�e�B��ǉ�����B
     * 
     * @param mgr SNMP�}�l�[�W���̃G���e�B�e�B�B
     */
    public void addSnmpManager(SnmpManager mgr)
    {
        if (mgr == null)
        {
            return ;
        }
        
        this.managerList_.add(mgr);
    }
    
    /**
     * �S�Ă�SNMP�}�l�[�W����z��Ŏ擾����B
     * 
     * @return �S�Ă�SNMP�}�l�[�W���̔z��B
     */
    public SnmpManager[] getSnmpManagers()
    {
        SnmpManager[] managers = new SnmpManager[this.managerList_.size()];
        managers = this.managerList_.toArray(managers);
        
        return managers;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("SnmpManagerList{");
        int index = 0;
        for (SnmpManager mgr : this.managerList_)
        {
            if (index > 0)
            {
                buf.append(',');
            }
            buf.append(mgr.toString());
            index++;
        }
        buf.append('}');
        return buf.toString();
    }
}
