// TrapDataLoader.java ----
// History: 2004/03/07 - Create
// 2009/07/25 - URI�w���s�v�Ƃ��邽�߂̏C��
package jp.co.acroquest.tool.snmp.toolkit.loader;

import java.io.File;
import java.io.IOException;

import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpVarbind;
import jp.co.acroquest.tool.snmp.toolkit.entity.TrapData;
import jp.co.acroquest.tool.snmp.toolkit.entity.Traps;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * Digester���g�p���āAXML�t�@�C������Trap�f�[�^��ǂݍ��ރ��[�_�B
 * 
 * @author akiba
 * @version 1.0
 */
public class TrapDataLoader
{
    /** Trap�f�[�^���L�q����XML��ǂݍ��ވׂ�Digester�B */
    private Digester digester_;

    /**
     * �R���X�g���N�^�B
     */
    public TrapDataLoader()
    {
        super();

        this.digester_ = new Digester();

        // --------------------------------------------------------------------
        // <traps>�v�f�̒ǉ��w��
        // --------------------------------------------------------------------
        // 1. traps �v�f�́ATraps�N���X�𐶐�����
        this.digester_.addObjectCreate("traps", Traps.class);

        // --------------------------------------------------------------------
        // <traps/trap-data>�v�f�̒ǉ��w��
        // --------------------------------------------------------------------
        // 1. traps/trap-data �v�f�́ATrapData�N���X�𐶐�����
        this.digester_.addObjectCreate("traps/trap-data", TrapData.class);
        // 2. �������� TrapData�I�u�W�F�N�g�́ATraps�N���X��addTrapData()���\�b�h�Œǉ�����
        this.digester_.addSetNext("traps/trap-data", "addTrapData", TrapData.class.getName());
        // 3. traps/trap-data/trap-oid �v�f�́AsetTrapOid()���\�b�h���Ăяo��
        this.digester_.addBeanPropertySetter("traps/trap-data/trap-oid", "trapOid");
        // 4. traps/trap-data/enterprise �v�f�́AsetEnterprise()���\�b�h���Ăяo��
        this.digester_.addBeanPropertySetter("traps/trap-data/enterprise");

        this.digester_.addBeanPropertySetter("traps/trap-data/generic");

        this.digester_.addBeanPropertySetter("traps/trap-data/specific");
        // 5. traps/trap-data@* �v�f�́Aset*()���\�b�h���Ăяo��
        this.digester_.addSetProperties("traps/trap-data");

        // --------------------------------------------------------------------
        // <traps/trap-data/varbind>�̒ǉ��w��
        // --------------------------------------------------------------------
        // 1. traps/trap-data/varbind �v�f�́ASnmpVarbind�N���X�𐶐�����
        this.digester_.addObjectCreate("traps/trap-data/varbind", SnmpVarbind.class);
        // 2. ��������SnmpVarbind�I�u�W�F�N�g�́AaddVarbind()���\�b�h���Ăяo����TrapData�ɒǉ�����
        this.digester_.addSetNext("traps/trap-data/varbind", "addVarbind");
        // 3. traps/trap-data/varbind/oid �v�f�́AsetOid()���\�b�h���Ăяo��
        this.digester_.addBeanPropertySetter("traps/trap-data/varbind/oid");
        // 4. traps/trap-data/varbind/value �v�f�́AsetValue()���\�b�h���Ăяo��
        this.digester_.addBeanPropertySetter("traps/trap-data/varbind/value");
        // 5. traps/trap-data/varbind/value@type �v�f�́AsetType()���\�b�h���Ăяo��
        this.digester_.addSetProperties("traps/trap-data/varbind/value", "type", "type");
    }

    /**
     * Trap�f�[�^�����[�h����B
     * 
     * @param path Trap�f�[�^���L�q����XML�t�@�C���ւ̃p�X�B
     * @return Trap�f�[�^�B
     * @throws IOException �ǂݍ��݂Ɏ��s�������B
     */
    public Traps load(String path) throws IOException
    {
        Traps traps;
        try
        {
            // URI�w���s�v�Ƃ��邽�߂�File�N���X��ʂ�
            File datafile = new File(path);

            // Digester���g�p����Trap�f�[�^�����[�h����
            traps = (Traps) this.digester_.parse(datafile);
        }
        catch (SAXException e)
        {
            throw new IOException("SAXException occured.\n" + e.toString());
        }

        return traps;
    }

    /**
     * �e�X�g�p�̃��C�����\�b�h�B
     * 
     * @param args �ǂݍ��ރf�[�^�t�@�C���B
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        TrapDataLoader loader = new TrapDataLoader();
        Traps traps = loader.load(args[0]);

        TrapData[] datas = traps.getAllTrapData();
        for (int index = 0; index < datas.length; index++)
        {
            System.out.println("[" + index + "] --------------------------");
            System.out.println(datas[index].toString());
        }
    }
}
