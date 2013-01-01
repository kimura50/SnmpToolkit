// SnmpToolkitImpl.java ----
// History: 2004/03/23 - Create
// 2009/08/15 - AgentService�Ή�
package jp.co.acroquest.tool.snmp.toolkit;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpConfigItem;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpManager;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpManagerList;
import jp.co.acroquest.tool.snmp.toolkit.entity.TrapData;
import jp.co.acroquest.tool.snmp.toolkit.loader.SnmpConfiguration;
import jp.co.acroquest.tool.snmp.toolkit.trap.TrapSender;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SnmpToolkit�̃f�t�H���g�����B
 * 
 * @author akiba
 * @version 1.0
 */
public class SnmpToolkitImpl extends UnicastRemoteObject implements SnmpToolkit
{
    private static final long     serialVersionUID    = 4675606205704697125L;

    /** �o�[�W�����\�����s���I�v�V�����w�蕶����B */
    private static final String   OPT_SHOW_VERSION    = "-v";

    /** �ݒ�t�@�C���ւ̃p�X���w�肷��V�X�e���v���p�e�B�̃L�[�B */
    private static final String   CONFIG_PATH         = "snmptoolkit.configPath";

    /** �ݒ�t�@�C���ւ̃f�t�H���g�p�X�B */
    private static final String   DEFAULT_CONFIG_PATH = "../conf/config.xml";

    /** Agent�̃��C�t�T�C�N���Ǘ����s���I�u�W�F�N�g�B */
    private AgentLifecycleManager lcMgr_              = null;

    /** ���̃v���Z�X���N���������ԁB */
    private static long           upTime__            = System.currentTimeMillis();

    /**
     * ���̃v���Z�X���N�����Ă���o�߂������Ԃ��擾����B
     * 
     * @return ���̃v���Z�X���N�����Ă���o�߂�������(�~���b)�B
     */
    public static long getSysUpTime()
    {
        long sysUpTime = System.currentTimeMillis() - upTime__;
        return sysUpTime;
    }

    /**
     * SnmpToolkitImpl������������B
     * 
     * @throws RemoteException �������Ɏ��s�����ꍇ�B
     */
    public SnmpToolkitImpl(String agentDefFile) throws RemoteException
    {
        super();

        Log log = LogFactory.getLog(SnmpToolkitImpl.class);
        log.info("SnmpToolkit.<init>");

        try
        {
            this.lcMgr_ = AgentLifecycleManager.getInstance();
            SnmpConfiguration config = SnmpConfiguration.getInstance();
            SnmpConfigItem configItem = config.getSnmpConfigItem();
            String parentPath = configItem.getDataDir();
            this.lcMgr_.setDataDir(parentPath);
            try
            {
                this.lcMgr_.loadAgent(agentDefFile);
            }
            catch (IOException exception)
            {
                throw new SnmpToolkitException(exception);
            }
            this.lcMgr_.startAllAgents();
        }
        catch (SnmpToolkitException exception)
        {
            throw new RemoteException("An exception occured in initialize.", exception);
        }
    }

