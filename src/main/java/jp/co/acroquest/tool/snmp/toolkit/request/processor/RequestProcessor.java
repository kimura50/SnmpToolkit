//RequestProcessor.java ----
// History: 2010/02/03 - Create
package jp.co.acroquest.tool.snmp.toolkit.request.processor;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;

import org.snmp4j.PDU;

/**
 * SNMP�̗v��(PDU)����������v���Z�b�T�N���X�̊��C���^�t�F�[�X�B
 * 
 * @author akiba
 */
public interface RequestProcessor
{
    /**
     * ��M����PDU����������B
     * 
     * @param pdu ��M����PDU�B
     * @return �����������ʐ�������A�ԐM����PDU�B
     * @throws SnmpToolkitException PDU�̏������ɃG���[�����������ꍇ�B
     */
    PDU processPdu(PDU pdu)
        throws SnmpToolkitException;
}
