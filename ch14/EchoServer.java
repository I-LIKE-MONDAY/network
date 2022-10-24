package ch14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	
	int port = 8001;
	int cnt = 0;
	
	public EchoServer() {
		try {
			ServerSocket server = new ServerSocket(port);
			System.out.println("ServerSocket Start....");
			while(true) {
				Socket sock = server.accept(); //Client가 접속할 때까지 대기상태(thread) client가 소켓 들고 접속하는 순간 바로 작동
				EchoThread et = new EchoThread(sock);
				et.start();
				cnt++;
				System.out.println("Client" + cnt + "Socket");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//외부클래스와 아주 밀접한 관계가 있는 클래스, 장점은 상속이 가능하다.
	//내부클래스 EchoServer$EchoThread.class 이렇게 저장되어있다.
	class EchoThread extends Thread{
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		
		public EchoThread(Socket sock) {
			// 소켓 넘겨주고 인, 아웃 = 계속 반복해서 쓸 예정
			try {
				this.sock = sock;
				in = new BufferedReader(
						new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			try {
//				client가 접속을 하면 가장 먼저 받는 메세지
				out.println("Hello Enter Bye to exit");//print 뒤에 ln무조건 해야한다!!
				while(true) {
//					Client가 메세지 보내면 실행
					String line = in.readLine();
					if(line == null)
						break;
					else {
						out.println("Echo : " + line);
						if(line.equals("BYE"))
							break;
					}
				}
				in.close();
				out.close();
				sock.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}

	public static void main(String[] args) {
		new EchoServer();
	}

}