    /**
     * Trap�𑗐M����B
     * 
     * @param address Agent���w�肷��IP�A�h���X�B
     * @param trapData ���M����Trap�̃f�[�^�B
     * @throws RemoteException RMI�Ăяo���Ŕ���������O�B
     */
    public void sendTrap(String address, TrapData trapData)
        throws RemoteException
    {
        try
        {
            TrapSender sender = this.lcMgr_.getTrapSender(address);
            if (sender != null)
            {
                sender.sendTrap(trapData);
            }
        }
        catch (SnmpToolkitException exception)
        {
            Log log = LogFactory.getLog(SnmpToolkitImpl.class);
            log.error("Failed to send trap(s).", exception);
            throw new RemoteException("An exception occured.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void reloadMIBData() throws RemoteException
    {
        try
        {
            this.lcMgr_.suspendAllServices();
            this.lcMgr_.reloadMIBData();
            this.lcMgr_.resumeAllServices();
        }
        catch (SnmpToolkitException exception)
        {
            Log log = LogFactory.getLog(SnmpToolkitImpl.class);
            log.error("Failed to reload MIB data.", exception);
            throw new RemoteException("Failed to reload MIB data.", exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void reloadMIBData(String ipAddress) throws RemoteException
    {
        try
        {
            this.lcMgr_.suspendService(ipAddress);
            this.lcMgr_.reloadMIBData(ipAddress);
            this.lcMgr_.resumeService(ipAddress);
        }
        catch (SnmpToolkitException exception)
        {
            Log log = LogFactory.getLog(SnmpToolkitImpl.class);
            log.error("Failed to reload MIB data.", exception);
            throw new RemoteException("Failed to reload MIB data.", exception);
        }
    }

    /**
     * �v���O�����G���g���B
     * 
     * @param args �R�}���h���C�������BRMI�|�[�g�ԍ����w�肷��B
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        if (args.length < 1)
        {
            System.err.println("USAGE: SnmpToolkitImpl <data-file>");
            System.exit(1);
        }
        
        if (OPT_SHOW_VERSION.equals(args[0]) == true)
        {
            System.err.println("SNMPToolkit version " + Version.VERSION);
            System.exit(0);
        }
        
        // Agent��`�f�[�^�t�@�C��
        String dataFile = args[0];

        Log log = LogFactory.getLog(SnmpToolkitImpl.class);
        String logoStr = "-- SNMP Toolkit version " + Version.VERSION + " --";
        String lineStr = getLineStr(logoStr);
        log.info(lineStr);
        log.info(logoStr);
        log.info(lineStr);
        
        try
        {
            // �V�X�e���v���p�e�B����ݒ�t�@�C���̃p�X���擾����
            String configPath = System.getProperty(CONFIG_PATH, DEFAULT_CONFIG_PATH);
            log.debug("Configuration file path: " + configPath);

            // SnmpToolkit�̏�����
            SnmpConfiguration.initialize(configPath);
            // �R���t�B�O�̓��e���`�F�b�N����
            checkConfig();
            SnmpConfiguration config = SnmpConfiguration.getInstance();
            SnmpConfigItem configItem = config.getSnmpConfigItem();
            log.debug("SnmpConfiguration: " + configItem.toString());

            SnmpToolkit toolkit = new SnmpToolkitImpl(dataFile);
            log.info("SnmpToolkitImpl is created.");

            // ���W�X�g���̏�����
            int rmiPort = configItem.getRemotePort();
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            log.debug("Created local registry in port " + rmiPort);

            // SnmpToolkit�����W�X�g���Ƀo�C���h����
            InetAddress local = InetAddress.getLocalHost();
            String localAddr = local.getHostName();
            registry.bind("SnmpToolkit", toolkit);

            // ���O���R���\�[���o��
            String msg = "SnmpToolkit was started at [rmi://" + localAddr + ":" + rmiPort + "].";
            log.info(msg);
            System.out.println(msg);
        }
        catch (Exception exception)
        {
            log.error("Exception occured.", exception);
            System.err.println("ERROR: Failed to start SnmpToolkit. See log file for detail.");
            System.exit(1);
        }
    }

    /**
     * ���S�\���p�̋�؂��������𐶐�����B
     *
     * @param logoStr ���S�̕�����B
     * @return ��؂��������B
     */
    private static String getLineStr(String logoStr)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < logoStr.length(); i++)
        {
            sb.append('-');
        }
        return sb.toString();
    }

    /**
     * �R���t�B�O�t�@�C���̓��e���`�F�b�N����B
     * 
     * @throws SnmpToolkitException �`�F�b�N�ŃG���[�����������ꍇ�B
     */
    private static void checkConfig()
        throws SnmpToolkitException
    {
        SnmpConfiguration config = SnmpConfiguration.getInstance();
        SnmpConfigItem configItem = config.getSnmpConfigItem();
        
        // SNMP�}�l�[�W���̃��X�g�𑖍����A�A�h���X���������F������Ă��Ȃ����̂�����΃G���[�Ƃ���
        SnmpManagerList mgrList = configItem.getSnmpManagerList();
        SnmpManager[] managers = mgrList.getSnmpManagers();
        for (SnmpManager manager : managers)
        {
            String mgrAddress = manager.getManagerAddress();
            if (mgrAddress == null)
            {
                throw new SnmpToolkitException("Manager address is null.");
            }
        }
    }
}
