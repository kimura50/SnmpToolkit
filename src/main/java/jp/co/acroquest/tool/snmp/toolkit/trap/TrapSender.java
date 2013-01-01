//TrapSender.java ----
// History: 2004/11/22 - Create
package jp.co.acroquest.tool.snmp.toolkit.trap;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;
import jp.co.acroquest.tool.snmp.toolkit.entity.TrapData;

/**
 * Trap���M���s���N���X�̊��C���^�t�F�[�X�B
 * 
 * @author akiba
 * @version 1.0
 */
public interface TrapSender
{
	/**
     * Trap�𑗐M���鎞�̃R�~���j�e�B����ݒ肷��B
     * 
     * @param comm Trap�R�~���j�e�B���B
     */
    public void setCommunity(String comm);
    
    /**
     * �w�肵��TrapData���g�p����Trap�𑗐M����B
     * 
     * @param trapData ���M����TrapData�B
     * @throws SnmpToolkitException Trap���M���ɔ���������O�B
     */
    public void sendTrap(TrapData trapData)
    	throws SnmpToolkitException;
}
