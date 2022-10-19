package ch13;

import java.io.InputStream;

public class InputStreamEx { 

	public static void main(String[] args) {
	InputStream in =System.in; 
	//키보드. 이탤릭체로 살짝 누워있는 건 static 변수. 괄호있는 건 메소드. 괄호없는건 필드.
	// InputStream in => in이 키보드와 자바를 연결시켜준다. in은 일방통행을 데이터 들어오는 걸 뜻함.
	
	try {
		//throws 메소드는 반드시 예외처리 해야 함.
		while(true) {
		int i = in.read(); //키보드 입력 전까지 대기 상태 -> 키보드 입력 실행
		if(i==-1) break; //ctrl + z
		System.out.print((char)i);
		//try 일어날 가능성이 있는 것들을 넣어놓는다. 예외처리해놓은것.
		//예외를 잡고나면 catch로 간다.
		}
	} catch (Exception e) {
		//e는 예외의 객체. rapper(?)변수.
		e.printStackTrace();//예외가 일어난 경로 및 메시지
	}
  }
}
