//Agent.java ----
// History: 2009/05/07 - Create
//          2009/05/21 - GETNEXT�Ή�
//          2009/08/15 - AgentService�Ή�
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * �P���SNMPAgent�I�u�W�F�N�g�B
 * 
 * @author akiba
 */
public class Agent
{
    /** Agent�̎�ނ�\��Type���́B */
    private String type_;
    
    /** Agent�̑Ή�����SNMP�o�[�W�����Bv1, v2c���w��\�B */
    private String version_;
    
    /** Agent��IP�A�h���X�B */
    private String address_;
    
    /** �ǂݍ��ݐ�p�R�~���j�e�B���B */
    private String roCommunity_;
    
    /** �������ݑΉ��R�~���j�e�B���B */
    private String rwCommunity_;
    
    /** Trap�R�~���j�e�B���B */
    private String trapCommunity_;
    
    /** GET�^SET Request��t�|�[�g�ԍ��B */
    private int snmpPort_;
    
    /** Trap���M�|�[�g�ԍ��B */
    private int trapPort_;
    
    /** SNMP��VariableBinding���i�[����Map�B */
    private SortedMap<String, SnmpVarbind> objectMap_;
    
    /** OID�̔�r���s�����߂�Comparator�B */
    private OIDComparator comp_;
    
    /**
     * Agent�𐶐�����B
     */
    public Agent()
    {
        this.comp_ = new OIDComparator();
        this.objectMap_ = new TreeMap<String, SnmpVarbind>(this.comp_);
    }

    /**
     * Agent��Varbind��ǉ�����B
     * 
     * @param varbind �ǉ�����Varbind�B
     */
    public void addVarbind(SnmpVarbind varbind)
    {
        if (varbind == null)
        {
            return;
        }
        
        String oid = varbind.getOid();
        if (oid == null)
        {
            return;
        }
        this.objectMap_.put(oid, varbind);
    }

    /**
     * �w�肵��OID�ɑΉ�����Varbind���擾����B
     * 
     * @param oid �����Ώ�OID�B
     * @param exact �w�肵��OID�ɑ΂��Č����Ɉ�v����Varbind���������邩�ǂ����B
     * @return �q�b�g����Varbind�B
     */
    public SnmpVarbind findObject(String oid, boolean exact)
    {
        if (oid == null)
        {
            return null;
        }
        Log log = LogFactory.getLog(Agent.class);
        
        SnmpVarbind object = null;
        if (exact == true)
        {
            // ���S��v�w��̏ꍇ�́A���̂܂܂�OID��Varbind����������
            object = this.objectMap_.get(oid);
        }
        else
        {
            // �w�肳�ꂽOID�ȍ~�̃T�u�}�b�v����A�����ɍ��v����ł��ႢVarbind����������
            SortedMap<String, SnmpVarbind> subMap = this.objectMap_.tailMap(oid);
            if (subMap.size() == 0)
            {
                // ����ȏ�Varbind���Ȃ��ꍇ
                log.info("END of MIB.");
                return null;
            }
            
            Set<String> keys = subMap.keySet();
            String[] keyArray = keys.toArray(new String[keys.size()]);
            if (keyArray[0].equals(oid) == true)
            {
                // GETNEXT�Ŋ��S�ɍ��v����OID���w�肳�ꂽ�ꍇ�́A���̗v�f����������
                if (keyArray.length > 1)
                {
                    object = this.objectMap_.get(keyArray[1]);
                }
                else
                {
                    // ����ȏ�Varbind���Ȃ��ꍇ
                    log.info("END of MIB.");
                    return null;
                }
            }
            else if (keyArray[0].contains(oid) == true)
            {
                // GETNEXT�Ŋ��S�ɍ��v���Ȃ�OID���w�肳�ꂽ�ꍇ�́A
                // ���Ɏ��ł��ႢVarbind����������
                object = this.objectMap_.get(keyArray[0]);
            }
        }
        return object;
    }
    
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
        this.rwCommunity_ = rwCommunity;
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
     * @return the type
     */
    public String getType()
    {
        return this.type_;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type_ = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        
        int cnt = 0;
        for (SnmpVarbind obj : this.objectMap_.values())
        {
            if (cnt > 0)
            {
                buf.append(",\n");
            }
            buf.append("varbind:");
            buf.append(obj.toString());
            cnt++;
        }
        
        return buf.toString();
    }
}
