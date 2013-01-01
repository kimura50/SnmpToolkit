//Snmp4jFactory.java ----
// History: 2005/02/07 - Create
//          2009/05/07 - SnmpStackFactory�𓱓�
//          2009/08/15 - AgentService�Ή�
package jp.co.acroquest.tool.snmp.toolkit.stack;

import java.io.IOException;
import java.net.InetAddress;

import jp.co.acroquest.tool.snmp.toolkit.AgentService;
import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.request.RequestHandler;
import jp.co.acroquest.tool.snmp.toolkit.request.Snmp4jRequestHandler;
import jp.co.acroquest.tool.snmp.toolkit.trap.Snmp4jTrapSender;
import jp.co.acroquest.tool.snmp.toolkit.trap.TrapSender;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * Snmp4j�̃I�u�W�F�N�g�����p�t�@�N�g���N���X�B
 * 
 * @author akiba
 * @version 1.0
 */
public class Snmp4jFactory extends SnmpStackFactory
{
    /**
     * {@inheritDoc}
     */
    public TrapSender createTrapSender(Agent agent)
        throws SnmpToolkitException
    {
        String address       = agent.getAddress();
        String host          = getHostIpAddress(address);
        String trapCommunity = agent.getTrapCommunity();
        
        TrapSender sender = new Snmp4jTrapSender(host);
        
        // ��������TrapSender�ɏ�������ݒ肷��
        sender.setCommunity(trapCommunity);
        
        return sender;
    }
    
    /**
     * �z�X�g�\�L����IP�A�h���X�𒊏o����B
     * 
     * @param address �z�X�g�̕\�L�B"�z�X�g��:�|�[�g�ԍ�"�̌`�������҂��Ă���B
     * @return �z�X�g�̕\�L���璊�o����IP�A�h���X�B�|�[�g�ԍ����Ȃ���Γ��͂��̂܂܂�Ԃ��B
     */
    private String getHostIpAddress(String address)
    {
        String host;
        //TODO: TCP/UDP�̐ؑւ��\�ɂ���
        String hostAndPort = address.replace("udp://", "");
        int portIndex = hostAndPort.indexOf(':');
        if (portIndex < 0)
        {
            host = hostAndPort;
        }
        else
        {
            host = hostAndPort.substring(0, portIndex);
        }
        return host;
    }

    /**
     * {@inheritDoc}
     */
    public RequestHandler createRequestHandler(AgentService agentService)
        throws SnmpToolkitException
    {
        RequestHandler handler = new Snmp4jRequestHandler();
        handler.initHandler(agentService);
        
        return handler;
    }
    
    /**
     * Snmp�I�u�W�F�N�g�𐶐�����B
     * 
     * @param host �o�C���h��z�X�g���B
     * @param port �o�C���h��|�[�g�ԍ��B
     * @return Snmp�I�u�W�F�N�g�B
     * @throws IOException Snmp�I�u�W�F�N�g�̏������Ɏ��s�����ꍇ�B
     */
    public static Snmp createSnmp(String host, int port)
        throws IOException
    {
        //TODO: TCP/UDP�̐ؑւ��\�ɂ���
        UdpAddress udpAddress = new UdpAddress(InetAddress.getByName(host), port);
        TransportMapping transportMapping = new DefaultUdpTransportMapping(udpAddress); 
        Snmp snmp = new Snmp(transportMapping);
        
        return snmp;
    }
    
    /**
     * Trap���M�p��Snmp�I�u�W�F�N�g�𐶐�����B
     * 
     * @param host �o�C���h��z�X�g���B
     * @return Trap���M�p��Snmp�I�u�W�F�N�g�B
     * @throws IOException Snmp�I�u�W�F�N�g�̏������Ɏ��s�����ꍇ�B
     */
    public static Snmp createSnmp(String host)
        throws IOException
    {
        //TODO �����|�[�g�̊m�ۂ͉\���H
        Snmp snmp = createSnmp(host, SnmpStackFactory.DEFAULT_TRAP_PORT);
        return snmp;
    }
}
