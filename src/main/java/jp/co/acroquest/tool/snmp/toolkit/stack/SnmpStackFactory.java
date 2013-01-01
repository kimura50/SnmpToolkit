//SnmpStackFactory.java ----
// History: 2009/05/07 - Create
//          2009/08/15 - AgentService�Ή�
package jp.co.acroquest.tool.snmp.toolkit.stack;

import jp.co.acroquest.tool.snmp.toolkit.AgentService;
import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.request.RequestHandler;
import jp.co.acroquest.tool.snmp.toolkit.trap.TrapSender;

/**
 * SNMP�X�^�b�N�ɋ��ʂ��鏈���I�u�W�F�N�g�����t�@�N�g���B
 * 
 * @author akiba
 */
public abstract class SnmpStackFactory
{
    /** ���̃t�@�N�g���̃C���X�^���X�B */
    private static SnmpStackFactory factory__;
    
    /** �t�@�N�g���N���X�����w�肷��v���p�e�B���B */
    private static final String PROP_STACK_FACTORY = "snmptoolkit.stackFactory";
    
    /** �f�t�H���g�̃t�@�N�g���N���X���B */
    private static final String DEF_STACK_FACTORY  = "jp.co.acroquest.tool.snmp.toolkit.stack.Snmp4jFactory";
    
    /** �f�t�H���g��Trap�|�[�g�ԍ��B */
    protected static final int  DEFAULT_TRAP_PORT  = 162;
    
    /**
     * �t�@�N�g���𐶐�����B
     */
    protected SnmpStackFactory()
    {
    }
    
    /**
     * �t�@�N�g���I�u�W�F�N�g���擾����B<br/>
     * �t�@�N�g���N���X�́A�v���p�e�B<code>snmptoolkit.stackFactory</code>�Őݒ�\�B
     * 
     * @return ���������t�@�N�g���I�u�W�F�N�g�B
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public static final SnmpStackFactory getFactory()
        throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        if (factory__ == null)
        {
            String factoryClassName = System.getProperty(PROP_STACK_FACTORY, DEF_STACK_FACTORY);
            Class<?> factoryClass = Class.forName(factoryClassName);
            factory__ = (SnmpStackFactory) factoryClass.newInstance();
        }
        
        return factory__;
    }
    
    /**
     * TrapSender�I�u�W�F�N�g�𐶐�����B
     * 
     * @param agent TrapSender����������Agent�̃f�[�^�B
     * @return TrapSender�I�u�W�F�N�g�B
     * @throws SnmpToolkitException
     */
    public abstract TrapSender createTrapSender(Agent agent)
        throws SnmpToolkitException;
    
    /**
     * RequestHandler�I�u�W�F�N�g�𐶐�����B
     * 
     * @param agent RequestHandler����������Agent�����T�[�r�X�B
     * @return RequestHandler�I�u�W�F�N�g�B
     * @throws SnmpToolkitException
     */
    public abstract RequestHandler createRequestHandler(AgentService agentService)
        throws SnmpToolkitException;
}
