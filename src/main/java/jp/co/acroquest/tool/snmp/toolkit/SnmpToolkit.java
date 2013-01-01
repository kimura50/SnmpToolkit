// SnmpToolkit.java ----
// History: 2004/03/23 - Create
// 2009/07/25 - MIB�f�[�^�ēǂݍ��݃C���^�t�F�[�X��ǉ�
package jp.co.acroquest.tool.snmp.toolkit;

import java.rmi.Remote;
import java.rmi.RemoteException;

import jp.co.acroquest.tool.snmp.toolkit.entity.TrapData;

/**
 * SNMP Toolkit�̃����[�g�C���^�t�F�[�X�B
 * 
 * @author akiba
 * @version 1.0
 */
public interface SnmpToolkit extends Remote
{
    /**
     * Trap�𑗐M����B
     * 
     * @param address Agent���w�肷��IP�A�h���X�B
     * @param trapData ���M����Trap�̃f�[�^�B
     * @throws RemoteException RMI�Ăяo���Ŕ���������O�B
     */
    public void sendTrap(String address, TrapData trapData)
        throws RemoteException;

    /**
     * MIB�f�[�^�̍ēǂݍ��݂����s����B
     * 
     * @throws RemoteException RMI�Ăяo���Ŕ���������O�B
     */
    public void reloadMIBData() throws RemoteException;

    /**
     * ����IP�A�h���X��Agent�ɂ���MIB�f�[�^�̍ēǂݍ��݂����s����B
     * 
     * @param address Agent���w�肷��IP�A�h���X�B
     * @throws RemoteException RMI�Ăяo���Ŕ���������O�B
     */
    public void reloadMIBData(String address) throws RemoteException;
}
