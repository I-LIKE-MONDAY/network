package talk04;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class TalkClient extends MFrame 
implements ActionListener, Runnable {

	Button  saveBtn, msgBtn, sendBtn;
	TextField sendTf;
	TextArea contentArea;
	List chatList;
	Socket sock;
	BufferedReader in;
	PrintWriter out;
	String title = "Talk 1.0";
	String listTitle = "*****CHAT LIST*****";
	boolean flag = false;
	String id;
	String label[] = {"SAVE", "MESSAGE", "SEND"};
	String str[] = {"바보","개새끼","새끼","자바","java"};
	Label chatTxt; // CHAT 라벨 추가

	public TalkClient(BufferedReader in, PrintWriter out, String id) {
		super(450, 500);
		this.in = in;
		this.out = out;
		this.id = id; 
		setTitle(title + " - " + id + "님 반갑습니다.");
		contentArea = new TextArea();
		contentArea.setBackground(Color.white);
		contentArea.setForeground(Color.black);
		contentArea.setEditable(false);
		add(BorderLayout.CENTER, contentArea);
		// /////////////////////////////////////////////////////////////////////////////////////////
		chatList = new List();
		
		Panel p2 = new Panel();
		add(BorderLayout.NORTH, p2);
		saveBtn = new Button(label[0]);
		saveBtn.addActionListener(this);
		p2.add(saveBtn);

		// ///////////////////////////////////////////////////////////////////////////////////////////
		Panel p4 = new Panel();
		sendTf = new TextField("", 30);
		sendTf.addActionListener(this);
		sendBtn = new Button(label[2]);
		sendBtn.addActionListener(this);
		chatTxt = new Label("CHAT");
		// 라벨 추가
		p4.add(chatTxt);
		p4.add(sendTf);
		p4.add(sendBtn);
		add(BorderLayout.SOUTH, p4);
		new Thread(this).start();
		validate();
	}
	
	public void run() {
		try {
			while(true) {
				String line = in.readLine();
				if(line==null)
					 break;
				else
					routine(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//--run
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==saveBtn/*save*/) {
			String content = contentArea.getText();
			//1970년1월1일 ~현재까지 1/1000초 단위로 계산
			long fileName = System.currentTimeMillis();
			try {
				FileWriter fw = new FileWriter("talk04/"+fileName+".txt");
				fw.write(content);
				fw.close();
				contentArea.setText("");
				new MDialog(this, "Save", "대화내용을 저장하였습니다.");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}else if(obj==msgBtn/*message*/) {
			int i = chatList.getSelectedIndex();
			if(i==-1||i==0) {
				new MDialog(this, "경고", "아이디를 선택하세요.");
			}else {
				new Message("TO:");
			}
		}else if(obj==sendBtn ||obj==sendTf) {
			String str = sendTf.getText();
			if(filterMgr(str)) {
				new MDialog(this, "경고", "입력하신 글자는 금지어입니다.");
				return;
			}
			int i = chatList.getSelectedIndex();
			if(i==-1||i==0) {//전체채팅
				sendMessage("CHATALL" + ":" + str);
			}else { //귓속말 채팅
				String id = chatList.getSelectedItem();
				sendMessage("CHAT" + ":" + id + ";" + str); // CHAT:id;str
			}
			sendTf.setText("");
			sendTf.requestFocus();
		}
	}//--actionPerformed

	public void routine(String line) {
		int idx = line.indexOf(':');
		String cmd = line.substring(0, idx); // 처음부터 : 까지 (CHAT:)
		String data = line.substring(idx+1); // id;str
		if(cmd.equals("CHATLIST")) {
			chatList.removeAll();
			chatList.add(listTitle);
			StringTokenizer st = new StringTokenizer(data, ";"); // id 추출
			while(st.hasMoreTokens()) {
				chatList.add(st.nextToken()); // chatList에 id 추가
			}
		}else if(cmd.equals("CHAT")||
				cmd.equals("CHATALL")){ // 처음부터 : 까지가 CHAT 이거나 CHATALL 이라면
			contentArea.append(data+"\n"); // contentArea에 내용 추가
		}else if(cmd.equals("MESSAGE")){ // 처음부터 : 까지가 MESSAGE 라면
			idx = data.indexOf(';'); //idx = ; 의 위치
			cmd = data.substring(0,idx); // cmd = id값
			data = data.substring(idx); // data = str값
			new Message("FROM", cmd, data); // FROM, id, str
		}
	}//--routine
	
	public void sendMessage(String msg) {
		out.println(msg);
	}

	public boolean filterMgr(String msg){
		boolean flag = false;//false이면 금지어 아님
		
		//msg : 하하 호호 히히
		StringTokenizer st = new StringTokenizer(msg);//생략하면 구분자는 공백
		String msgs[] = new String[st.countTokens()];
		for (int i = 0; i < msgs.length; i++) {
			msgs[i] = st.nextToken();
		}
		for (int i = 0; i < str.length; i++) {
			if(flag) break;//첫번째 for문 빠져나감.
			for (int j = 0; j < msgs.length; j++) {
				if(str[i].equalsIgnoreCase(msgs[j])) {
					flag = true;
					break; //두번째 for문 빠져나감.
				}//if
			}//for2
		}//for1
		return flag;
	}
	
	class Message extends Frame implements ActionListener {

		Button send, close;
		TextField name;
		TextArea ta;
		String mode;// to/from
		String id;

		public Message(String mode) {
			setTitle("쪽지보내기");
			this.mode = mode;
			id = chatList.getSelectedItem();
			layset("");
			validate();
		}
		public Message(String mode, String id, String msg) {
			setTitle("쪽지읽기");
			this.mode = mode;
			this.id = id;
			layset(msg);
			validate();
		}
		public void layset(String msg) {
			 addWindowListener(new WindowAdapter() {
				   public void windowClosing(WindowEvent e) {
				    dispose();
				   }
			});
			Panel p1 = new Panel();
			p1.add(new Label(mode, Label.CENTER));
			name = new TextField(id, 20);
			p1.add(name);
			add(BorderLayout.NORTH, p1);
			
			ta = new TextArea("");
			add(BorderLayout.CENTER, ta);
			ta.setText(msg);
			Panel p2 = new Panel();
			if (mode.equals("TO:")) {
				p2.add(send = new Button("send"));
				send.addActionListener(this);
			}
			p2.add(close = new Button("close"));
			close.addActionListener(this);
			add(BorderLayout.SOUTH, p2);
			
			setBounds(200, 200, 250, 250);
			setVisible(true);
		}

		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==send){
				sendMessage("MESSAGE" + ":" + id + ";" + ta.getText());
			}
			setVisible(false);
			dispose();
		}
	}

	class MDialog extends Dialog implements ActionListener{
		
		Button ok;
		TalkClient ct2;
		
		public MDialog(TalkClient ct2,String title, String msg) {
			super(ct2, title, true);
			this.ct2 = ct2;
			 //////////////////////////////////////////////////////////////////////////////////////////
			   addWindowListener(new WindowAdapter() {
			    public void windowClosing(WindowEvent e) {
			     dispose();
			    }
			   });
			   /////////////////////////////////////////////////////////////////////////////////////////
			   setLayout(new GridLayout(2,1));// 그리드 (행, 열)
			   Label label = new Label(msg, Label.CENTER);
			   add(label);
			   add(ok = new Button("확인"));
			   ok.addActionListener(this);
			   layset();
			   setVisible(true);
			   validate();
		}
		
		public void layset() {
			int x = ct2.getX();
			int y = ct2.getY();
			int w = ct2.getWidth();
			int h = ct2.getHeight();
			int w1 = 150;
			int h1 = 100;
			setBounds(x + w / 2 - w1 / 2, 
					y + h / 2 - h1 / 2, 200, 100);
		}

		public void actionPerformed(ActionEvent e) {
			sendTf.setText("");
			dispose();
		}
	}
}
