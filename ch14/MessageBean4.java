package ch14;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Data
@Getter @Setter
public class MessageBean4 {
	
	// Beans 만드는법 : getter, setter 불러와야함
	// 롬복 사용 : int no ~ String mdate 까지 선언하고 lombok.data 임포트 -> @Data 클래스 밖에 입력
	private int no;
	private String fid;
	private String tid;
	private String msg;
	private String mdate;
	

	
	
	
}
