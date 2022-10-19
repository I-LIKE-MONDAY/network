package ch13;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BufferReaderEx1 {

	public static void main(String[] args) {
		
		InputStream is = System.in;
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		///////////////////////////////////////////////////////////////
		
		BufferedReader br1 =  //버퍼의 기능이 있으면서도 문자열로 받아들이며
				new BufferedReader(
				new InputStreamReader(System.in));
		
		String s = "";
		while(true) {
			try {
				s = br1.readLine(); //BufferedReader, readLine() 이거 하면 채팅 프로그램 거의 다 완성된다.
				System.out.println("출력 : " + s);
			} catch (Exception e) {
				e.printStackTrace(); //에러가 난 위치를 보여줌.
			}
		}
	}

}
