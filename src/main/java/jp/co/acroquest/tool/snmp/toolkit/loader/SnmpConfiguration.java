// SnmpConfiguration.java ----
// History: 2004/03/07 - Create
// 2009/07/25 - URI�w���s�v�Ƃ��邽�߂̏C��
package jp.co.acroquest.tool.snmp.toolkit.loader;

import java.io.File;
import java.io.IOException;

import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpConfigItem;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpManager;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpManagerList;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * SNMP�ݒ��ێ�����N���X�B
 * 
 * @author akiba
 * @version 1.0
 */
public class SnmpConfiguration
{
    /** �ݒ�A�C�e���B */
    private SnmpConfigItem           item_     = null;

    /** ���̃N���X�̃C���X�^���X�B */
    private static SnmpConfiguration config__  = null;

    /** ���[�g�m�[�h���B */
    private static final String      ROOT_NODE = "config";

    /**
     * ���̃N���X�̃C���X�^���X������������B<br>
     * getInstance()���\�b�h���g�p���ăC���X�^���X���擾����O�ɁA�K�����s���邱�ƁB<br>
     * ���̃��\�b�h�̌Ăяo���͏���̂ݗL���ł���A�Q��ڈȍ~�̌Ăяo���͉������Ȃ��B
     * 
     * @param path �ǂݍ��ސݒ�t�@�C��(XML)�ւ̃p�X�B
     * @throws IOException
     * @throws SAXException
     */
    public static void initialize(String path) throws IOException, SAXException
    {
        if (config__ != null)
        {
            return;
        }

        config__ = new SnmpConfiguration(path);
    }

    /**
     * ���̃N���X�̐����ς݃C���X�^���X���擾����B<br>
     * �������Ainitialize()���\�b�h���Ăяo���O�͏��null��Ԃ��B
     * 
     * @return ���̃N���X�̐����ς݃C���X�^���X�B����������Ă��Ȃ��ꍇ�͏��null�B
     */
    public static SnmpConfiguration getInstance()
    {
        return config__;
    }

    /**
     * �ݒ�N���X������������B
     */
    private SnmpConfiguration(String path) throws IOException, SAXException
    {
        super();
        loadConfiguration(path);
    }

    /**
     * �R���X�g���N�^����Ăяo����AXML�t�@�C������ݒ�l���擾����B
     * 
     * @param path �ݒ�t�@�C���ւ̃p�X�B
     * @throws IOException
     * @throws SAXException
     */
    private void loadConfiguration(String path) throws IOException, SAXException
    {
        Digester digester = new Digester();
        digester.setValidating(false);
        digester.push(this);

        digester.addObjectCreate(ROOT_NODE, SnmpConfigItem.class.getName());
        digester.addSetNext(ROOT_NODE, "setSnmpConfigItem", SnmpConfigItem.class.getName());
        digester.addCallMethod(ROOT_NODE + "/property", "setProperty", 2);
        digester.addCallParam(ROOT_NODE + "/property", 0, "name");
        digester.addCallParam(ROOT_NODE + "/property", 1, "value");

        digester.addObjectCreate(ROOT_NODE + "/managers", SnmpManagerList.class.getName());
        digester.addSetNext(ROOT_NODE + "/managers", "setSnmpManagerList", SnmpManagerList.class
                .getName());
        digester.addObjectCreate(ROOT_NODE + "/managers/manager", SnmpManager.class.getName());
        digester.addSetNext(ROOT_NODE + "/managers/manager", "addSnmpManager", SnmpManager.class
                .getName());
        digester.addCallMethod(ROOT_NODE + "/managers/manager", "setManagerAddress", 0);

        // URI�w���s�v�Ƃ��邽�߂�File�I�u�W�F�N�g��ʂ�
        digester.parse(new File(path));
    }

    /**
     * Snmp�ݒ��ۑ�����B
     * 
     * @param item Snmp�ݒ�B
     */
    public void setSnmpConfigItem(SnmpConfigItem item)
    {
        this.item_ = item;
    }

    /**
     * Snmp�ݒ���擾����B
     * 
     * @return Snmp�ݒ�B
     */
    public SnmpConfigItem getSnmpConfigItem()
    {
        return this.item_;
    }

    /**
     * Object#toString()�̃I�[�o�[���C�h�B
     * 
     * @return ���̃I�u�W�F�N�g�̕�����\���B
     */
    public String toString()
    {
        String str = "SnmpConfiguration={" + this.item_.toString() + "}";
        return str;
    }

    public static void main(String[] args) throws Exception
    {
        SnmpConfiguration.initialize(args[0]);
        SnmpConfiguration config = SnmpConfiguration.getInstance();
        SnmpManagerList mgrList = config.getSnmpConfigItem().getSnmpManagerList();
        for (SnmpManager mgr : mgrList.getSnmpManagers())
        {
            System.out.println(mgr);
        }
    }
}
