package ch14;

public class ChatProtocol4 {
	
		// (클라이언트(C) -> 서버(S)) ID:aaa
		// (S->C) CHATLIST:aaa;bbb;ccc;강호동;
		public static final String ID = "ID"; // 상수로 선언 : 오타발생가능성 줄이기위해
		
		// (C->S) CHAT:받는아이디;메세지 ex)CHAT:bbb;밥먹자 (aaa가 bbb에게 "밥먹자" 귓속말)
		// (S->C) CHAT:보내는아이디;메세지 ex)CHAT:aaa;밥먹자
		public static final String CHAT = "CHAT";
		
		// 전체대화
		// (C->S) CHATALL:메세지
		// (S->C) CHATALL:[보낸아이디]메세지
		public static final String CHATALL = "CHATALL";
		
		// (C->S) MESSAGE:받는아이디;쪽지내용 ex)MESSAGE:bbb;지금 어디?
		// (S->C) MESSAGE:받는아이디;쪽지내용 ex)MESSAGE:aaa;지금 어디?
		public static final String MESSAGE = "MESSAGE";
		
		// (S->C) CHATLIST:aaa;bbb;ccc;강호동
		public static final String CHATLIST = "CHATLIST";
		
		public static final String MSGLIST = "MSGLIST";
		
		// 구분자 -> 프로토콜:data(delimiter)
		public static final String DM1 = ":";
		
		public static final String DM2 = ";";
}
