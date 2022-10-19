package ch13;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FileDialog;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;

public class FileCopyEx2 extends MFrame implements ActionListener{
	
	Button open, save;
	TextArea ta;
	FileDialog openDialog, saveDialog;
	String sourceDir;
	String sourceFile;
	
	public FileCopyEx2() {
		super(400, 500);
		setTitle("FileCopyEx2");
		add(ta = new TextArea());
		Panel p = new Panel();
		p.add(open = new Button("OPEN"));
		p.add(save = new Button("SAVE"));
		//ta.setEditable(false);
		open.addActionListener(this);  //연결
		save.addActionListener(this);
		add(p,BorderLayout.SOUTH);
		validate();
	}

	@Override

	public void actionPerformed(ActionEvent e) { // 이벤트 입장에서 버튼은 source
		Object obj = e.getSource();
		System.out.println(open.hashCode());
		System.out.println(save.hashCode());
		if(obj == open) { // 참조형은 객체의 주소값을 비교한다. 
			//비번 비교는 절대 ==사용하면 안된다. db비번과 사용자가 입력한 비번은 서로 다른 String 객체
			//dbPass == inputPass 죽었다깨도 false나온다.
			// dbPass.equals(inputPass) 이렇게 해야 한다.
			// equals는 내용이 동일한지 비교. ==는 주소값 비교
			
			openDialog = new FileDialog(this,"파일열기",FileDialog.LOAD);
			openDialog.setVisible(true);
			String dir, file;
			dir = openDialog.getDirectory();
			file = openDialog.getFile();
			fileReader(dir + file);
			
			
		}else if(obj == save) {
			saveDialog = new FileDialog(this,"파일저장",FileDialog.SAVE);
			saveDialog.setVisible(true);
			String dir, file;
			dir = openDialog.getDirectory();
			file = openDialog.getFile();
			fileWriter(dir + file);
		}
		
//		int i = 10;
//		int j =12;
		//System.out.println(i == j); //java 기본형은 값을 비교
	}
	
	public void fileReader(String file){
		try {
			FileReader fr = new FileReader(file);
			int c;
			String s="";  
			while((c=fr.read())!=-1) {
				s+=(char)c;
			}
			ta.setText(s);
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void fileWriter(String file){
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(ta.getText());
			for (int i = 5; i > 0; i--) {
				ta.setText("저장 하였습니다. - " + i
						+"초후에 사라집니다.");
				Thread.sleep(1000);
			}
			ta.setText("");
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new FileCopyEx2();
	}
}



