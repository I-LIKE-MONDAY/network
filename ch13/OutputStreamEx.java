package ch13;
import java.io.IOException;
import java.io.OutputStream;
public class OutputStreamEx {

	public static void main(String[] args) /*throws Exception*/{
      OutputStream out = System.out;
      int i = 'A';
      char c = 'b';
      char c1 = '한';
      try {
		out.write(i);
		out.write(c);
		out.write(c1);
		out.flush(); //스트림에 남아있는 data를 비운다.
		out.close();
	} catch (IOException e) {
		e.printStackTrace();
	}  //print ,write 같은 것.
      
      
   }
}
