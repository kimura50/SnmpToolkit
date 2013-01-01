//TrapSenderFactory.java ----
// History: 2005/02/07 - Create
//          2009/05/07 - SnmpStackFactory�𓱓�
//          2009/08/15 - AgentService�Ή�
package jp.co.acroquest.tool.snmp.toolkit.trap;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.stack.SnmpStackFactory;

/**
 * TrapSender�C���X�^���X������������ׂ̃t�@�N�g���N���X�B<br>
 * �񋟂��郁�\�b�h��static�ł���A���̃N���X���̂̃C���X�^���X�����͕s�v�ł���B
 * 
 * @author akiba
 * @version 1.0
 */
public class TrapSenderFactory
{
    /**
     * TrapSender�C���X�^���X������������B
     * 
     * @param agent TrapSender����������Agent�̃f�[�^�B
     * @return ��������TrapSender�C���X�^���X�B
     * @throws SnmpToolkitException
     */
    public static TrapSender createTrapSender(Agent agent)
        throws SnmpToolkitException
    {
        TrapSender sender;
        try
        {
            SnmpStackFactory factory = SnmpStackFactory.getFactory();
            sender = factory.createTrapSender(agent);
        }
        catch (Exception exception)
        {
            throw new SnmpToolkitException(exception);
        }
        
        return sender;
    }

    /**
     * �C���X�^���X���͕s�v�ׁ̈A�R���X�g���N�^��private������B
     */
    private TrapSenderFactory()
    {
    }
}
