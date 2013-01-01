// Traps.java ----
// History: 2004/03/08 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * TrapData�����X�g�ŊǗ�����N���X�B
 * 
 * @author akiba
 * @version 1.0
 */
public class Traps
{
    /** �ǉ�����Ă���TrapData���Ǘ����郊�X�g�B */
    private List<TrapData> trapList_;

    /**
     * �R���X�g���N�^�B
     */
    public Traps()
    {
        super();

        this.trapList_ = new ArrayList<TrapData>();
    }

    /**
     * TrapData��ǉ�����B
     * 
     * @param trapData TrapData�B
     */
    public void addTrapData(TrapData trapData)
    {
        this.trapList_.add(trapData);
    }

    /**
     * �ǉ�����Ă���S�Ă�TrapData��z��Ŏ擾����B
     * 
     * @return �ǉ�����Ă���S�Ă�TrapData�B
     */
    public TrapData[] getAllTrapData()
    {
        TrapData[] trapDataArray = new TrapData[this.trapList_.size()];
        trapDataArray = (TrapData[]) this.trapList_.toArray(trapDataArray);

        return trapDataArray;
    }
}
