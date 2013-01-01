//SnmpToolkitException.java ----
// History: 2004/11/22 - Create

package jp.co.acroquest.tool.snmp.toolkit;

/**
 * SnmpToolkit�̓����ŋ��ʓI�Ɏ��񂷗�O�B
 * 
 * @author akiba
 * @version 1.00
 */
public class SnmpToolkitException extends Exception
{
    private static final long serialVersionUID = 930082892844855194L;

    /**
     * SnmpToolkitException�𐶐�����B
     */
    public SnmpToolkitException()
    {
        super();
    }

    /**
     * SnmpToolkitException�𐶐�����B
     * 
     * @param cause ���̗�O�������������R�B
     */
    public SnmpToolkitException(String cause)
    {
        super(cause);
    }

    /**
     * SnmpToolkitException�𐶐�����B
     * 
     * @param superException ���̗�O�ɘA���������O�I�u�W�F�N�g�B
     */
    public SnmpToolkitException(Throwable superException)
    {
        super(superException);
    }

    /**
     * SnmpToolkitException�𐶐�����B
     * 
     * @param cause ���̗�O���������闝�R�B
     * @param superException ���̗�O�ɘA���������O�I�u�W�F�N�g�B
     */
    public SnmpToolkitException(String cause, Throwable superException)
    {
        super(cause, superException);
    }
}
