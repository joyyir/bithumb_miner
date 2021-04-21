==============================
실행 방법
==============================
0) JDK 11 설치
- https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html

1) 실행 파일을 메모장에서 수정 (윈도우는 run.bat, macOS는 run.sh)
- 본인 빗썸 계정의 Connect Key와 Secret Key를 입력하고 저장
- 다계정 사용자를 위해 port도 설정할 수 있도록 함. 기본은 8080

2) 실행 파일 실행
- 윈도우 : run.bat 더블 클릭
- macOS : sh ./run.sh

3) 브라우저에서 http://localhost:{port} 접속 (ex. http://localhost:8080)

4) 현금 최대 한도와 구매할 코인, 총 사이클 수를 입력하고 start 버튼 클릭

5) 봇이 실행된다. 터미널 창에서 여러 로그를 보면서 봇이 하는 일을 관찰한다.


==============================
오류 해결
==============================
- java.lang.UnsupportedClassVersionError가 나는 경우 : JDK 11 설치