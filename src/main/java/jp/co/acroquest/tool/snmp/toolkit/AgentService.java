//AgentService.java ----
// History: 2009/08/15 - Create
package jp.co.acroquest.tool.snmp.toolkit;

import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.request.RequestHandler;
import jp.co.acroquest.tool.snmp.toolkit.stack.SnmpStackFactory;
import jp.co.acroquest.tool.snmp.toolkit.trap.TrapSender;

/**
 * �P���Agent�̋N���E�I�����Ǘ�����B
 * 
 * @author akiba
 */
public class AgentService
{
    /** ����AgentService���Ǘ�����Agent�I�u�W�F�N�g�B */
    private Agent agent_;
    
    /** ����AgentService������RequestHandler�B */
    private RequestHandler reqHandler_;
    
    /** ����AgentService�����p����TrapSender�B */
    private TrapSender trapSender_;
    
    /**
     * AgentService������������B
     * 
     * @param agent ����AgentService������Agent�I�u�W�F�N�g�B
     */
    public AgentService(Agent agent)
    {
        this.agent_ = agent;
    }

    /**
     * AgentService���J�n����B
     * 
     * @throws SnmpToolkitException AgentService�̊J�n�Ɏ��s�����ꍇ�B
     */
    public void startService() throws SnmpToolkitException
    {
        try
        {
            SnmpStackFactory factory = SnmpStackFactory.getFactory();
            this.reqHandler_ = factory.createRequestHandler(this);
            this.trapSender_ = factory.createTrapSender(this.agent_);
            
            this.reqHandler_.startListening();
        }
        catch (ClassNotFoundException exception)
        {
            throw new SnmpToolkitException(exception);
        }
        catch (InstantiationException exception)
        {
            throw new SnmpToolkitException(exception);
        }
        catch (IllegalAccessException exception)
        {
            throw new SnmpToolkitException(exception);
        }
    }
    
    /**
     * AgentService���~����B
     * 
     * @throws SnmpToolkitException AgentService�̒�~�Ɏ��s�����ꍇ�B
     */
    public void stopService() throws SnmpToolkitException
    {
        this.reqHandler_.stopListening();
    }
    
    /**
     * AgentService�̓�����ꎞ�I�ɒ�~����B
     * 
     * @throws SnmpToolkitException AgentService�̈ꎞ��~�Ɏ��s�����ꍇ�B
     */
    public void suspendService() throws SnmpToolkitException
    {
        //this.reqHandler_.suspend();
    }
    
    /**
     * �ꎞ��~����AgentService���ĊJ����B
     * 
     * @throws SnmpToolkitException AgentService�̍ĊJ�Ɏ��s�����ꍇ�B
     */
    public void resumeService() throws SnmpToolkitException
    {
        //this.reqHandler_.resume();
    }
    
    /**
     * �V����Agent�I�u�W�F�N�g��ݒ肵�AMIB�f�[�^���X�V����B
     * 
     * @param agent �V����Agent�I�u�W�F�N�g�B
     */
    public void reloadAgent(Agent agent)
    {
        this.agent_ = agent;
        //this.reqHandler_.setAgent(agent);
    }
    
    /**
     * ����AgentService������Agent�I�u�W�F�N�g���擾����B
     * 
     * @return Agent�I�u�W�F�N�g�B
     */
    public Agent getAgent()
    {
        return this.agent_;
    }
    
    /**
     * ����AgentService���g�p����TrapSender���擾����B
     * 
     * @return TrapSender�I�u�W�F�N�g�B
     */
    public TrapSender getTrapSender()
    {
        return this.trapSender_;
    }
}
