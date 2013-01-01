//AgentDefinitionList.java ----
// History: 2009/05/07 - Create
package jp.co.acroquest.tool.snmp.toolkit.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * AgentDefinition��ێ����郊�X�g�BAgentDefinitionLoader���琶�������B
 * 
 * @author akiba
 */
public class AgentDefinitionList
{
    /** AgentDefinition��ێ�����}�b�v�B */
    private Map<String, AgentDefinition> agentDefinitionList_;
    
    /**
     * AgentDefinitionList�𐶐�����B
     */
    public AgentDefinitionList()
    {
        this.agentDefinitionList_ = new HashMap<String, AgentDefinition>();
    }

    /**
     * AgentDefinition��ǉ�����B
     * 
     * @param AgentDefinition �ǉ��Ώۂ�AgentDefinition�B
     */
    public void addAgentDefinition(AgentDefinition AgentDefinition)
    {
        if (AgentDefinition != null)
        {
            String address = AgentDefinition.getAddress();
            AgentDefinition preInfo = this.agentDefinitionList_.get(address);
            if (preInfo == null)
            {
                this.agentDefinitionList_.put(address, AgentDefinition);
            }
        }
    }
    
    /**
     * �w�肳�ꂽIP�A�h���X�ɑΉ�����AgentDefinition���擾����B
     * 
     * @param address �擾�Ώ�AgentDefinition��IP�A�h���X�B
     * @return �Ή�����AgentDefinition�B���݂��Ȃ��ꍇ��null��Ԃ��B
     */
    public AgentDefinition getAgentDefinition(String address)
    {
        AgentDefinition retAgentDefinition = this.agentDefinitionList_.get(address);
        return retAgentDefinition;
    }
    
    /**
     * ����AgentDefinitionList���ێ�����S�Ă�AgentDefinition��z��Ŏ擾����B
     * 
     * @return AgentDefinition�̔z��B
     */
    public AgentDefinition[] getAgentDefinitionArray()
    {
        Collection<AgentDefinition> infoSet = this.agentDefinitionList_.values();
        AgentDefinition[] infoArray = new AgentDefinition[infoSet.size()];
        infoArray = infoSet.toArray(infoArray);
        
        return infoArray;
    }
}
