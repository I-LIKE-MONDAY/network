package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer1 {
	
	ServerSocket server;
	int port = 8002;
	// Vector<저장시킬 데이터타입> : 제네릭
	Vector<ClientThread1> vc;
	
	
	public ChatServer1() {
		try { // 서버 시작
			server = new ServerSocket(port);
			vc = new Vector<ChatServer1.ClientThread1>(); 
		
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error in Server");
			// 1은 비정상적인 종료를 의미 ( 매개변수가 0이면 정상적인 종료) : 일반적으로사용
			System.exit(1); 
		}
		System.out.println("*ChatServer1*********************");
		System.out.println("*client 접속을 기다리고있습니다.*");
		System.out.println("*********************************");
		
		try {
			while(true) { // 무한반복문
				Socket sock = server.accept();
				ClientThread1 ct = new ClientThread1(sock);
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
			ClientThread1 ct = vc.get(i);
			// ClientThread 가지고 있는 각각의 메세지 보내는 메소드 호출
			// 			-> 모든 접속되어있는 클라이언트에게 동일한 메세지가 보내짐
			ct.sendMessage(msg);
		}
	}
	
	// 접속이 끊어진 ClientThread는 Vector에서 제거해줘야함
	public void removeClient(ClientThread1 ct) {
		vc.remove(ct);
	}
	
	
	
	class ClientThread1 extends Thread{ // 내부클래스
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id; // 사용자 id를 저장할 변수 id 생성
		
		public ClientThread1(Socket sock) { // 생성자
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
				id = in.readLine();
				// 모든 사용자들에게 접속한 사람의 welcome 메세지 전달
				sendAllMessage("[" + id + "]님이 입장하였습니다.");
				String line = "";
				while(true) {
					line = in.readLine(); // 메세지 들어올때까지 대기상태
					if(line == null)
						break;
					sendAllMessage("[" + id + "]" + line);
					} // --while
					in.close();
					out.close();
					removeClient(this);				
			} catch (Exception e) { // 채팅 실행(run)중 문제가 생겼을때
				removeClient(this); // 현재 객체 자신(Vector 내부의)
				System.err.println(sock + "끊어짐...");
			}
		}
		
		// Client 에게 메세지 전달 메소드
		public void sendMessage(String msg) {
			out.println(msg);
		}
	}
	
	public static void main(String[] args) {
			new ChatServer1();

	}
}


