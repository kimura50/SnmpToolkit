//AbstractSnmpVariableHelper.java ----
// History: 2009/05/20 - Create
package jp.co.acroquest.tool.snmp.toolkit.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.acroquest.tool.snmp.toolkit.SnmpEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SNMP�̒l���^�w�蕶����ɂ��������ĕϊ�����w���p�[�N���X�̒��ۃN���X�B<br/>
 * �e�X�^�b�N�ɋ��ʂ̏������������Ă���B
 * 
 * @author akiba
 */
public abstract class AbstractSnmpVariableHelper implements SnmpVariableHelper
{
    /** ���t��͗p�t�H�[�}�b�^�B */
    protected static final SimpleDateFormat PARSER =
        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    /**
     * �w���p�[�N���X������������B
     */
    protected AbstractSnmpVariableHelper()
    {
    }
    
    /**
     * Object�̌^����AAsnObject�𐶐�����B
     * 
     * @param value AsnObject�ɕϊ�������Object�B
     * @param type  Object�̌^��\��������B
     * @return AsnObject�B
     */
    public Object createAsnObject(Object value, String type)
        throws SnmpEncodingException
    {
        Log log = LogFactory.getLog(SnmpVariableHelper.class);
        Object obj = null;
        
        if (type.equals(OCTET_STRING))
        {
            // OctetString�`���̏ꍇ�́A���̂܂�String�Ƃ��Ĉ���
            String strObj = "";
            if (value != null)
            {
                strObj = (String) value;
            }
            log.debug("OCTET-STRING=" + strObj);
            obj = createOctetString(strObj);
        }
        else
        if (type.equals(STRING))
        {
            String strObj = "";
            if (value != null)
            {
                strObj = (String) value;
            }
            log.debug("STRING=" + strObj);
            obj = createString(strObj);
        }
        else
        if (type.equals(OBJECT_ID))
        {
            // ObjectID�`���̏ꍇ�́A���̂܂�OID�ɕϊ�����
            String oidStr = (String) value;
            log.debug("OBJECT-ID=" + oidStr);
            obj = createObjectId(oidStr);
        }
        else
        if (type.equals(HEX_STRING))
        {
            // Hex-String�`���̏ꍇ�́A':' �ŋ�؂�ꂽ16�i�������byte[]�ɕϊ�����
            byte[]       bytes;
            if (value != null)
            {
                String   strObj   = (String) value;
                String[] strArray = strObj.split(":");
                
                bytes = new byte[strArray.length];
                StringBuffer buf   = new StringBuffer(strArray.length * 2);
                try
                {
                    for (int index = 0; index < strArray.length; index ++)
                    {
                        // byte�ɕϊ�����O�ɁA��xint��ʂ����Ƃ�0x80�ȏ�̒l������
                        int intValue = Integer.parseInt(strArray[index], 16);
                        bytes[index] = (byte) (intValue & 0x000000FF);
                        
                        if (log.isDebugEnabled())
                        {
                            buf.append(toHexString(bytes[index]) + ":");
                        }
                    }
                }
                catch (NumberFormatException exception)
                {
                    // Hex���l�Ƃ��ĔF���ł��Ȃ��ꍇ�́A��O���X���[����
                    throw new SnmpEncodingException("cannot recognize value as numeric.", exception);
                }
                log.debug("HEX-STRING=" + buf.toString());
            }
            else
            {
                bytes = new byte[0];
                log.debug("HEX-STRING=");
            }
            obj = createOctetString(bytes);
        }
        else
        if (type.equals(TIMETICKS))
        {
            // Timeticks�`���̏ꍇ�́A
            Date dateObj = null;
            if (value instanceof Date)
            {
                dateObj = (Date) value;
            }
            else
            if (value instanceof String)
            {
                String strObj = (String) value;
                if ("NOW".equalsIgnoreCase(strObj))
                {
                    dateObj = new Date();
                }
                else
                {
                    try
                    {
                        dateObj = PARSER.parse(strObj);
                    }
                    catch (ParseException exception)
                    {
                        throw new SnmpEncodingException("failed to parse date-time.", exception);
                    }
                }
            }
            else
            {
                // Integer�AString�ȊO�͗�O���X���[����
                String actualType = value.getClass().getName();
                throw new SnmpEncodingException("invalid object type: excepcted=Date, actual=" +
                                                actualType);
            }
            
            long ticks = dateObj.getTime();
            log.debug("TIMETICKS=" + ticks);
            obj = createTimeticks(ticks);
        }
        else
        if (type.equals(INTEGER))
        {
            // Integer�`���́A(java.lang)Integer��String�̂ݗL��
            Integer intObj = null;
            if (value instanceof Integer)
            {
                // (java.lang)Integer�̏ꍇ
                intObj = (Integer) value;
            }
            else
            if (value instanceof String)
            {
                // String�̏ꍇ
                intObj = Integer.valueOf((String) value);
            }
            else
            {
                // Integer�AString�ȊO�͗�O���X���[����
                String actualType = value.getClass().getName();
                throw new SnmpEncodingException("invalid object type: expected=Integer, actual=" +
                                                actualType);
            }
            log.debug("INTEGER=" + intObj);
            obj = createInteger(intObj.intValue());
        }
        else
        if (type.equals(IPADDRESS))
        {
            // IpAddress�`���́AInetAddress�I�u�W�F�N�g�Ƃ��Ĉ���
            InetAddress addrObj;
            try
            {
                addrObj = InetAddress.getByName((String) value);
                log.debug("IPADDRESS=" + addrObj.getHostAddress());
                obj = createIpAddress(addrObj);
            }
            catch (UnknownHostException exception)
            {
                // IpAddress�I�u�W�F�N�g�ɕϊ��ł��Ȃ������ꍇ��
                // �Ǝ��̗�O�ɒu�������ăX���[����
                throw new SnmpEncodingException("Invalid address.", exception);
            }
        }
        else
        {
            throw new SnmpEncodingException("Unknown type: " + type);
        }
        
        return obj;
    }
    
    /**
     * 1��������byte�l��HEX������ɕϊ�����B(���d)
     * 
     * @param ch HEX������ɕϊ�����byte�l�B
     * @return HEX������B
     */
    private String toHexString(byte ch)
    {
        int tmpCh = ch & 0x000000ff;
        return Integer.toHexString(tmpCh);
    }
}
