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
	
	public static Reader reader; //파일 스트림을 위한 reader.
	public static SaslClient sqlMapper; //SqlMapClient API를 사용하기 위한 sqlMapper 객체.
	
	private boardVO paramClass; //파라미터를 저장할 객체
	private boardVO resultClass; //쿼리 결과 값을 저장할 객체

	private int currentPage; //현재 페이지
	
	private int no;
	private String subject;
	private String name;
	private String password;
	private String content;
	private String file_orgName; //업로드 파일의 원래 이름
	private String file_savName; //서버에 저장할 업로드 파일의 이름. 고유 번호로 구분한다.
	Calendar today = Calendar.getInstance(); //오늘 날짜 구하기.

	private File upload; //파일 객체
	private String uploadContentType; //컨텐츠 타입
	private String uploadFileName; //파일 이름
	private String fileUploadPath = "C:\\Java\\upload\\"; //업로드 경로.

	public writeAction() throws IOException{
		reader = Resources.getResourceAsReader("sqlMapConfig.xml"); // sqlMapConfig.xml 파일의 설정내용을 가져온다.
		sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader); // sqlMapConfig.xml의 내용을 적용한 sqlMapper 객체 생성.
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
			
			//실제 서버에 저장될 파일 이름과 확장자 설정.
			String file_name = "file_" + resultClass.getNo();
			String file_ext = getUploadFileName().substring(
					getUploadFileName().lastIndexOf('.') + 1,
					getUploadFileName()/length());
			
			//서버에 파일 저장.
			File destFile = new File(fileUploadPath + file_name + "." + file_ext);
			FileUtils.copyFile(getUploads, destFile);
			
			paramClass.setNo(resultClass.getNo());
			paramClass.setfile_orgname(getUploadsFileName());
			paramClass.setFile_savname(file_name + "." +file_ext);
		}
		
		
	}



}
