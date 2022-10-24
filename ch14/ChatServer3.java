package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer3 {
	
	ServerSocket server;
	int port = 8004;
	Vector<ClientThread3> vc;
	ChatMgr3 mgr;
	
	public ChatServer3() {
		try {
			server = new ServerSocket(port);
			vc = new Vector<ClientThread3>();
			mgr = new ChatMgr3();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in Server");
			System.exit(1);//비정상적인 종료
		}
		System.out.println("****************************");
		System.out.println("Welcome Server 3.0...");
		System.out.println("클라이언트의 접속을 기다리고 있습니다.");
		System.out.println("****************************");
		try {
			while(true) {
				Socket sock = server.accept();
				ClientThread3 ct = new ClientThread3(sock);
				ct.start();//쓰레드 스케줄러에 등록 -> run 메소드 호출
				vc.addElement(ct);//ClientThread 객체를 벡터에 저장
			}
		} catch (Exception e) {
			System.err.println("Error in sock");
			e.printStackTrace();
		}
	}
	
	//모든 접속된 Client에게 메세지 전달
	public void sendAllMessage(String msg) {
		for (int i = 0; i < vc.size(); i++) {
			//Vector에 저장된 ClientThread를 순차적으로 자져옴
			ClientThread3 ct = vc.get(i);
			//ClientThread 가지고 있는 각각의 메세지 보내는 메소드 호출
			ct.sendMessage(msg);
		}
	}
	
	//접속이 끊어진 ClientThread는 벡터에서 제거
	public void removeClient(ClientThread3 ct) {
		vc.remove(ct);
	}
	
	//접속된 모든 id 리스트 ex)aaa;bbb;ccc;강호동;
	public String getIds() {
		String ids = "";
		for (int i = 0; i < vc.size(); i++) {
			ClientThread3 ct = vc.get(i);
			ids+=ct.id+";";
		}
		return ids;
	}
	
	//지정한 ClientThread2 검색
	public  ClientThread3 findClient(String id) {
		ClientThread3 ct = null;
		for (int i = 0; i < vc.size(); i++) {
			ct = vc.get(i);
			if(id.equals(ct.id)) {
				break;
			}//--if
		}//--for
		return ct;
	}
	
	class ClientThread3 extends Thread{
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id;
		
		public ClientThread3(Socket sock) {
			try {
				this.sock = sock;
				in = new BufferedReader(
						new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(), true);
				System.out.println(sock + " 접속됨...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				while(true) {
					String line = in.readLine();
					if(line==null)
						break;
					else
						routine(line);
				}
				in.close();
				out.close();
				sock.close();
				removeClient(this);
			} catch (Exception e) {
				removeClient(this);
				System.err.println(sock + "[" + id +"] 끊어짐...");
			}
		}
		
		public  void routine(String line) {
            System.out.println("line : " + line);
			int idx = line.indexOf(ChatProtocol3.DM);//;
			String cmd = line.substring(0, idx);//CHATALL
			String data = line.substring(idx+1);//[aaa]오늘은 월요일입니다.
			if(cmd.equals(ChatProtocol3.ID)) {
				idx = data.indexOf(";");//aaa;1234
	            cmd = data.substring(0, idx);//aaa
	            data = data.substring(idx+1);//1234
	            if(mgr.loginChk(cmd, data)) {
	            	// 접속한 아이디로 기존의 Client가 있는지 검색
	            	ClientThread3 ct = findClient(cmd);
	            	// ct.equals(ID) 하면 에러남
	            	if(ct != null && ct.id != null && ct.id.equals(cmd)) { //이중접속(접속실패)
	            		sendMessage(ChatProtocol3.ID + ChatProtocol3.DM + "C");
	            	} else { // 접속 성공
	            		id = cmd; // aaa
	            		sendMessage(ChatProtocol3.ID + ChatProtocol3.DM + "T");
	            		sendAllMessage(ChatProtocol3.CHATLIST + ChatProtocol3.DM + getIds());// 주의! getId가 아니라 getIds임!
	            		sendAllMessage(ChatProtocol3.CHATALL + ChatProtocol3.DM + "[" + id + "]님이 입장하였습니다.");
	            	}
	            }else { // 로그인 실패
	            	sendMessage(ChatProtocol3.ID + ChatProtocol3.DM + "F"); // 로그인 실패하면 F 가 날아감
	            }
	       
//	            System.out.println(mgr.loginChk(cmd, data)); //DB 연동 메소드
			}else if(cmd.equals(ChatProtocol3.CHAT)) {
				idx = data.indexOf(';');
				cmd = data.substring(0, idx);//bbb
				data = data.substring(idx+1);//data
				ClientThread3 ct = findClient(cmd);
				if(ct!=null) {//현재 접속자
					ct.sendMessage(ChatProtocol3.CHAT+ChatProtocol3.DM+"["
					+id+"(S)]" + data);//CHAT:[aaa(S)]밥먹자
					sendMessage(ChatProtocol3.CHAT+ChatProtocol3.DM+"["
							+id+"(S)]" + data);//CHAT:[aaa(S)]밥먹자
				}else {//bbb가 접속이 안된 경우
					sendMessage(ChatProtocol3.CHAT+ChatProtocol3.DM+"["+
							cmd+"]님 접속자가 아닙니다.");
				}
			}else if(cmd.equals(ChatProtocol3.MESSAGE)) {
				idx = data.indexOf(';');
				cmd = data.substring(0, idx);//bbb
				data = data.substring(idx+1);//data
				ClientThread3 ct = findClient(cmd);
				if(ct!=null) {//현재 접속자
					ct.sendMessage(ChatProtocol3.MESSAGE+ChatProtocol3.DM+
					id+";" + data);
				}else {//bbb가 접속이 안된 경우
					sendMessage(ChatProtocol3.CHAT+ChatProtocol3.DM+"["+
							cmd+"]님 접속자가 아닙니다.");
				}
			}else if(cmd.equals(ChatProtocol3.CHATALL)) {
				sendAllMessage(ChatProtocol3.CHATALL+ChatProtocol3.DM+"["+
						id+"]"+data);
			}
		}

		//Client에게 메세지 전달 메소드
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	
	public static void main(String[] args) {
		new ChatServer3();
	}

}





