// Snmp4jTrapSender.java ----
// History: 2009/05/07 - Create
// 2009/05/21 - GETNEXT�Ή�
// 2009/08/15 - AgentService�Ή�
package jp.co.acroquest.tool.snmp.toolkit.request;

import java.io.IOException;
import java.net.InetAddress;

import jp.co.acroquest.tool.snmp.toolkit.AgentService;
import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.request.processor.RequestProcessor;
import jp.co.acroquest.tool.snmp.toolkit.request.processor.Snmp4jGetRequestProcessor;
import jp.co.acroquest.tool.snmp.toolkit.request.processor.Snmp4jSetRequestProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * SNMP4J�p��Request�����N���X�B
 * 
 * @author akiba
 */
public class Snmp4jRequestHandler implements RequestHandler, CommandResponder
{
    /** Get typeStr */
    private static final String PDU_TYPESTR_GET      = "GET";
    /** GetBulk typeStr */
    private static final String PDU_TYPESTR_GETBULK  = "GETBULK";
    /** GetNext typeStr */
    private static final String PDU_TYPESTR_GETNEXT  = "GETNEXT";
    /** Inform typeStr */
    private static final String PDU_TYPESTR_INFORM   = "INFORM";
    /** Report typeStr */
    private static final String PDU_TYPESTR_REPORT   = "REPORT";
    /** Response typeStr */
    private static final String PDU_TYPESTR_RESPONSE = "RESPONSE";
    /** Set typeStr */
    private static final String PDU_TYPESTR_SET      = "SET";
    /** Trap(Notification) typeStr */
    private static final String PDU_TYPESTR_TRAP     = "TRAP";
    /** V1Trap typeStr */
    private static final String PDU_TYPESTR_V1TRAP   = "V1TRAP";
    /** Unknown typeStr */
    private static final String PDU_TYPESTR_UNKNOWN  = "unknown";

    /** SNMP�X�^�b�N�B */
    private Snmp                snmp_;

    /** �ǂݍ��݃R�~���j�e�B�B */
    private String              roCommunity_;

    /** �������݃R�~���j�e�B�B */
    private String              rwCommunity_;

    /** RequestHandler������SNMP-Agent���̃T�[�r�X�B */
    private AgentService        agentService_;

    /** GET/GETNEXT �̗v������������v���Z�b�T�B */
    private RequestProcessor    getReqProcessor_;

    /** SET �̗v������������v���Z�b�T�B */
    private RequestProcessor    setReqProcessor_;

    /**
     * �f�t�H���g�R���X�g���N�^�B
     */
    public Snmp4jRequestHandler()
    {
    }

