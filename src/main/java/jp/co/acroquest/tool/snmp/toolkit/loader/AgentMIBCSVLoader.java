//AgentMIBCSVLoader.java ----
// History: 2009/05/20 - Create
package jp.co.acroquest.tool.snmp.toolkit.loader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jp.co.acroquest.tool.snmp.toolkit.entity.Agent;
import jp.co.acroquest.tool.snmp.toolkit.entity.SnmpVarbind;

import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.constraint.Unique;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * Agent��MIB�f�[�^CSV�t�@�C����ǂݍ��ރ��[�_�[�N���X�B<br/>
 * CSV�t�@�C���́AExcel�œǂݍ��߂�`���ŁA�P�s�ڂɃw�b�_��������w�肵�Ă����K�v������B<br/>
 * �P�s�ڂ̃w�b�_������́AAgent�I�u�W�F�N�g��Getter���\�b�h���ɓ]�p����邽�߁A�ύX���Ȃ����ƁB<br/>
 * �񖼁F
 * <ol>
 *   <li>oid : MIB�f�[�^��OID�B</li>
 *   <li>type : MIB�f�[�^�̌^�Bstring, octets, integer, hex, ipaddress, timeticks, object-id�̂����ꂩ�B</li>
 *   <li>value : MIB�̒l�B</li>
 *   <li>accessibility : �Q�Ɖ\���BREAD-WRITE(default), READ-ONLY, NOT-ACCESSIBLE�̂����ꂩ�B</li>
 * </ol>
 * 
 * @author akiba
 */
public class AgentMIBCSVLoader
{
    /** CSV�ǂݍ��ݎ��̋K�����K�肷��CellProcessor�B */
    private final CellProcessor[] PROCESSORS = new CellProcessor[] {
        // oid
        new Unique(),
        // type
        null,
        // value
        null,
        // accessibility
        new ConvertNullTo("READ-WRITE")
    };
    
    /**
     * �f�t�H���g�R���X�g���N�^�B
     */
    public AgentMIBCSVLoader()
    {
    }

    /**
     * �w�肵���p�X�ɔz�u����Ă���CSV�t�@�C����ǂݍ��݁AAgent�f�[�^�𐶐�����B
     * 
     * @param parent CSV�t�@�C�����z�u����Ă���f�B���N�g���B
     * @param filename CSV�t�@�C�����B
     * @return �ǂݍ��݂ɐ����������ʁA�������ꂽAgent�I�u�W�F�N�g�B
     * @throws IOException �ǂݍ��݂Ɏ��s�����ꍇ�B
     */
    public Agent load(String parent, String filename)
        throws IOException
    {
        Agent agent = null;
        
        // �f�B���N�g���ƃt�@�C��������File�I�u�W�F�N�g�𐶐�����
        File csvFile = new File(parent, filename);
        
        // CSV�t�@�C�����[�_�[
        ICsvBeanReader reader = null;
        try
        {
            // Excel�̓ǂݍ��݋K�����g�p����CSV�t�@�C����ǂݍ���
            reader = new CsvBeanReader(new FileReader(csvFile), CsvPreference.EXCEL_PREFERENCE);
            
            // �w�b�_���擾����
            final String[] headers = reader.getCSVHeader(true);
            
            // Agent�𐶐����A�P�s�P��Varbind�𐶐����i�[����
            agent = new Agent();
            while(true)
            {
                SnmpVarbind varbind = reader.read(SnmpVarbind.class, headers, PROCESSORS);
                if (varbind == null)
                {
                    break;
                }
                
                agent.addVarbind(varbind);
            }
        }
        catch (SuperCSVException exception)
        {
            IOException ioex = new IOException(exception.getLocalizedMessage());
            throw ioex;
        }
        finally
        {
            // �J����Ă���Reader�̓N���[�Y����
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException ioex)
                {
                }
            }
        }
        
        return agent;
    }
    
    /**
     * �e�X�g�p�̃��C�����\�b�h�B
     * 
     * @param args �ǂݍ��ރf�[�^�t�@�C���B
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        AgentMIBCSVLoader loader = new AgentMIBCSVLoader();
        Agent agent = loader.load(".", args[0]);
        System.out.println(agent.toString());
    }
}
