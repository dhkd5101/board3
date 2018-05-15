package board;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Calendar;

import javax.annotation.Resources;
import javax.security.sasl.SaslClient;

import org.apache.commons.io.FileUtils;

import com.opensymphony.xwork2.ActionSupport;




public class writeAction extends ActionSupport{
	
	public static Reader reader; //���� ��Ʈ���� ���� reader.
	public static SaslClient sqlMapper; //SqlMapClient API�� ����ϱ� ���� sqlMapper ��ü.
	
	private boardVO paramClass; //�Ķ���͸� ������ ��ü
	private boardVO resultClass; //���� ��� ���� ������ ��ü

	private int currentPage; //���� ������
	
	private int no;
	private String subject;
	private String name;
	private String password;
	private String content;
	private String file_orgName; //���ε� ������ ���� �̸�
	private String file_savName; //������ ������ ���ε� ������ �̸�. ���� ��ȣ�� �����Ѵ�.
	Calendar today = Calendar.getInstance(); //���� ��¥ ���ϱ�.

	private File upload; //���� ��ü
	private String uploadContentType; //������ Ÿ��
	private String uploadFileName; //���� �̸�
	private String fileUploadPath = "C:\\Java\\upload\\"; //���ε� ���.

	public writeAction() throws IOException{
		reader = Resources.getResourceAsReader("sqlMapConfig.xml"); // sqlMapConfig.xml ������ ���������� �����´�.
		sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader); // sqlMapConfig.xml�� ������ ������ sqlMapper ��ü ����.
		reader.close();
	}
	
	public String form()throws Exception{
		return SUCCESS;
	}
	public String execute() throws Exception{
		
		paramClass = new boardVO();
		resultClass = new boardVO();
		
		paramClass.setSubject(getSubject());
		paramClass.setName(getName());
		paramClass.setPassword(getPassword());
		paramClass.setContent(getContent());
		paramClass.setRegdate(today.getTime());
		
		sqlMapper.insert("insertBoard", paramClass);
		
		if(getUploads() != null) {
			
			resultClass = (boardVO) sql.Mapper.queryForObject("selectLastNo");
			
			//���� ������ ����� ���� �̸��� Ȯ���� ����.
			String file_name = "file_" + resultClass.getNo();
			String file_ext = getUploadFileName().substring(
					getUploadFileName().lastIndexOf('.') + 1,
					getUploadFileName()/length());
			
			//������ ���� ����.
			File destFile = new File(fileUploadPath + file_name + "." + file_ext);
			FileUtils.copyFile(getUploads, destFile);
			
			paramClass.setNo(resultClass.getNo());
			paramClass.setfile_orgname(getUploadsFileName());
			paramClass.setFile_savname(file_name + "." +file_ext);
		}
		
		
	}



}
