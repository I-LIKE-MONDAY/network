package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.MarshalException;
import java.util.Vector;

public class ChatServer4 {

	Vector<ClientThread4> vc;
	ServerSocket server;
	int port = 8005;
	ChatMgr4 mgr; // 로그인과 관련 있는 객체
	MsgMgr4 mgr2; // 채팅과 관련 있는 객체

	public ChatServer4() {
		try {
			vc = new Vector<ClientThread4>();
			server = new ServerSocket(port);
			mgr = new ChatMgr4();
			mgr2 = new MsgMgr4();
		} catch (Exception e) {
			System.err.println("Error in Server");
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("****************************************");
		System.out.println("*Welcome Chat Server 4.0...");
		System.out.println("*클라이언트 접속을 기다리고 있습니다.");
		System.out.println("****************************************");
		try {
			while (true) {
				Socket sock = server.accept();
				ClientThread4 ct = new ClientThread4(sock);
				ct.start();
				vc.addElement(ct);
			}
		} catch (Exception e) {
			System.err.println("Error in Socket");
			e.printStackTrace();
		}
	}

	public void sendAllMessage(String msg) {
		for (int i = 0; i < vc.size(); i++) {
			ClientThread4 ct = vc.elementAt(i);
			ct.sendMessage(msg);
		}
	}

	public void removeClient(ClientThread4 ct) {
		vc.remove(ct);
	}

	// 접속된 모든 id 리스트 리턴 ex) aaa;bbb;ccc;ddd;홍길동;
	public String getIds() {
		String ids = "";
		for (int i = 0; i < vc.size(); i++) {
			ClientThread4 ct = vc.get(i);
			ids += ct.id + ";";
		}
		return ids;
	}

	// 매개변수 id값으로 ClientThread4를 검색
	public ClientThread4 findClient(String id) {
		ClientThread4 ct = null;
		for (int i = 0; i < vc.size(); i++) {
			ct = vc.get(i);
			if (ct.id.equals(id)) {// 매개변수 id와 Client의 id와 같다면...
				break;
			}
		} // --for
		return ct;
	}// --findClient

	class ClientThread4 extends Thread {

		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id = "익명";

		public ClientThread4(Socket sock) {
			try {
				this.sock = sock;
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter((sock.getOutputStream()), true);
				System.out.println(sock + " 접속됨...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					String line = in.readLine();
					if (line == null)
						break;
					else
						routine(line);
				}
			} catch (Exception e) {
				removeClient(this);
				System.err.println(sock + "[" + id + "] 끊어짐...");
			}
		}

		public void routine(String line) {
			//System.out.println("line:" + line);
			int idx = line.indexOf(ChatProtocol4.DM1);
			String cmd = line.substring(0, idx); 
			String data = line.substring(idx + 1); 
			//ID:aaa;1234
			if (cmd.equals(ChatProtocol4.ID)) {
				idx = data.indexOf(ChatProtocol4.DM2);
				cmd = data.substring(0, idx);//aaa
				data = data.substring(idx+1);//1234
				if(mgr.loginChk(cmd, data)) {
					//로그인 성공
					ClientThread4 ct = findClient(cmd);//aaa
					if(ct!=null&&ct.id.equals(cmd)) {
						//이중접속
						sendMessage(ChatProtocol4.ID+ChatProtocol4.DM1+"C");
					}else {
						id = cmd;
						sendMessage(ChatProtocol4.ID+ChatProtocol4.DM1+"T");
						sendAllMessage(ChatProtocol4.CHATLIST+ChatProtocol4.DM1+getIds());
						sendAllMessage(ChatProtocol4.CHATALL+ChatProtocol4.DM1+"["+id+"]님이 입장하였습니다.");
					}
				}else {
					//로그인 실패
					sendMessage(ChatProtocol4.ID+ChatProtocol4.DM1+"F");
				}
			} else if (cmd.equals(ChatProtocol4.CHAT)) {// CHAT:bbb;밥먹자
				idx = data.indexOf(ChatProtocol4.DM2);
				cmd/* bbb */ = data.substring(idx);
				data/* 밥먹자 */ = data.substring(idx + 1);
				// id : bbb를 가진 클라이언트를 찾아야 한다.
				ClientThread4 ct = findClient(cmd);
				if (ct != null) {// 상대방과 자신에게 보냄
					ct.sendMessage(ChatProtocol4.CHAT + ChatProtocol4.DM1 + "[" + id + "(S)]" + data); // bbb에게 날라가는것(상대방) , data =
					sendMessage(ChatProtocol4.CHAT + ChatProtocol4.DM1 + "[" + id + "(S)]" + data); // 자신(aaa)에게 날라옴(sendMessage)
				} else {// 자신에게 보내는것 (aaa)
					sendMessage(ChatProtocol4.CHAT + ChatProtocol4.DM1 + "[" + cmd + "]님이 접속자가 아닙니다.");
				}

			} else if (cmd.equals(ChatProtocol4.CHATALL)) {
				sendAllMessage(ChatProtocol4.CHATALL + ChatProtocol4.DM1 + "[" + id + "]" + data);
			} else if (cmd.equals(ChatProtocol4.MESSAGE)) {
				// 세미콜론 값 받아와서 짤라야함
				idx = data.indexOf(ChatProtocol4.DM2); // DM2 = ;
				cmd /* bbb */ = data.substring(0, idx); //fid : aaa, tid : bbb
				data /* 오늘 뭐해 */ = data.substring(idx+1);// msg : data
				ClientThread4 ct = findClient(cmd); // bbb를 찾음
				if(ct != null) { // 현재 접속자(bbb)가 있음
					MessageBean4 bean = new MessageBean4();
					bean.setFid(id); // aaa
					bean.setTid(cmd); // bbb
					bean.setMsg(data); // 오늘 뭐해
					mgr2.insertMsg(bean); // DB에 저장
					// DB접속과 별개로 쪽지의 내용이 들어있는 쪽지창
					ct.sendMessage(ChatProtocol4.MESSAGE + ChatProtocol4.DM1 + id/*aaa*/ + ChatProtocol4.DM2 + data); 
				} else { // bbb가 없음 -> 자신에게 보냄(aaa)
					sendMessage(ChatProtocol4.CHAT + ChatProtocol4.DM1 + "[" + cmd + "]님이 접속자가 아닙니다.");
				}
			}else if (cmd.equals(ChatProtocol4.MSGLIST)) {
				Vector<MessageBean4> vlist = mgr2.getMsgList(id);// id와 관련된 리스트를 받아서
				String str = "";
				for (int i = 0; i < vlist.size(); i++) {
					MessageBean4 bean = vlist.get(i);
					str += bean.getFid() + ",";
					str += bean.getTid() + ",";
					str += bean.getMsg() + ChatProtocol4.DM2/* ; */;
				}
				sendMessage(ChatProtocol4.MSGLIST + ChatProtocol4.DM1/* : */ + str);
			}
		}

		public void sendMessage(String msg) {
			out.println(msg);
		}
	}

	public static void main(String[] args) {
		new ChatServer4();
	}
}
