package ch13;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ReaderEx {

	public static void main(String[] args) {
		InputStream is = System.in;
		//1바이트 단위로 들어온 값을 문자 단위로 처리
		Reader reader = new InputStreamReader(is);
		while (true) {
			try {
				int i = reader.read();
				if(i==-1) break;
				System.out.println((char)i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
