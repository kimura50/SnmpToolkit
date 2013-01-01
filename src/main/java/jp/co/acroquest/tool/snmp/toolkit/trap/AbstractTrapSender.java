// AbstractTrapSender.java ----
// History: 2004/11/22 - Create
package jp.co.acroquest.tool.snmp.toolkit.trap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.acroquest.tool.snmp.toolkit.SnmpToolkitException;

/**
 * TrapSender�C���^�t�F�[�X�������������ۃN���X�B
 * 
 * @author akiba
 * @version 1.0
 */
public abstract class AbstractTrapSender implements TrapSender
{
    /** Trap���M��z�X�g���B */
    protected String host_;

    /** Trap���M��|�[�g�ԍ��B */
    protected int    port_;

    /** Trap���M�R�~���j�e�B���B */
    protected String community_;

    /**
     * TrapSender�ɋ��ʂ̃R���X�g���N�^�B
     */
    protected AbstractTrapSender()
    {
        super();
    }

    /**
     * TrapSender��Trap�𑗐M����z�X�g�ƃ|�[�g�ԍ����w�肷��B
     * 
     * @param host Trap���M��z�X�g���܂���IP�A�h���X�B
     * @param port Trap���M��|�[�g�ԍ��B
     * @throws SnmpToolkitException ���M��̐ݒ�Ɏ��s�����ꍇ�B
     */
    public void setTarget(String host, int port) throws SnmpToolkitException
    {
        this.host_ = host;
        this.port_ = port;
    }

    /**
     * Trap���M�R�~���j�e�B����ݒ肷��B
     * 
     * @param community Trap���M�R�~���j�e�B���Bnull���w�肵���ꍇ��public�ɂȂ�B
     */
    public void setCommunity(String community)
    {
        Log log = LogFactory.getLog(TrapSender.class);
        if (community == null)
        {
            this.community_ = "public";
        }
        else
        {
            this.community_ = community;
        }
        log.debug("Community is set to " + this.community_);
    }

    /**
     * Trap���M��̃z�X�g���܂���IP�A�h���X���擾����B
     * 
     * @return Trap���M��̃z�X�g���܂���IP�A�h���X�B
     */
    protected String getHost()
    {
        return this.host_;
    }

    /**
     * Trap���M��̃|�[�g�ԍ����擾����B
     * 
     * @return Trap���M��̃|�[�g�ԍ��B
     */
    protected int getPort()
    {
        return this.port_;
    }

    /**
     * Trap���M���̃R�~���j�e�B�����擾����B
     * 
     * @return Trap���M���̃R�~���j�e�B���B
     */
    protected String getCommunity()
    {
        return this.community_;
    }
}
