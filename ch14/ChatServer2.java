package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Vector;

public class ChatServer2 {
	
	ServerSocket server;
	int port = 8003;
	// Vector<저장시킬 데이터타입> : 제네릭
	Vector<ClientThread2> vc;
	
	
	public ChatServer2() {
		try { // 서버 시작
			server = new ServerSocket(port);
			vc = new Vector<ClientThread2>(); 
		
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in Server");
			// 1은 비정상적인 종료를 의미 ( 매개변수가 0이면 정상적인 종료) : 일반적으로사용
			System.exit(1); 
		}
		System.out.println("*ChatServer 2.0*********************");
		System.out.println("*client 접속을 기다리고있습니다.****");
		System.out.println("************************************");
		
		try {
			while(true) { // 무한반복문
				Socket sock = server.accept();
				ClientThread2 ct = new ClientThread2(sock);
				ct.start(); // 쓰레드 스케쥴러에 등록 -> run 메소드 호출
				vc.addElement(ct); // ClientThread 객체를 벡터에 저장
			}
			
		} catch (Exception e) {
			System.err.println("Error in sock");
			e.printStackTrace();
		}
	}
	
	// 모든 접속된 Client에게 메세지 전달하는 메서드 sendAllMessage()
	public void sendAllMessage(String msg) {
		// A가 Vector로 묶인 서버에 "안녕" 보내면 A, B, C 모두에게 메세지가 가야함
		for (int i = 0; i < vc.size(); i++) {
			// Vector에 저장된 ClientThread를 순차적으로 가져옴
			ClientThread2 ct = vc.get(i);
			// ClientThread 가지고 있는 각각의 메세지 보내는 메소드 호출
			// 			-> 모든 접속되어있는 클라이언트에게 동일한 메세지가 보내짐
			ct.sendMessage(msg);
		}
	}
	
	// 접속이 끊어진 ClientThread는 Vector에서 제거해줘야함
	public void removeClient(ClientThread2 ct) {
		vc.remove(ct);
	}
	
	// 접속된 모든 id 리스트 ex)aaa;bbb;ccc;강호동
	public String getIds() {
		String ids = ""; 
		for(int i = 0; i < vc.size(); i++) {
			ClientThread2 ct = vc.get(i);
			ids += ct.id + ";";
		}
		return ids;
	}
	
	
	// 지정한 ClientThread2 검색
	public ClientThread2 findClient(String id) { // ??
		ClientThread2 ct = null;
		for(int i = 0; i < vc.size(); i++) {
			ct = vc.get(i);
			if(id.equals(ct.id)) {
				break; // 찾은거임(10개중에 3번째에서 찾았으면 나머지 7개는 볼 필요 없으니까)
			}//--if
		}//--for
		return ct;
	}
	
	
	class ClientThread2 extends Thread{ // 내부클래스
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id; // 사용자 id를 저장할 변수 id 생성
		
		public ClientThread2(Socket sock) { // 생성자
			// 반복사용 try - catch문
			try {
				this.sock = sock;
				in = new BufferedReader(
						new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(), true);
				System.out.println(sock + "접속됨...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			
			try {
				// Client가 처음으로 받는 메세지
				out.println("사용하실 아이디를 입력하세요.");
				while(true) {
					String line = in.readLine();
					if(line == null)
						break;
					else
						routine(line);
				}
				in.close(); // 오류나는이유? 죽은 코드이기때문(절대 실행될수없는 코드)
				out.close();
				removeClient(this);				
			} catch (Exception e) { // 채팅 실행(run)중 문제가 생겼을때
				removeClient(this); // 현재 객체 자신(Vector 내부의)
				System.err.println(sock + "끊어짐...");
			}
		}
		
		private void routine(String line) {
			// CHATALL:[aaa]오늘은 월요일입니다. <- 프로토콜과 메세지를 짤라야함
			System.out.println(line);
			int idx = line.indexOf(ChatProtocol2.DM); // : (콜론)을 의미
			String cmd = line.substring(0, idx); // 0부터 : 까지 = << CHATALL >>
			String data = line.substring(idx + 1); // : 바로 뒤 위치부터 끝까지 = << [aaa]오늘은 월요일입니다. >>
			if(cmd.equals(ChatProtocol2.ID)) {
				id = data; // aaa
				// 새로운 접속자가 추가 되었기 때문에 리스트 재전송 해야함
				sendAllMessage(ChatProtocol2.CHATLIST + ChatProtocol2.DM + getIds());
				// welcome 메세지 전송
				sendAllMessage(ChatProtocol2.CHATALL + ChatProtocol2.DM + "[" + id + "]님이 입장하였습니다.");
			} else if(cmd.equals(ChatProtocol2.CHAT)) {
				// data : bbb;밥먹자    (받는아이디;메세지)
				idx = data.indexOf(';'); // idx는 세미콜론(;) 의 위치값을 얻어옴
				cmd = data.substring(0, idx); // bbb
				data = data.substring(idx + 1); // data
				ClientThread2 ct = findClient(cmd);
				if(ct != null) { // 현재 접속자
					// ct는 bbb 접속자
					ct.sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM + "[" + id + "(S)]" + data);// CHAT:[aaa(S)]밥먹자
					sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM + "[" + id + "(S)]" + data); // CHAT:[aaa(S)]밥먹자
				} else { // bbb가 접속이 안 된 경우
					sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM + "[" + cmd + "]님 접속자가 아닙니다.");
				}
			} else if(cmd.equals(ChatProtocol2.MESSAGE)) {
				idx = data.indexOf(';'); // idx는 세미콜론(;) 의 위치값을 얻어옴
				cmd = data.substring(0, idx); // bbb
				data = data.substring(idx + 1); // data
				ClientThread2 ct = findClient(cmd);
				if(ct != null) { // 현재 접속자
					// ct는 bbb 접속자
					ct.sendMessage(ChatProtocol2.MESSAGE + ChatProtocol2.DM + id + ";" + data);// CHAT:aaa;밥먹자
				} else { // bbb가 접속이 안 된 경우
					sendMessage(ChatProtocol2.CHAT + ChatProtocol2.DM + "[" + cmd + "]님 접속자가 아닙니다.");
				}
			} else if(cmd.equals(ChatProtocol2.CHATALL)) {
				sendAllMessage(ChatProtocol2.CHATALL + ChatProtocol2.DM + "[" + id + "]" + data);
			}
		}//-- routine()
		
		
		// Client 에게 메세지 전달 메소드
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	
	public static void main(String[] args) {
			new ChatServer2();

	}
}


