package ch14;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class LoginAWT4 extends MFrame implements ActionListener {

	TextField idTf, pwTf;
	Label logo, idl, pwl, msgl;
	Button logBtn;
	Socket sock;
	BufferedReader in;
	PrintWriter out;
	String id;
	String host = "127.0.0.1";
	int port = 8005;
	String title = "MyChat 4.0";
	String label[] = { "ID와 PWD를 입력하세요.", "ID와 PWD를 확인하세요.", "이중 접속입니다." };

	public LoginAWT4() {
		super(450, 400, new Color(100, 200, 100));
		setLayout(null);
		setTitle(title);
		logo = new Label(title);
		logo.setFont(new Font("Dialog", Font.BOLD, 50));

		idl = new Label("ID");
		pwl = new Label("PWD");
		idTf = new TextField("aaa");
		pwTf = new TextField("1234");
		logBtn = new Button("로그인");
		msgl = new Label(label[0]);
		logo.setBounds(100, 50, 280, 100);
		idl.setBounds(150, 200, 50, 20);
		idTf.setBounds(200, 200, 100, 20);
		pwl.setBounds(150, 230, 50, 20);
		pwTf.setBounds(200, 230, 100, 20);
		logBtn.setBounds(150, 260, 150, 40);
		msgl.setBounds(150, 320, 150, 40);
		logBtn.addActionListener(this);
		add(logo);
		add(idl);
		add(idTf);
		add(pwl);
		add(pwTf);
		add(logBtn);
		add(msgl);
		validate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Object obj = e.getSource();
			if (obj == logBtn) {
				if (sock == null) {
					connect();
				}
				id = idTf.getText().trim();
				out.println(ChatProtocol4.ID + ChatProtocol4.DM1 + id 
						+ ChatProtocol4.DM2 + pwTf.getText().trim());
				String line = in.readLine();
				int idx = line.indexOf(ChatProtocol4.DM1);
				String cmd = line.substring(0, idx); 
				String data = line.substring(idx + 1);
				if(cmd.equals(ChatProtocol3.ID)) {
					if(data.equals("F")) {
						msgl.setForeground(Color.RED);
						msgl.setText(label[1]);
					}else if(data.equals("C")) {
						msgl.setForeground(Color.BLUE);
						msgl.setText(label[2]);
					}else if(data.equals("T")) {
						dispose();//LoginAWT 사라지고
						new ChatClient4(in, out, id);
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void connect() {
		try {
			sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true/* auto flush */);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}// --connect

	public static void main(String[] args) {
		new LoginAWT4();
	}
}
