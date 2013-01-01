//RequestHandler.java ----
// History: 2005/02/07 - Create
//          2009/05/07 - initHandler()��ǉ�
//          2009/08/15 - AgentService�Ή�
package jp.co.acroquest.tool.snmp.toolkit.request;

import jp.co.acroquest.tool.snmp.toolkit.AgentService;
import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;

/**
 * SNMP-GET���N�G�X�g���󂯕t���鏈���N���X�̃C���^�t�F�[�X�B
 * 
 * @author akiba
 */
public interface RequestHandler
{
    /** �f�t�H���g��Request��M�|�[�g�ԍ��B */
    static final int    DEFAULT_SNMP_PORT      = 161;
    
    /** �f�t�H���g�̓ǂݍ��ݐ�p�R�~���j�e�B���B */
    static final String DEFAULT_RO_COMMUNITY   = "public";
    
    /** �f�t�H���g�̏������݉\�R�~���j�e�B���B */
    static final String DEFAULT_RW_COMMUNITY   = "public";
    
    /** �f�t�H���g��Trap���M�R�~���j�e�B���B */
    static final String DEFAULT_TRAP_COMMUNITY = "public";
    
    /**
     * RequestHandler�ɏ������p�����[�^��^����B
     * 
     * @param agentService RequestHandler����������Agent�f�[�^�̃T�[�r�X�B
     * @throws SnmpToolkitException RequestHandler�̏������Ɏ��s�����ꍇ�B
     */
    void initHandler(AgentService agentService)
        throws SnmpToolkitException;
    
    /**
     * ���X�j���O���J�n����B
     * 
     * @throws SnmpToolkitException ���X�j���O�̊J�n�Ɏ��s�����ꍇ�B
     */
    void startListening() throws SnmpToolkitException;
    
    /**
     * RequestHandler���~����B
     * 
     * @throws SnmpToolkitException ���X�j���O�̒�~�Ɉُ킪���������ꍇ�B
     */
    void stopListening() throws SnmpToolkitException;

    ///**
    // * �V����Agent�I�u�W�F�N�g��ݒ肷��B
    // * 
    // * @param agent �V����Agent�I�u�W�F�N�g�B
    // */
    //void setAgent(Agent agent);
    //
    ///**
    // * ���ݐݒ肳��Ă���Agent�I�u�W�F�N�g���擾����B
    // * 
    // * @return Agent�I�u�W�F�N�g�B
    // */
    //Agent getAgent();
}
