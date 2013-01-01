// RemoteTrapSender.java ----
// History: 2004/03/23 - Create
// 2009/07/25 - �p�b�P�[�W�ړ�(trap��command)
package jp.co.acroquest.tool.snmp.toolkit.command;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkit;
import jp.co.acroquest.tool.snmp.toolkit.entity.TrapData;
import jp.co.acroquest.tool.snmp.toolkit.entity.Traps;
import jp.co.acroquest.tool.snmp.toolkit.loader.TrapDataLoader;

/**
 * RMI��SnmpToolkit�ɐڑ����ATrap�𑗐M����R�}���h�B
 * 
 * @author akiba
 * @version 1.0
 */
public class RemoteTrapSender
{
    private static final String DEFAULT_SRV_NAME = "SnmpToolkit";

    /**
     * �v���O�����G���g���B
     * 
     * @param args �R�}���h���C�������B[0]=RMI�ڑ�URL�B[1]=Agent��IP�A�h���X�B[2]=Trap�f�[�^�t�@�C���B[3]=Trap���M�Ԋu(�I�v�V����)�B
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        if (args.length < 3)
        {
            System.err.println("USAGE: RemoteTrapSender <rmi-url> <address> <datafile> [<interval>]");
            System.exit(1);
        }

        String boundObjName = args[0] + "/" + DEFAULT_SRV_NAME;
        System.out.println("Connecting to " + boundObjName + " ...");
        SnmpToolkit toolkit = null;
        try
        {
            toolkit = (SnmpToolkit) Naming.lookup(boundObjName);
        }
        catch (NotBoundException exception)
        {
            System.err.println("Failed connecting to [" + boundObjName + "]. Couldn't find object.");
            System.exit(1);
        }
        System.out.println("Succeeded connecting to [" + boundObjName + "].");
        
        // Trap���M�C���^�[�o���̎擾
        long interval = 0L;
        if (args.length > 3)
        {
            try
            {
                interval = Long.parseLong(args[3], 10);
                if (interval < 0)
                {
                    System.err.println("ERROR: invalid interval: " + args[3] + " < 0");
                    System.exit(1);
                }
                System.out.println("Interval: " + interval + " msec.");
            }
            catch (NumberFormatException exception)
            {
                System.err.println("ERROR: invalid interval: " + args[3]);
                System.exit(1);
            }
        }

        try
        {
            // ���M����Trap�f�[�^�̓ǂݍ���
            TrapDataLoader loader = new TrapDataLoader();
            Traps traps = loader.load(args[2]);
            TrapData[] trapArray = traps.getAllTrapData();
            for (int index = 0; index < trapArray.length; index++)
            {
                // �擾��������Trap�𑗐M����
                TrapData trapData = trapArray[index];
                toolkit.sendTrap(args[1], trapData);
                System.out.println("Trap[" + index + "] was sent.");
                
                // �E�F�C�g������(1ms�ȏ�̏ꍇ)
                if (interval > 0)
                {
                    Thread.sleep(interval);
                }
            }
        }
        catch (RemoteException exception)
        {
            System.err.println("Failed to send trap(s).);");
            System.err.println(exception.getLocalizedMessage());
            System.err.println();
            System.err.println("See snmptoolkit.log for more detail.");
        }
    }
}
