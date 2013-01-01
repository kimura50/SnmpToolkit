// SnmpConfigItem.java ----
// History: 2004/03/07 - Create
// 2009/05/20 - �N���X���ύX
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * SnmpToolkit�̐ݒ�f�[�^��\���B
 * 
 * @author akiba
 * @version 1.0
 */
public class SnmpConfigItem
{
    /** �ݒ�l���i�[����}�b�v�B */
    private Map<String, Object> configMap_;

    /** SNMP�}�l�[�W���̃��X�g�B */
    private SnmpManagerList     managerList_;

    /** Registry�̃f�t�H���g�|�[�g�ԍ��B */
    private static final int    DEFAULT_PORT = 10000;

    /**
     * �R���X�g���N�^�B
     */
    public SnmpConfigItem()
    {
        super();

        this.configMap_ = new HashMap<String, Object>();
    }

    /**
     * SNMP�}�l�[�W���̃��X�g��ݒ肷��B
     * 
     * @param mgrList SNMP�}�l�[�W���̃��X�g�B
     */
    public void setSnmpManagerList(SnmpManagerList mgrList)
    {
        this.managerList_ = mgrList;
    }

    /**
     * SNMP�}�l�[�W���̃��X�g���擾����B
     * 
     * @return SNMP�}�l�[�W���̃��X�g�B
     */
    public SnmpManagerList getSnmpManagerList()
    {
        return this.managerList_;
    }

    /**
     * �ݒ�l���}�b�v�ɒǉ�����B<br>
     * ���̃��\�b�h�́ADigester����Ăяo�����B
     * 
     * @param name �ݒ�l�̖��́B
     * @param value �ݒ�l�B
     */
    public void setProperty(String name, Object value)
    {
        this.configMap_.put(name, value);
    }

    /**
     * �f�[�^�f�B���N�g�������擾����B
     * 
     * @return �f�[�^�f�B���N�g�����B
     */
    public String getDataDir()
    {
        String dataDir = (String) this.configMap_.get("data-dir");
        return dataDir;
    }

    /**
     * ���M��|�[�g�ԍ����擾����B
     * 
     * @return ���M��|�[�g�ԍ��B
     */
    public int getRemotePort()
    {
        int port = this.getIntValue("remote-port", DEFAULT_PORT);
        return port;
    }

    /**
     * �ݒ�l���i�[�����}�b�v����A�w�肵�����̂̃p�����[�^��int�l�Ƃ��Ď擾����B
     * 
     * @param name �擾����p�����[�^�̖��́B
     * @param defvalue �p�����[�^���w�肳��Ă��Ȃ������ꍇ�̃f�t�H���g�l�B
     * @return �擾����int�p�����[�^�B
     */
    private int getIntValue(String name, int defvalue)
    {
        int value = defvalue;
        String valueObj = (String) this.configMap_.get(name);
        if (valueObj != null)
        {
            value = Integer.parseInt(valueObj);
        }

        return value;
    }

    /**
     * ���̃I�u�W�F�N�g�̕�����\�����擾����B<br>
     * ���̃��\�b�h�͓����ŕێ�����}�b�v��toString()���\�b�h�Ɉˑ�����B
     * 
     * @return ���̃I�u�W�F�N�g�̕�����\���B
     */
    public String toString()
    {
        return this.configMap_.toString();
    }
}
