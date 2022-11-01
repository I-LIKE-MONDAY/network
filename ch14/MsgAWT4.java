package ch14;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.Vector;

public class MsgAWT4 extends MFrame implements ActionListener{
	
	ChatClient4 client;
	String id;
	String title = "Message";
	Button closeBtn;
	List list;
	Vector<MessageBean4> vlist;
	String str;
	String label[] = {"Fid", "Tid", "Msg"};
	
	public MsgAWT4(ChatClient4 client, String str) {
		super(300, 300, new Color(200, 100, 100));
		this.client = client;
		this.str = str;
		setTitle(title);
		list = new List();
		add(list, BorderLayout.CENTER);
		closeBtn = new Button("Close");
		closeBtn.addActionListener(this);
		Panel p = new Panel();
		p.add(closeBtn);
		add(p, BorderLayout.SOUTH);
		addListMsg();
		validate();
	}

	public void addListMsg() {
		// aaa,bbb,밥먹자;bbb,aaa,OK <- 해당 방식으로 넘어옴
		// StringTokenizer: str을 ; 단위로 문자열을 잘라줌
		StringTokenizer st1 = new StringTokenizer(str, ChatProtocol4.DM2/* ; */); 
		while(st1.hasMoreElements()) {
			StringTokenizer st2 = new StringTokenizer(st1.nextToken(), ", "); // st1을 , 단위로 잘라서 저장
			String item = "";
			for(int i = 0; i < label.length; i++) {
				item += label[i] + ChatProtocol4.DM1/* : */ + st2.nextToken() + " ";
			}
			list.add(item);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==closeBtn) {
			dispose();
		}
	}
}






