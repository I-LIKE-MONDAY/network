package ch13;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;

public class FileWrite2Ex extends MFrame implements ActionListener{
	
	
	TextArea ta;
	TextField tf;
	Button save;
	
	public FileWrite2Ex() {
		super(320, 400);  //가로,세로
		setTitle("FileWriter");
		add(ta=new TextArea());
		Panel p = new Panel();
		p.add(tf = new TextField(30));
		p.add(save = new Button("SAVE"));
		ta.setEditable(false);  //편집 비활성화. 입력할 수 없게
		tf.addActionListener(this);
		save.addActionListener(this);
		add(p,BorderLayout.SOUTH);  //아래에 
		validate();
	}
	
	@Override  //ctrl space 하니까 생김. 입력하고 엔터치면 호출되도록 만듦. e는 버튼치면 액션이벤트 객체가 내부에 만들어진 걸 가리킨다
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource(); //이벤트를 발생시킨 객체를 리턴 
		if(obj == tf) {
			//System.out.println("tf");
			ta.append(tf.getText() + "\n");
			tf.setText("");
			tf.requestFocus();
		} else if(obj == save){
//			System.out.println("save");
			saveFile(ta.getText());
			ta.setText("");
			try {
				for(int i =5; i>0; i--) {
					ta.setText("저장하였습니다." + i + "초 후에 사라집니다.");
					Thread.sleep(1000);//1초 후에 사라지게. 5부터 카운트 다운 시작.
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			ta.setText("");
	 } 
  }
	
	public void saveFile(String str) { // 입력한 값을 여기에 넣는다.
		try {
			long name = System.currentTimeMillis();
			FileWriter fw = new FileWriter("ch13/" + name + ".txt");
			fw.write(str);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	public static void main(String[] args) {
		new FileWrite2Ex();
	}

}
