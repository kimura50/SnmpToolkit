//Snmp4jTrapSender.java ----
// History: 2004/11/22 - Create
//          2009/08/15 - AgentService�Ή�
package jp.co.acroquest.tool.snmp.toolkit.trap;

import java.io.IOException;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitImpl;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpConfigItem;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpManager;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpManagerList;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpVarbind;
import jp.co.acroquest.tool.snmp.toolkit.entity.TrapData;
import jp.co.acroquest.tool.snmp.toolkit.helper.Snmp4jVariableHelper;
import jp.co.acroquest.tool.snmp.toolkit.helper.SnmpVariableHelper;
import jp.co.acroquest.tool.snmp.toolkit.loader.SnmpConfiguration;
import jp.co.acroquest.tool.snmp.toolkit.stack.Snmp4jFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

/**
 * SNMP4j���C�u�������g�p����TrapSender�B
 * 
 * @author akiba
 * @version 1.0
 */
public class Snmp4jTrapSender extends AbstractTrapSender
{
    private static final Object SNMP_VERSION_V1  = "v1";
    private static final Object SNMP_VERSION_V2C = "v2c";
    
    /** PDU���M�Ɏg�p����Snmp�I�u�W�F�N�g�B */
    private Snmp snmp_;

    /**
     * Snmp4jTrapSender������������B
     * 
     * @param host �o�C���h����z�X�g���B
     * @exception SnmpToolkitException SNMP�R���e�L�X�g�̍쐬�Ɏ��s�����ꍇ�B
     */
    public Snmp4jTrapSender(String host)
        throws SnmpToolkitException
    {
        super();
        Log log = LogFactory.getLog(TrapSender.class);
        
        // Snmp�R���e�L�X�g�̍쐬
        try
        {
            log.debug("Initializing Snmp4jTrapSender. host=" + host);
            this.snmp_ = Snmp4jFactory.createSnmp(host);
        }
        catch (IOException exception)
        {
            // TransportMapping�̍쐬�Ɏ��s�����ꍇ�͓����̗�O�Ƀ��b�v���ăX���[����
            throw new SnmpToolkitException("Failed to create transport mapping.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sendTrap(TrapData trapData) throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(TrapSender.class);
        PDU pdu = null;
        
        int version = 0;
        String versionStr = trapData.getVersion();
        if (SNMP_VERSION_V1.equals(versionStr) == true)
        {
            if (log.isDebugEnabled() == true)
            {
                log.debug("Trap SNMP version is v1.");
            }
            version = SnmpConstants.version1;
            pdu = createTrapV1PDU(trapData);
        }
        else if (SNMP_VERSION_V2C.equals(versionStr) == true)
        {
            if (log.isDebugEnabled() == true)
            {
                log.debug("Trap SNMP version is v2c.");
            }
            version = SnmpConstants.version2c;
            pdu = createTrapV2cPDU(trapData);
        }
        
        // Trap�𑗐M����
        try
        {
            SnmpConfiguration config = SnmpConfiguration.getInstance();
            SnmpConfigItem configItem = config.getSnmpConfigItem();
            SnmpManagerList mgrList = configItem.getSnmpManagerList();
            SnmpManager[] managers = mgrList.getSnmpManagers();

            // ��`����Ă���S�Ẵ}�l�[�W���ɑ��M����
            for (SnmpManager manager : managers)
            {
                String mgrAddress = manager.getManagerAddress();
                if (mgrAddress == null)
                {
                    log.warn("Manager address is null.");
                    throw new SnmpToolkitException("Manager address is null.");
                }
                
                if (log.isDebugEnabled() == true)
                {
                    log.debug("snmp manager=" + mgrAddress);
                }
                
                CommunityTarget target = new CommunityTarget();
                target.setVersion(version);
                target.setCommunity(new OctetString(super.community_));
                Address address = GenericAddress.parse(mgrAddress);
                if (address == null)
                {
                    log.error("Failed to parse manager address.");
                    throw new SnmpToolkitException("Failed to parse manager address.");
                }
                target.setAddress(address);
                
                if (log.isDebugEnabled() == true)
                {
                    log.debug("Sending trap-pdu=" + pdu + ", target=" + target);
                }
                
                this.snmp_.send(pdu, target);
                log.info("Trap is sent to " + target.getAddress());
            }
        }
        catch (IOException ioEx)
        {
            throw new SnmpToolkitException("Failed to send pdu.", ioEx);
        }
        catch (RuntimeException ex)
        {
            log.error("Failed to send pdu caused by unknown error.", ex);
            throw ex;
        }
    }
    
    /**
     * SNMPv1Trap��PDU���擾����B
     * 
     * @param trapData PDU�ɐݒ肷��Trap�f�[�^�B
     * @return SNMPv2Trap��PDU�B
     * @throws SnmpToolkitException �s����Trap�f�[�^�������ꍇ�B
     */
    private PDU createTrapV1PDU(TrapData trapData)
        throws SnmpToolkitException
    {
        SnmpVariableHelper varHelaper = new Snmp4jVariableHelper();
        
        PDUv1 pdu = new PDUv1();
        pdu.setType(PDU.V1TRAP);
        
        // RequestID���w�肳��Ă���΁ATrapPDU�ɐݒ肷��
        boolean hasReqId = trapData.hasReqId();
        if (hasReqId == true)
        {
            pdu.setRequestID(new Integer32(trapData.getReqId()));
        }
        
        int generic = trapData.getGeneric();
        int specific = trapData.getSpecific();
        String enterprise = trapData.getEnterprise();
        
        if (generic < 0)
        {
            throw new SnmpToolkitException("invalid generic: " + generic);
        }
        pdu.setGenericTrap(generic);
        
        if (specific < 0)
        {
            throw new SnmpToolkitException("invalid specific: " + specific);
        }
        pdu.setSpecificTrap(specific);
        
        if (enterprise == null)
        {
            throw new SnmpToolkitException("invalid enterprise: null");
        }
        pdu.setEnterprise(new OID(enterprise));
        
        // sysUpTime��1/100�P�ʁA����32bit Integer�𒴂��Ȃ��悤�ɂ���
        long sysUpTime = SnmpToolkitImpl.getSysUpTime();
        int  sysUpTimeInt = (int)((sysUpTime / 10) % 4294967296L);
        pdu.setTimestamp(sysUpTimeInt);
        
        // Trap�ɕK�v��VariableBinding�̐ݒ�
        SnmpVarbind[] varbinds = trapData.getVarbinds();
        for (int index = 0; index < varbinds.length; index ++)
        {
            String oid   = varbinds[index].getOid();
            Object value = varbinds[index].getValue();
            String type  = varbinds[index].getType();
            Variable asnObject = (Variable) varHelaper.createAsnObject(value, type);
            if (asnObject != null)
            {
                pdu.add(new VariableBinding(new OID(oid), asnObject));
            }
        }
        
        return pdu;
    }
    
    /**
     * SNMPv2Trap��PDU���擾����B
     * 
     * @param trapData PDU�ɐݒ肷��Trap�f�[�^�B
     * @return SNMPv2Trap��PDU�B
     */
    private PDU createTrapV2cPDU(TrapData trapData)
    {
        SnmpVariableHelper varHelaper = new Snmp4jVariableHelper();
        
        PDU pdu = new PDU();
        pdu.setType(PDU.TRAP);
        
        // RequestID���w�肳��Ă���΁ATrapPDU�ɐݒ肷��
        // �w�肳��Ă��Ȃ���΁A�����I�ɃC���N�������g���ꂽ�l��ݒ肷��
        boolean hasReqId = trapData.hasReqId();
        if (hasReqId == true)
        {
            pdu.setRequestID(new Integer32(trapData.getReqId()));
        }
        else
        {
            int newReqId = this.snmp_.getNextRequestID();
            pdu.setRequestID(new Integer32(newReqId));
        }
        
        // SysUpTime�ATrapOID�̐ݒ�
        // sysUpTime��1/100�P�ʁA����32bit Integer�𒴂��Ȃ��悤�ɂ���
        long sysUpTime = SnmpToolkitImpl.getSysUpTime();
        int  sysUpTimeInt = (int)((sysUpTime / 10) % 4294967296L);
        pdu.add(new VariableBinding(SnmpConstants.sysUpTime,
                                    new TimeTicks(sysUpTimeInt)));
        pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID,
                new OID(trapData.getTrapOid())));
        
        // Trap�ɕK�v��VariableBinding�̐ݒ�
        SnmpVarbind[] varbinds = trapData.getVarbinds();
        for (int index = 0; index < varbinds.length; index ++)
        {
            String oid   = varbinds[index].getOid();
            Object value = varbinds[index].getValue();
            String type  = varbinds[index].getType();
            Variable asnObject = (Variable) varHelaper.createAsnObject(value, type);
            if (asnObject != null)
            {
                pdu.add(new VariableBinding(new OID(oid), asnObject));
            }
        }
        
        // trapEnterprise�̐ݒ�
        // XML�t�@�C������<enterprise>�v�f���܂�ł���ꍇ�́A�����ݒ肷��B
        // �w�肪�Ȃ��ꍇ�́A�ݒ肵�Ȃ��B���̏ꍇ�́A������Varbind��ݒ肷��K�v������B
        String enterprise = trapData.getEnterprise();
        if (enterprise != null)
        {
            pdu.add(new VariableBinding(SnmpConstants.snmpTrapEnterprise,
                    new OID(enterprise)));
        }
        
        return pdu;
    }
}
