//Snmp4jGetRequestProcessor.java ----
// History: 2010/02/03 - Create
package jp.co.acroquest.tool.snmp.toolkit.request.processor;

import java.util.List;

import jp.co.acroquest.tool.snmp.toolkit.AgentService;
import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpVarbind;
import jp.co.acroquest.tool.snmp.toolkit.helper.Snmp4jVariableHelper;
import jp.co.acroquest.tool.snmp.toolkit.helper.SnmpVariableHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.snmp4j.PDU;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

/**
 * GET/GETNEXT ����������RequestProcessor�B
 * 
 * @author akiba
 */
public class Snmp4jGetRequestProcessor implements RequestProcessor
{
    /** RequestHandler������SNMP-Agent���̃T�[�r�X�B */
    private AgentService agentService_;

    /**
     * Snmp4jGetRequestProcessor������������B
     * 
     * @param agentService RequestHandler������SNMP-Agent���̃T�[�r�X�B
     */
    public Snmp4jGetRequestProcessor(AgentService agentService)
    {
        this.agentService_ = agentService;
    }

    /**
     * {@inheritDoc}
     */
    public PDU processPdu(PDU pdu) throws SnmpToolkitException
    {
        Log log = LogFactory.getLog(this.getClass());

        // �����p��PDU�𐶐�����
        PDU retPdu = new PDU();
        retPdu.setType(PDU.RESPONSE);

        // �v���̃��N�G�X�gID���擾���A����PDU�ɃZ�b�g����
        Integer32 requestID = pdu.getRequestID();
        retPdu.setRequestID(requestID);

        // SNMP4J�p�̌^�ϊ��I�u�W�F�N�g
        SnmpVariableHelper varHelper = new Snmp4jVariableHelper();

        // AgentService����Agent���擾����
        Agent agent = this.agentService_.getAgent();

        // �v��PDU��Varbind�𑖍�����
        int varCount = 0;
        List<?> reqVarbinds = pdu.getVariableBindings();
        for (Object reqVarbindObj : reqVarbinds)
        {
            varCount++;

            // �v������Ă���OID���擾����
            VariableBinding reqVarbind = (VariableBinding) reqVarbindObj;
            OID oid = reqVarbind.getOid();
            
            // GETNEXT�łȂ��ꍇ�́A�w�肳�ꂽOID���̂��̂��擾���悤�Ƃ���
            // GETNEXT�̏ꍇ�́A�w�肳�ꂽOID�z���ōł��߂��I�u�W�F�N�g��T��
            boolean exact = (pdu.getType() != PDU.GETNEXT);
            SnmpVarbind foundVarbind = agent.findObject(oid.toString(), exact);
            if (foundVarbind == null)
            {
                // Varbind��������Ȃ������ꍇ��noSuchName��Ԃ�
                log.warn("varbind is not found. oid=" + oid.toString());

                retPdu.setErrorStatus(SnmpConstants.SNMP_ERROR_NO_SUCH_NAME);
                retPdu.setErrorIndex(varCount);

                Variable retObject = new Null();
                VariableBinding retVarbind = new VariableBinding(oid, retObject);
                retPdu.add(retVarbind);
                break;
            }

            // READ-WRITE�łȂ���΃G���[������Ԃ�
            String accessibility = foundVarbind.getAccessibility();
            if (accessibility.equals(SnmpVarbind.ACCESSIBILITY_NOT_ACCESSIBLE) == true)
            {
                log.warn("varbind is not accessible. accessibility=" + accessibility);
                
                retPdu.setErrorStatus(SnmpConstants.SNMP_ERROR_NO_ACCESS);
                retPdu.setErrorIndex(varCount);
                
                Variable retObject = new Null();
                VariableBinding retVarbind = new VariableBinding(oid, retObject);
                retPdu.add(retVarbind);
                break;
            }

            try
            {
                // ����̉�����Ԃ�
                retPdu.setErrorStatus(SnmpConstants.SNMP_ERROR_SUCCESS);
                retPdu.setErrorIndex(0);

                log.debug("varbind is found: " + foundVarbind.toString());
                String typeStr = foundVarbind.getType();
                Object retValueObj = foundVarbind.getValue();
                Variable retObject = (Variable) varHelper.createAsnObject(retValueObj, typeStr);

                OID retOID = new OID(foundVarbind.getOid());
                VariableBinding retVarbind = new VariableBinding(retOID, retObject);
                retPdu.add(retVarbind);
            }
            catch (Exception exception)
            {
                log.warn("exception occured", exception);
                
                // ���m�̃G���[����������PDU���쐬����
                retPdu.setErrorStatus(SnmpConstants.SNMP_ERROR_GENERAL_ERROR);
                retPdu.setErrorIndex(varCount);
                
                if (foundVarbind != null)
                {
                    OID retOID = new OID(foundVarbind.getOid());
                    Variable retObject = new Null();
                    VariableBinding retVarbind = new VariableBinding(retOID, retObject);
                    retPdu.add(retVarbind);
                }
            }
        }
        return retPdu;
    }
}
