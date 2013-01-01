//AgentDefinitionListLoader.java ----
// History: 2009/04/26 - Create
//          2009/08/15 - �N���X���ύX
package jp.co.acroquest.tool.snmp.toolkit.loader;

import java.io.File;
import java.io.IOException;

import jp.co.acroquest.tool.snmp.toolkit.entity.AgentDefinition;
import jp.co.acroquest.tool.snmp.toolkit.entity.AgentDefinitionList;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * Agent���X�g��`�f�[�^XML��ǂݍ��ރ��[�_�[�N���X�B
 * 
 * @author akiba
 */
public class AgentDefinitionListLoader
{
    /** Agent���X�g��`�f�[�^���L�q����XML��ǂݍ��ވׂ�Digester�B */
    private Digester digester_;

    /**
     * �R���X�g���N�^�B<br/>
     * Digester�����������A�ǂݍ��݃��[����ݒ肷��B
     */
    public AgentDefinitionListLoader()
    {
        super();
        
        this.digester_ = new Digester();
        
        // 1. agent�v�f�́AAgent�N���X�𐶐�����
        this.digester_.addObjectCreate("agents", AgentDefinitionList.class);
        
        //--------------------------------------------------------------------
        // <traps/trap-data/varbind>�̒ǉ��w��
        //--------------------------------------------------------------------
        // 1. traps/trap-data/varbind �v�f�́ASnmpVarbind�N���X�𐶐�����
        this.digester_.addObjectCreate("agents/agent", AgentDefinition.class);
        // 2. �������� AgentDefinition �́AaddAgentInfo()���\�b�h���Ăяo����TrapData�ɒǉ�����
        this.digester_.addSetNext("agents/agent", "addAgentDefinition", AgentDefinition.class.getName());
        // 3. agents/agent �v�f�́AsetOid()���\�b�h���Ăяo��
        this.digester_.addCallMethod("agents/agent", "setAgentMIBFile", 0);
        // 4. agents/agent@address �v�f�́AsetType()���\�b�h���Ăяo��
        this.digester_.addSetProperties("agents/agent");
        
    }
    
    /**
     * �w�肳�ꂽ�t�@�C����ǂݍ��݁AAgentDefinitionList�I�u�W�F�N�g�𐶐�����B<br/>
     * �������ꂽAgentDefinitionList�I�u�W�F�N�g�́A������AgentDefinition�I�u�W�F�N�g��ێ�����B
     * 
     * @param filename Agent��`�t�@�C���p�X�B
     * @return �ǂݍ��݂ɐ����������ʐ������ꂽAgentDefinitionList�I�u�W�F�N�g�B
     * @throws IOException �ǂݍ��݂Ɏ��s�����ꍇ�B
     */
    public AgentDefinitionList load(String filename)
        throws IOException
    {
        AgentDefinitionList agentList = null;
        try
        {
            // Digester���g�p����Agent���X�g��`�f�[�^�����[�h����
            agentList = (AgentDefinitionList) this.digester_.parse(new File(filename));
        }
        catch (SAXException exception)
        {
            throw new IOException("SAXException occured.\n" +  exception.toString());
        }
        
        return agentList;
    }
    
    /**
     * �e�X�g�p�̃��C�����\�b�h�B
     * 
     * @param args �ǂݍ��ރf�[�^�t�@�C���B
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        AgentDefinitionListLoader loader = new AgentDefinitionListLoader();
        AgentDefinitionList agentList = loader.load(args[0]);
        for (AgentDefinition agent : agentList.getAgentDefinitionArray())
        {
            System.out.println(agent.toString());
        }
    }
}
