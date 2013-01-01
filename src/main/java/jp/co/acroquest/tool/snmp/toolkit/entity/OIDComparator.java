//OIDComparator.java ----
// History: 2009/11/22 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * OID���Ƀ\�[�g���s���R���p���[�^�B
 * 
 * @author akiba
 */
public class OIDComparator implements Comparator<String>
{
    /** �������傫���Ɣ��肵���ꍇ�B */
    private static final int LEFT   = 1;

    /** �E�����傫���Ɣ��肵���ꍇ�B */
    private static final int RIGHT  = -1;

    /** �o���������ł���Ɣ��肵���ꍇ�B */
    private static final int EQUALS = 0;

    /** OID�𕪊�����f���~�^�����B */
    private static final int OID_DELIM = '.';

    /**
     * {@inheritDoc}
     */
    public int compare(String str1, String str2)
    {
        if (str1 == null)
        {
            if (str2 == null)
            {
                // �o���Ƃ�null�̏ꍇ�͓��l�Ɣ��肷��
                return EQUALS;
            }
            else
            {
                // ����������null�̏ꍇ�́A�E�����Ɣ��肷��
                return RIGHT;
            }
        }

        // �E��������null�̏ꍇ�́A�������Ɣ��肷��
        if (str2 == null)
        {
            return LEFT;
        }

        // �ȉ��͑o���Ƃ�null�łȂ��ꍇ

        // OID�������digit���ɕ�������
        Integer[] leftArray = this.splitOid(str1);
        Integer[] rightArray = this.splitOid(str2);

        for (int cnt = 0; cnt < leftArray.length; cnt++)
        {
            // �r���܂œ����ŁA�����̕��������ꍇ
            if (cnt >= rightArray.length)
            {
                return LEFT;
            }

            // �r���̊K�w���قȂ�l�ɂȂ��Ă���ꍇ�́A���̊K�w�̍��ŏ��������߂�
            if (leftArray[cnt] < rightArray[cnt])
            {
                return RIGHT;
            }
            
            if (rightArray[cnt] < leftArray[cnt])
            {
                return LEFT;
            }
        }
        
        // �r���܂œ����ŁA�E���̕��������ꍇ
        if (leftArray.length < rightArray.length)
        {
            return RIGHT;
        }

        // �S�Ă̊K�w�œ����l�������ꍇ�͓��l�Ƃ݂Ȃ�
        return EQUALS;
    }
    
    /**
     * OID�������Integer�z��ɕ�������B<br/>
     * ���̃��\�b�h�́A<code>String#split()</code>�����p�t�H�[�}���X�̖����������邽�߂ɓ������ꂽ�B<br/>
     * 
     * @param str null�ł͂Ȃ�OID������B<br/>
     *            ���digit�����o�����悤��OID��^�����NumberFormatException����������B
     *            ��̓I�ɂ́A�擪���s���I�h�Ŏn�܂�^�������s���I�h�ŏI���^�r���Ƀs���I�h���A��������̂�NG�B
     * @return �������ꂽInteger�z��B
     */
    private Integer[] splitOid(String str)
    {
        List<Integer> list = new ArrayList<Integer>();
        
        int subPos = 0;
        while (true)
        {
            int delimPos = str.indexOf(OID_DELIM, subPos);
            if (delimPos < 0)
            {
                String subStr = str.substring(subPos);
                list.add(Integer.parseInt(subStr));
                break;
            }
            
            String subStr = str.substring(subPos, delimPos);
            list.add(Integer.parseInt(subStr));
            subPos = delimPos + 1;
        }
        
        Integer[] array = new Integer[list.size()];
        array = list.toArray(array);
        return array;
    }
}
