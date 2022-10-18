package ch13;

import java.io.FileWriter;

public class FileWriterEx {

	public static void main(String[] args) {
		String str = "Dream as if you'll live forever,\n"
				+ "Live as if you'll die today - 제임스딘";
		
		try {
			FileWriter fw = new FileWriter("ch13/bbb.txt"); // 파일에 빨대꽂음 
			fw.write(str);
			fw.flush();
			fw.close();
			System.out.println("End~~");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
