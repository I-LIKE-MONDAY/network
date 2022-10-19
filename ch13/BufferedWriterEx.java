package ch13;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class BufferedWriterEx {

	public static void main(String[] args) {

		String s = "오늘은 즐거운 화요일입니다.";
		BufferedWriter bw =
				new BufferedWriter(
						new OutputStreamWriter(System.out));
		try {
			bw.write(s,0,s.length());
			bw.newLine();
			bw.write(s);
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

