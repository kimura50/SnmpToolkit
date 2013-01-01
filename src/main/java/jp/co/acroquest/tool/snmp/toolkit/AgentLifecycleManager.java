//AgentLifecycleManager.java ----
// History: 2009/05/05 - Create
//          2009/08/15 - AgentService�Ή�
package jp.co.acroquest.tool.snmp.toolkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.entity.AgentDefinition;
import jp.co.acroquest.tool.snmp.toolkit.entity.AgentDefinitionList;
import jp.co.acroquest.tool.snmp.toolkit.loader.AgentMIBCSVLoader;
import jp.co.acroquest.tool.snmp.toolkit.loader.AgentDefinitionListLoader;
import jp.co.acroquest.tool.snmp.toolkit.trap.TrapSender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Agent�̋N���E�I���̃��C�t�T�C�N�����Ǘ�����N���X�B
 * 
 * @author akiba
 */
public class AgentLifecycleManager
{
    /** Agent��`�f�[�^�z�u�f�B���N�g���̃f�t�H���g�l�B */
    private static final String DEFAULT_DATA_DIR = "data";
    
    /** ���̃I�u�W�F�N�g�̋��ʃC���X�^���X�B */
    private static AgentLifecycleManager instance__;
    
    /** AgentInfo�̃��X�g�B */
    private AgentDefinitionList agentDefList_;
    
    /** AgentService��ێ�����Map�I�u�W�F�N�g�B */
    private Map<String, AgentService> agentServiceMap_;
    
    /** Agent��`�f�[�^�z�u�f�B���N�g���B */
    private String dataDir_;
    
    /**
     * AgentLifecycleManager�̃C���X�^���X���擾����B
     * 
     * @return AgentLifecycleManager�C���X�^���X�B
     */
    public static AgentLifecycleManager getInstance()
    {
        if (instance__ == null)
        {
            instance__ = new AgentLifecycleManager();
        }

        return instance__;
    }
    
    /**
     * �R���X�g���N�^�B
     */
    private AgentLifecycleManager()
    {
        this.dataDir_ = DEFAULT_DATA_DIR;
    }
    
    /**
     * Agent���X�g��`��ǂݍ��݁A�S�Ă�Agent�����[�h����B
     * 
     * @param defFile Agent��`�t�@�C���̃p�X�B
     * @throws IOException Agent��`�t�@�C���̓ǂݍ��݂Ɏ��s�����ꍇ�B
     */
    public void loadAgent(String defFile) throws IOException
    {
        Log log = LogFactory.getLog(AgentLifecycleManager.class);
        log.info("Start loading agents. defFile=" + defFile);
        
        AgentDefinitionListLoader listLoader = new AgentDefinitionListLoader();
        this.agentDefList_ = listLoader.load(defFile);
        
        this.agentServiceMap_ = new HashMap<String, AgentService>();
        for (AgentDefinition agentDef : this.agentDefList_.getAgentDefinitionArray())
        {
            // Agent��`�t�@�C������Agent�I�u�W�F�N�g�𐶐�����
            String agentAddress = agentDef.getAddress();
            String agentMibFile = agentDef.getAgentMIBFile();
            AgentMIBCSVLoader defLoader = new AgentMIBCSVLoader();
            Agent agent = defLoader.load(this.dataDir_, agentMibFile);
            
            // Agent�I�u�W�F�N�g�ɑ�����ݒ肷��
            agent.setAddress(agentDef.getAddress());
            agent.setSnmpPort(agentDef.getSnmpPort());
            agent.setTrapPort(agentDef.getTrapPort());
            agent.setRoCommunity(agentDef.getRoCommunity());
            agent.setRwCommunity(agentDef.getRwCommunity());
            agent.setTrapCommunity(agentDef.getTrapCommunity());
            
            // Agent���}�b�v�Ɋi�[����
            AgentService service = new AgentService(agent);
            this.agentServiceMap_.put(agentAddress, service);
            if (log.isDebugEnabled())
            {
                log.debug("loaded agent: address=" + agentAddress);
            }
        }
        
        log.info("Finished loading agents.");
    }
    
    /**
     * MIB��`�f�[�^�̍ēǂݍ��݂����{����B
     * 
     * @throws SnmpToolkitException
     */
    public void reloadMIBData()
        throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(AgentLifecycleManager.class);
        log.info("Start reloading all agents.");
        
