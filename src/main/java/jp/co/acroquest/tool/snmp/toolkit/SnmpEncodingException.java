// SnmpEncodingException.java ----
// History: 2004/03/21 - Create
package jp.co.acroquest.tool.snmp.toolkit;

/**
 * SNMP Encoding�����ŃG���[�������������Ƃ�\����O�B
 * 
 * @author akiba
 * @version 1.0
 */
public class SnmpEncodingException extends RuntimeException
{
    private static final long serialVersionUID = -9220149928704551626L;

    /**
     * SnmpEncodingException�𐶐�����B
     */
    public SnmpEncodingException()
    {
        super();
    }

    /**
     * SnmpEncodingException�𐶐�����B
     * 
     * @param cause ���̗�O�������������R�B
     */
    public SnmpEncodingException(String cause)
    {
        super(cause);
    }

    /**
     * SnmpEncodingException�𐶐�����B
     * 
     * @param superException ���̗�O�ɘA���������O�I�u�W�F�N�g�B
     */
    public SnmpEncodingException(Throwable superException)
    {
        super(superException);
    }

    /**
     * SnmpEncodingException�𐶐�����B
     * 
     * @param cause ���̗�O�������������R�B
     * @param superException ���̗�O�ɘA���������O�I�u�W�F�N�g�B
     */
    public SnmpEncodingException(String cause, Throwable superException)
    {
        super(cause, superException);
    }
}
