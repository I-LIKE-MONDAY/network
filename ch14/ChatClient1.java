package ch14;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;

// Runnable 은 쓰레드 시작부분이 좀 다름 -> 아래의 connect() 메서드 참고
public class ChatClient1 extends MFrame implements ActionListener, Runnable{
	
	Button btn1, btn2;
	TextField tf1, tf2;
	TextArea ta;
	Panel p1, p2;
	BufferedReader in;
	PrintWriter out;
	int port = 8002;
	String id;
	String str[] =  {"개새끼","새끼","자바","java"}; // 필터링용 배열 필드. 로컬변수
	
	public ChatClient1() {
		super(350,400);
		setTitle("MyChat 1.0");
		p1 = new Panel();
		p1.setBackground(new Color(100,200,100));
		p1.add(new Label("HOST ",Label.CENTER));
		p1.add(tf1 = new TextField("127.0.0.1",25));
		//p1.add(tf1 = new TextField("10.100.204.62",25));
		p1.add(btn1 = new Button("Connect"));
		
		p2 = new Panel();
		p2.setBackground(new Color(100,200,100));
		p2.add(new Label("CHAT ",Label.CENTER));
		p2.add(tf2 = new TextField("",25));
		p2.add(btn2 = new Button("SEND"));	
		
		tf1.addActionListener(this);
		tf2.addActionListener(this);
		btn1.addActionListener(this);
		btn2.addActionListener(this);
		
		add(p1,BorderLayout.NORTH);
		add(ta=new TextArea());
		add(p2,BorderLayout.SOUTH);
		validate();//갱신
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object obj = e.getSource();
		if (obj == tf1 || obj == btn1) {
			connect(tf1.getText().trim());
			tf1.setEnabled(false);
			btn1.setEnabled(false);
			tf2.requestFocus();
		} else if (obj == tf2 || obj == btn2) {
			String str = tf2.getText().trim();
			if(str.length() == 0)
				return; // if에서의 return은 메소드 종료를 의미. 반복문으로 따지면 break와 동일한 기능
			
			
			// 필터링
			if(filterStr(str)) { // 금지어가 포함 filterStr(str)이 true일때만 if문이 실행되기때문
				ta.append("입력하신 글자는 금지어가 포함 되어있습니다.\n");
				tf2.setText("");
				tf2.requestFocus();
				return;
			}
			
		
			if(id == null) { // 제일 처음에만 실행
				id = str;
				setTitle(getTitle() + "[" + id + "]");
				ta.setText("채팅을 시작합니다.\n");
			}
			out.println(str); // 서버로 입력한 문자열을 보냄.
	        tf2.setText("");
	        tf2.requestFocus();
		
		}
	}//--actionPerformed
	
	 // 금지어 필터링. 메소드 네임은 첫단어는 동사가 오는게 좋음(기능)
	public boolean filterStr(String target) {
		boolean flag = false;
		for (int i = 0; i < str.length; i++) {
			if(target.contains(str[i])) {
				flag = true; // 금지어가 포함이 되어있다면 flag는 true
				break;
			}
		}
		return flag; // 포함이 되어있었다면 true가 리턴되고 포함되어있지 않앗다면 false가 리턴됨
	}
	
	@Override
	// 서버로부터 메세지가 들어오면 반응하는 기능
	public void run() {
		
		try {
			while(true) {	
				// 서버에서 메세지 전달되면 ta에 append
			ta.append(in.readLine() + "\n");
			// 채팅 입력창에 포커스 
			// (채팅메세지 데이터가 들어왔을때 거기로 포커스가 자동이동되는데 그걸 방지하기 위함->메세지입력창에 포커스 고정)
			tf2.requestFocus();
			}  // 채팅입력창
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//--run
	
	
	// connect
	public void connect(String host){
		try {
			Socket sock = new Socket(host,port);
			in = new BufferedReader(
					new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);
		
			// 서버 접속후에 쓰레드 시작 (Runnable 을 implement 했을때 쓰레드 시작하는방법)
			Thread t = new Thread(this);
			t.start(); // run() 메소드 호출
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//--connect
	
	public static void main(String[] args) {
		new ChatClient1();
	}
}