        synchronized(this.agentServiceMap_)
        {
            Set<String> agentAddressSet = this.agentServiceMap_.keySet();
            for (String agentAddress : agentAddressSet)
            {
                reloadMIBData(agentAddress);
            }
        }
        
        log.info("Finished reloading all agents.");
    }

    /**
     * ����IP�A�h���X��Agent�ɂ���MIB��`�f�[�^�̍ēǂݍ��݂����{����B
     * 
     * @param ipAddress
     * @throws IOException
     * @throws SnmpToolkitException
     */
    public void reloadMIBData(String ipAddress) 
        throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(AgentLifecycleManager.class);
        log.info("Start reloading agents. address=" + ipAddress);
        
        synchronized(this.agentServiceMap_)
        {
            try
            {
                AgentService service = this.agentServiceMap_.get(ipAddress);
                Agent oldAgent = service.getAgent();
                
                // Agent��`�t�@�C������Agent�I�u�W�F�N�g�𐶐�����
                AgentDefinition agentInfo = this.agentDefList_.getAgentDefinition(ipAddress);
                String agentDefFile = agentInfo.getAgentMIBFile();
                AgentMIBCSVLoader defLoader = new AgentMIBCSVLoader();
                Agent agent = defLoader.load(this.dataDir_, agentDefFile);
                log.debug("new agent loaded. " + agent.toString());
                
                // Agent�I�u�W�F�N�g�ɑ�����ݒ肷��
                agent.setAddress(oldAgent.getAddress());
                agent.setSnmpPort(oldAgent.getSnmpPort());
                agent.setTrapPort(oldAgent.getTrapPort());
                agent.setRoCommunity(oldAgent.getRoCommunity());
                agent.setRwCommunity(oldAgent.getRwCommunity());
                agent.setTrapCommunity(oldAgent.getTrapCommunity());
                
                // Agent���X�V����
                service.reloadAgent(agent);
            }
            catch (IOException exception)
            {
                throw new SnmpToolkitException(exception);
            }
        }
        
        log.info("Finished reloading agents. address=" + ipAddress);
    }
    
    /**
     * �w�肳�ꂽ�A�h���X��Agent���N������B
     * 
     * @param address Agent���N������A�h���X�B
     * @throws SnmpToolkitException Agent�̋N���Ɏ��s�����ꍇ�B
     */
    public void startAgent(String address) throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(AgentLifecycleManager.class);
        
        AgentService service = this.agentServiceMap_.get(address);
        if (service == null)
        {
            log.warn("cannot find agent: address=" + address);
            return;
        }
        
        service.startService();
        log.info("started agent: address=" + address);
    }
    
    /**
     * �o�^����Ă���S�Ă�Agent���N������B
     * 
     * @throws SnmpToolkitException Agent�̋N���Ɏ��s�����ꍇ�B
     */
    public void startAllAgents() throws SnmpToolkitException
    {
        if (this.agentServiceMap_ == null)
        {
            return;
        }
        
        Set<String> keys = this.agentServiceMap_.keySet();
        for (String address : keys)
        {
            startAgent(address);
        }
    }
    
    /**
     * �w�肳�ꂽIP�A�h���X��Agent������TrapSender���擾����B
     * 
     * @param ipAddress �擾����Agent��IP�A�h���X�B
     * @return TrapSender�I�u�W�F�N�g�B
     */
    public TrapSender getTrapSender(String ipAddress)
    {
        TrapSender sender = null;
        
        AgentService  service = this.agentServiceMap_.get(ipAddress);
        if (service != null)
        {
            sender = service.getTrapSender();
        }
        
        return sender;
    }

    /**
     * Agent��`�t�@�C���̔z�u�f�B���N�g����ݒ肷��B
     * 
     * @param dataDir Agent��`�t�@�C���̔z�u�f�B���N�g���B
     */
    public void setDataDir(String dataDir)
    {
        this.dataDir_ = dataDir;
    }

    /**
     * 
     */
    public void suspendAllServices()
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     */
    public void resumeAllServices()
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param ipAddress
     */
    public void suspendService(String ipAddress)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @param ipAddress
     */
    public void resumeService(String ipAddress)
    {
        // TODO Auto-generated method stub
        
    }
}
