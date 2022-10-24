package ch14;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class InetAddressFrameEx extends MFrame 
implements ActionListener{
	
	TextField tf;
	TextArea ta;
	Button lookup;
	InetAddress intAddr;
	
	public InetAddressFrameEx() {
		setTitle("InetAddress Example");
		Panel p = new Panel();
		p.setLayout(new BorderLayout());
		p.add("North",new Label("호스트이름"));
		p.add(tf = new TextField("",40));
		p.add("South",lookup = new Button("호스트 정보 얻기"));
		tf.addActionListener(this);
		lookup.addActionListener(this);
		add("North",p);
		ta = new TextArea("인터넷주소\n");
		ta.setFont(new Font("Dialog",Font.BOLD,15));
		ta.setForeground(Color.BLUE);
		ta.setEditable(false);
		add(ta);
		validate();
	}
	
	
	public void actionPerformed(ActionEvent e) {
//		InetAddress adds[] = getAllByName("naver.com","daum.com");
		Object obj = e.getSource();
		if(obj == lookup||obj==tf) {
			String host = tf.getText().trim();
			try {    //의도치않은 예외를 없애기 위해 예외처리한다.
				
				intAddr = InetAddress.getByName(host);
				String add = intAddr.getHostName();
				String ip = intAddr.getHostAddress();
				ta.append("" + add + "\n");
				ta.append("" + ip + "\n");
				
			} catch (Exception e2) {
//				e2.printStackTrace();
				ta.append("[" + host + "]");
				ta.append("해당 호스트가 없습니다.\n");
			}
			tf.setText("");
			tf.requestFocus();
		}
	}
	


	public static void main(String[] args) {
		new InetAddressFrameEx();
//		for(int i = 0; i<add.length; i++) {
//			
//			System.out.println("naver : " + adds[i]);
//			System.out.println("인터넷 주소 : \t" + adds[i].getHostAddress());
//			add = InetAddress.getByName("auction.co.kr");
//			System.out.println("옥션 Host Adress : " + add.getHostAddress());
			
		}
			
	
}




























