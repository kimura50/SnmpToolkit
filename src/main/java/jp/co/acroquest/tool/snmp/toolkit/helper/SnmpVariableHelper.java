//SnmpVariableHelper.java ----
// History: 2009/05/20 - Create
package jp.co.acroquest.tool.snmp.toolkit.helper;

import java.net.InetAddress;

/**
 * SNMP�̒l���^�w�蕶����ɂ��������ĕϊ�����w���p�[�N���X�̊��C���^�t�F�[�X�B<br/>
 * �l�X��SNMP�X�^�b�N���C�u�����𗘗p�ł���悤�A���ʂ̃C���^�t�F�[�X���K�肵�Ă���B
 * 
 * @author akiba
 */
public interface SnmpVariableHelper
{
    /** Integer */
    static final String INTEGER      = "integer";

    /** Octet-String */
    static final String OCTET_STRING = "octets";

    /** Hex-String */
    static final String HEX_STRING   = "hex";

    /** Timeticks */
    static final String TIMETICKS    = "timeticks";

    /** IP-Address */
    static final String IPADDRESS    = "ipaddress";
    
    /** Object-ID */
    static final String OBJECT_ID    = "object-id";
    
    /** String */
    static final String STRING       = "string";

    /**
     * �^����ꂽObject�ƃf�[�^�^������AsnObject�I�u�W�F�N�g���쐬����B
     * 
     * @param obj �ϊ��ΏۃI�u�W�F�N�g�B
     * @param typeStr �I�u�W�F�N�g�̃f�[�^�^���B�w��\�Ȍ^���͖{�C���^�t�F�[�X�̒萔�Ƃ��Đ錾���Ă�����̂Ƃ���B
     * @return �ϊ����AsnObject�I�u�W�F�N�g�B
     */
    Object createAsnObject(Object obj, String typeStr);
    
    /**
     * OctetString��\������Stack��̃I�u�W�F�N�g�𐶐�����B
     * 
     * @param str �ϊ����ƂȂ�String�I�u�W�F�N�g�B
     * @return �ϊ�����OctetString�I�u�W�F�N�g�B
     */
    Object createOctetString(String str);
    
    /**
     * OctetString��\������Stack��̃I�u�W�F�N�g�𐶐�����B
     * 
     * @param chars �ϊ����ƂȂ�char�z��B
     * @return �ϊ�����OctetString�I�u�W�F�N�g�B
     */
    Object createOctetString(char[] chars);
    
    /**
     * OctetString��\������Stack��̃I�u�W�F�N�g�𐶐�����B
     * 
     * @param bytes �ϊ����ƂȂ�byte�z��B
     * @return �ϊ�����OctetString�I�u�W�F�N�g�B
     */
    Object createOctetString(byte[] bytes);
    
    
    /**
     * String��\������Stack��̃I�u�W�F�N�g�𐶐�����B
     * 
     * @param string �ϊ����ƂȂ�String������B
     * @return �ϊ�����OctetString�I�u�W�F�N�g�B
     */
    Object createString(String string);

    /**
     * IpAddress��\������Stack��̃I�u�W�F�N�g�𐶐�����B
     * 
     * @param addr �ϊ����ƂȂ�InetAddress�I�u�W�F�N�g�B
     * @return �ϊ�����IpAddress�I�u�W�F�N�g�B
     */
    Object createIpAddress(InetAddress addr);

    /**
     * Integer��\������Stack��̃I�u�W�F�N�g�𐶐�����B
     * 
     * @param intValue �ϊ����ƂȂ�int�l�B
     * @return �ϊ�����Integer�I�u�W�F�N�g�B
     */
    Object createInteger(int intValue);

    /**
     * Timeticks��\������Stack��̃I�u�W�F�N�g�𐶐�����B
     * 
     * @param ticks �ϊ����ƂȂ�tick�l�B
     * @return �ϊ�����Timeticks�I�u�W�F�N�g�B
     */
    Object createTimeticks(long ticks);
    
    /**
     * ObjectID��\������Stack��̃I�u�W�F�N�g�𐶐�����B
     * 
     * @param oid �ϊ����ƂȂ�ObjectID�l�B
     * @return �ϊ�����ObjectID�I�u�W�F�N�g�B
     */
    Object createObjectId(String oid);

    /**
     * Stack��̃I�u�W�F�N�g�𕶎���`���ɕϊ�����B
     * 
     * @param type Agent�̌^�B
     * @param reqVar Stack��̃I�u�W�F�N�g�B
     * @return �ϊ�����������B
     */
    String convertToString(String type, Object reqVar);
}
