// Snmp4jSetRequestSender.java ----
// History: 2010/03/04 - Create
package jp.co.acroquest.tool.snmp.toolkit.command;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.tool.snmp.toolkit.helper.Snmp4jVariableHelper;
import jp.co.acroquest.tool.snmp.toolkit.helper.SnmpVariableHelper;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * SNMP4J���g�p����SET���N�G�X�g�𑗐M����c�[���B
 * 
 * @author akiba
 */
public class Snmp4jSetRequestSender implements ResponseListener
{
    private SnmpVariableHelper    helper_;
    private String                host_;
    private int                   port_;
    private String                rwCommunity_;
    private List<VariableBinding> varbinds_;
    private Snmp                  snmp_;

    /**
     * Snmp4jSetRequestSender�𐶐�����B
     * 
     * @param args �R�}���h���C�������B
     * @throws IOException
     */
    public Snmp4jSetRequestSender(String[] args) throws IOException
    {
        // SNMP4J�̃I�u�W�F�N�g�𐶐�
        UdpAddress udpAddress = new UdpAddress(InetAddress.getLocalHost(), 11111);
        TransportMapping transportMapping = new DefaultUdpTransportMapping(udpAddress);
        this.snmp_ = new Snmp(transportMapping);

        // Variable�����p�̃w���p�[�I�u�W�F�N�g�𐶐�
        this.helper_ = new Snmp4jVariableHelper();

        // ��������z�X�g���ƃ|�[�g�ԍ����擾
        this.host_ = args[0];
        this.port_ = Integer.parseInt(args[1]);
        this.rwCommunity_ = args[2];

        // Varbind�̃��X�g�𐶐�
        this.varbinds_ = new ArrayList<VariableBinding>();
        for (int index = 3; index < args.length; index += 3)
        {
            String oid = args[index];
            String type = args[index + 1];
            String value = args[index + 2];

            System.out.println("set: OID=" + oid + ", type=" + type + ", value=" + value);

            Variable variable = createVariable(value, type);
            VariableBinding varbind = new VariableBinding(new OID(oid), variable);

            this.varbinds_.add(varbind);
        }
        
        // RESPONSE���󂯎���悤��listen���J�n����
        this.snmp_.listen();
    }

    /**
     * Request�p�P�b�g�𑗐M����B
     * 
     * @throws IOException
     */
    private void execute() throws IOException
    {
        VariableBinding[] varbindArray = new VariableBinding[this.varbinds_.size()];
        varbindArray = this.varbinds_.toArray(varbindArray);

        // SET���s��PDU�𐶐�����
        PDU setPdu = new PDU();
        setPdu.setRequestID(new Integer32(1));
        setPdu.setType(PDU.SET);
        setPdu.addAll(varbindArray);

        System.out.println("SET PDU=" + setPdu);

        // ���M����
        CommunityTarget target = new CommunityTarget();
        target.setVersion(SnmpConstants.version2c);
        target.setTimeout(5000L);
        target.setAddress(new UdpAddress(InetAddress.getByName(this.host_), this.port_));
        target.setCommunity(new OctetString(this.rwCommunity_));

        ResponseEvent event = this.snmp_.set(setPdu, target);
        System.out.println("reseponse: " + event.getResponse());
    }

    /**
     * �l������������ƌ^����Variable�𐶐�����B
     * 
     * @param value SNMP�̒l�B
     * @param type SNMP��Value�̌^�B
     * @return �������ꂽVariable�I�u�W�F�N�g�B
     */
    private Variable createVariable(String value, String type)
    {
        Variable variable = (Variable) this.helper_.createAsnObject(value, type);
        return variable;
    }

    /**
     * {@inheritDoc}
     */
    public void onResponse(ResponseEvent response)
    {
        System.out.println("onResponse: " + response.toString());
    }

    /**
     * �v���O�����G���g���B
     * 
     * @param args �R�}���h���C�������B
     * @throws IOException �ڑ��Ɏ��s�����ꍇ�B
     */
    public static void main(String[] args) throws IOException
    {
        if (args.length < 3)
        {
            System.err.println("USAGE: Snmp4jSetRequestSender "
                    + "<host> <port> <community> [<oid> <type> <value>]...");
            System.exit(1);
        }

        Snmp4jSetRequestSender app = new Snmp4jSetRequestSender(args);
        app.execute();
    }
}