    /**
     * {@inheritDoc}
     */
    public void initHandler(AgentService agentService) throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(Snmp4jRequestHandler.class);
        try
        {
            // AgentService��ۑ�����
            this.agentService_ = agentService;

            Agent agent = agentService.getAgent();

            // �w�肳�ꂽAgent�̏����擾����
            String address = agent.getAddress();
            int port = agent.getSnmpPort();
            String roCommunity = agent.getRoCommunity();
            String rwCommunity = agent.getRwCommunity();
            log.info("agent: " + address + ":" + port + "@" + roCommunity + "/" + rwCommunity);

            // SNMP�X�^�b�N������������
            UdpAddress udpAddress = new UdpAddress(InetAddress.getByName(address), port);
            TransportMapping transportMapping = new DefaultUdpTransportMapping(udpAddress);
            this.snmp_ = new Snmp(transportMapping);
            this.snmp_.addCommandResponder(this);

            // �ǂݍ��݃R�~���j�e�B�̃f�t�H���g�`�F�b�N
            if (roCommunity == null)
            {
                this.roCommunity_ = DEFAULT_RO_COMMUNITY;
            }
            else
            {
                this.roCommunity_ = roCommunity;
            }

            // �������݃R�~���j�e�B�̃f�t�H���g�`�F�b�N
            if (rwCommunity == null)
            {
                this.rwCommunity_ = DEFAULT_RW_COMMUNITY;
            }
            else
            {
                this.rwCommunity_ = rwCommunity;
            }

            // RequestProcessor������������
            this.getReqProcessor_ = new Snmp4jGetRequestProcessor(agentService);
            this.setReqProcessor_ = new Snmp4jSetRequestProcessor(agentService);
        }
        catch (IOException exception)
        {
            log.error("IOException occured.", exception);
            throw new SnmpToolkitException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void startListening() throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(this.getClass());
        Agent agent = this.agentService_.getAgent();
        log.info("startListening: " + "address=[" + agent.getAddress() + "]");

        try
        {
            this.snmp_.listen();
            log.info("start : listening started." + " address=[" + agent.getAddress() + "]");
            System.out.println("start : listening at [" + agent.getAddress() + "]");
        }
        catch (IOException exception)
        {
            throw new SnmpToolkitException(exception);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stopListening() throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(this.getClass());
        Agent agent = this.agentService_.getAgent();
        if (agent == null)
        {
            log.warn("agent is null. stopListening is nothing to do.");
            return;
        }
        
        log.info("stopListening address=[" + agent.getAddress() + "]");
        
        try
        {
            this.snmp_.close();
            log.info("stop  : listening stopped." + " address=[" + agent.getAddress() + "]");
            System.out.println("stop  : agent [" + agent.getAddress() + "]");
        }
        catch (IOException exception)
        {
            throw new SnmpToolkitException(exception);
        }
    }

    /**
     * ��M����Request�Ŏw�肳�ꂽOID�ɑ΂���Response�𑗐M����B
     * 
     * @param event Request����M�������Ƃ�����SNMP4J�̃C�x���g�B
     */
    public void processPdu(CommandResponderEvent event)
    {
        Log log = LogFactory.getLog(this.getClass());

        // Event�����M����PDU���擾����
        PDU pdu = event.getPDU();
        log.info("processPdu : pdu=" + pdu);

        // �R�~���j�e�B�����擾����
        String securityName = new String(event.getSecurityName());

        // �����pPDU
        PDU retPdu = null;

        try
        {
            // GET, GETNEXT, SET�݂̂ɑΉ�����
            if (pdu.getType() == PDU.GET || pdu.getType() == PDU.GETNEXT)
            {
                // SNMP�R�~���j�e�B�����`�F�b�N����
                if (this.roCommunity_.equals(securityName) == false)
                {
                    log.warn("invalid request has received. community: " + securityName + " <> "
                            + this.roCommunity_);
                    return;
                }

                // ��M����PDU����������
                log.debug("PDU type [" + toTypeString(pdu.getType()) + "] is applicable.");
                event.setProcessed(true);
                retPdu = this.getReqProcessor_.processPdu(pdu);
            }
            else if (pdu.getType() == PDU.SET)
            {
                // SNMP�R�~���j�e�B�����`�F�b�N����
                if (rwCommunity_.equals(securityName) == false)
                {
                    log.warn("invalid request has received. community: " + securityName + " <> "
                            + this.rwCommunity_);
                    return;
                }

                // ��M����PDU����������
                log.debug("PDU type [" + toTypeString(pdu.getType()) + "] is applicable.");
                event.setProcessed(true);
                retPdu = this.setReqProcessor_.processPdu(pdu);
            }
            else
            {
                log.warn("PDU type [" + toTypeString(pdu.getType()) + "] is not applicable.");
                return;
            }
        }
        catch (SnmpToolkitException exception)
        {
            log.warn("exception occured in processPdu.", exception);
        }

        // ���M����ResponsePDU��������΁A�I������
        if (retPdu == null || retPdu.size() == 0)
        {
            log.info("there is nothing to respond.");
            return;
        }

        // ResponsePDU�ɕK�v�ȏ���ݒ肵�A���M����
        CommunityTarget target = new CommunityTarget();
        target.setAddress(event.getPeerAddress());
        int snmpVersion = getSnmpVersion(event.getSecurityLevel(), event.getSecurityModel());
        target.setVersion(snmpVersion);
        target.setCommunity(new OctetString(securityName));
        try
        {
            this.snmp_.send(retPdu, target);
            log.info("sent response: " + retPdu + ", target=" + target);
        }
        catch (IOException exception)
        {
            log.warn("Failed to send response.", exception);
        }
    }

    /**
     * PDU��type�l�ɑΉ����閼�̕�������擾����B
     * 
     * @param type PDU��type�l�B
     * @return PDU��type��\�����̕�����B
     */
    private String toTypeString(int type)
    {
        String typeStr;
        switch (type)
        {
        case PDU.GET:
            typeStr = PDU_TYPESTR_GET;
            break;
        case PDU.GETBULK:
            typeStr = PDU_TYPESTR_GETBULK;
            break;
        case PDU.GETNEXT:
            typeStr = PDU_TYPESTR_GETNEXT;
            break;
        case PDU.INFORM:
            typeStr = PDU_TYPESTR_INFORM;
            break;
        case PDU.REPORT:
            typeStr = PDU_TYPESTR_REPORT;
            break;
        case PDU.RESPONSE:
            typeStr = PDU_TYPESTR_RESPONSE;
            break;
        case PDU.SET:
            typeStr = PDU_TYPESTR_SET;
            break;
        case PDU.TRAP: // includes NOTIFICATION
            typeStr = PDU_TYPESTR_TRAP;
            break;
        case PDU.V1TRAP:
            typeStr = PDU_TYPESTR_V1TRAP;
            break;
        default:
            typeStr = PDU_TYPESTR_UNKNOWN;
            break;
        }

        return typeStr;
    }

    /**
     * �w�肳�ꂽ�Z�L�����e�B�p�����[�^����SNMP�o�[�W����������킷���l�ɕϊ�����B<br/>
     * 
     * 
     * @param securityLevel �Z�L�����e�B���x���ԍ��B
     * @param securityModel �Z�L�����e�B���f���ԍ��B
     * @return SNMP�o�[�W����������킷���l�B
     */
    private int getSnmpVersion(int securityLevel, int securityModel)
    {
        int snmpVersion = SnmpConstants.version1;
        switch (securityLevel)
        {
        case 0:
            snmpVersion = SnmpConstants.version1;
            break;
        case 1:
        	if (securityModel == 1)
        	{
                snmpVersion = SnmpConstants.version1;
        	}
        	else if (securityModel == 2)
        	{
                snmpVersion = SnmpConstants.version2c;
        	}
            break;
        case 3:
            snmpVersion = SnmpConstants.version3;
            break;
        default:
            break;
        }

        return snmpVersion;
    }

    /**
     * {@inheritDoc}
     */
    public void setAgent(Agent agent)
    {
        //this.agent_ = agent;
    }
    
    /**
     * {@inheritDoc}
     */
    public Agent getAgent()
    {
        return this.agentService_.getAgent();
    }
}
