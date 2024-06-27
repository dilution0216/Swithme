# 프로젝트 명 : SWITHME(Study With Me)📕 

<br>스윗미는 게시판, 그룹, 캘린더, 스톱워치, 채팅서비스를 제공하여 공부를 위한 환경 조성/커뮤니케이션 웹사이트입니다.

• 개발 기간 : 2023.08.26. ~ 2023.09.18.(3주)
• 개발 인원 : 4명
<br> • 팀 노션 : [URL](https://rowan-pufferfish-a5a.notion.site/SWITHME-7f7439e749f14811b222065339171abe)
<br> • 팀 깃허브 : [URL](https://github.com/sparta-ecochiko/swithme)
 
 <br>
<h2><a href="https://rowan-pufferfish-a5a.notion.site/SWITHME-884f77ca6e294588a2ffe745900de845" >🗄️ 아키텍쳐</a></h2>
<img src="https://drive.google.com/uc?id=14yPLmV5HJks5JwxcZH2yNLUyuK4ILX4o" />

<br>

<h2>🌐 ERD & API</h2>
<a href="https://www.erdcloud.com/d/nsyTLkMBDYAbSdHum" > ERD </a> &
<a href="https://young-baron-d39.notion.site/7c369a586ec944ba80a535197a03bc6a?v=6fc3cb1ffa3c433aa086ecff7bfdc40d&pvs=4" > API</a>

<br>
<br>
<br>

### **주요 역할 : 공부시간 기록 기능 개발**

---

- 자바스크립트 기반 스톱워치로 시간 측정 시, 백엔드에서 처리 후 MySQL DB 내 저장하도록 구현
- '오늘의 공부시간'과 '전체 누적 공부시간'을 효율적으로 관리 및 조회할 수 있도록 설계
- 팀장의 일정관리와 리소스 분배 역할
    - 전반적인 **기획, 일정관리, 업무 분담, 커뮤니케이션 증대, 백엔드 개발**
    - 서비스의 흐름, 기능 동작에 대한 이해와 커뮤니케이션을 위한 **인포메이션 아키텍쳐 작성**
    - 체계적인 개발과 **의사소통을 위한 코드 컨벤션, 커밋 컨벤션 기획**
<br>

### **기술적 특징 및 구현 세부사항**

---

- **MVC 아키텍처 적용**
    - **Controller, Service, Repository의 구조**를 활용하여 코드의 응집도를 높이고 결합도를 낮춤. 각 레이어가 가지는 책임을 명확히 구분하여 유지보수성 향상
- **예외 처리**
    - RecordTimeException 같은 **커스텀 예외처리를 도입**하여, 예상 가능한 문제 상황에 대해 미리 대응하여 사용자 경험 개선
- **JPA 최적화**
    - UserRepository와 AccumulatedTimeRepository의 **쿼리 최적화**를 통해 데이터베이스의 부하를 줄이고 응답 속도를 개선하였고, 없는 데이터에 대한 처리로 인한 불필요한 데이터베이스 액세스를 최소화
- **시간 파싱 로직**
    - 사용자에게 입력받은 시간 문자열을 분 단위로 파싱하는 로직을 구현, 데이터 일관성 유지
- **Spring Security 활용**
    - `@AuthenticationPrincipal` 어노테이션 활용하여 로그인된 사용자의 정보를 안전하게 처리
- **데이터 무결성 유지**
    - 현재 날짜와 마지막 기록 날짜를 비교하여 일자가 변경될 경우, 누적 시간을 초기화하는 로직을 도입하여 데이터의 신뢰성을 향상

<br>
<br>
<br>
<br>

### 컴퓨터 기초이론을 실제 적용하여 작성한 코드

<br>

### **1.자료구조 적용**

'공부 시간 기록 기능'을 구현할 때, 사용자별 공부 시간을 관리하기 위해 **배열**과 **해시맵**을 사용함. 배열은 시간 기록을 순차적으로 저장하는 데 사용되었고, 해시맵은 사용자별로 시간 기록을 빠르게 조회하고 관리하기 위해 활용

swithme/src/main/java/com/sparta/swithme/service/RecordService.java

```java
@Service
public class RecordService {

    private final RecordRepository recordRepository;

    // Constructor
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public List<StudyRecord> getRecordsForUser(Long userId) {
        // 데이터를 해시맵 구조로 저장하여 사용자별로 빠르게 접근 가능
        HashMap<Long, List<StudyRecord>> userRecordsMap = new HashMap<>();
        
        // 사용자의 전체 공부 기록을 리스트로 관리
        List<StudyRecord> userRecords = recordRepository.findByUserId(userId);
        userRecordsMap.put(userId, userRecords);
        
        return userRecords;
    }
}

```

### **2. 알고리즘 적용**

사용자가 입력한 시간 문자열을 분 단위로 파싱하여, 일관된 형식으로 변환하는 알고리즘을 구현

swithme/src/main/java/com/sparta/swithme/service/TimeParsingService.java

```java
@Service
public class TimeParsingService {

    public int parseTimeToMinutes(String timeString) {
        // 정규 표현식을 사용하여 시간 문자열을 분 단위로 파싱
        Pattern pattern = Pattern.compile("(\\d+):(\\d+)");
        Matcher matcher = pattern.matcher(timeString);

        if (matcher.matches()) {
            int hours = Integer.parseInt(matcher.group(1));
            int minutes = Integer.parseInt(matcher.group(2));
            return hours * 60 + minutes;
        } else {
            throw new IllegalArgumentException("Invalid time format");
        }
    }
}
```

**정규 표현식(Regular Expression)** 을 사용하여 시간 문자열을 분 단위로 파싱하는 알고리즘을 구현

사용자가 입력한 시간 문자열이 HH:MM 형식인지 확인하고, 이를 분 단위로 변환하여 저장함. 이를 통해 ‘공부시간’ 이라는 데이터의 일관성을 유지

### **3. 컴퓨터 구조 적용**

MVC 아키텍처의 Controller 부분에서 HTTP 요청을 처리하고, 서비스 계층으로 전달

swithme/src/main/java/com/sparta/swithme/controller/RecordController.java

```java
@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<StudyRecord>> getRecords(@PathVariable Long userId) {
        // 사용자 ID로 기록을 조회하고 반환
        List<StudyRecord> records = recordService.getRecordsForUser(userId);
        return ResponseEntity.ok(records);
    }

    @PostMapping
    public ResponseEntity<StudyRecord> createRecord(@RequestBody StudyRecord studyRecord) {
        // 새로운 기록을 생성하고 저장
        StudyRecord newRecord = recordService.saveRecord(studyRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRecord);
    }
}
```

RecordController는 HTTP 요청을 처리하고, RecordService로 전달한다. 이러한 MVC 구조를 통해 의존성을 줄여서 유지보수성을 높이도록 코드를 작성

### **4. 예외 처리 및 최적화**

커스텀 예외 처리를 통해 예상 가능한 문제 상황에 대해 미리 대응하고자 함

swithme/src/main/java/com/sparta/swithme/exception/RecordTimeException.java

```java
public class RecordTimeException extends RuntimeException {
    public RecordTimeException(String message) {
        super(message);
    }
}
```

swithme/src/main/java/com/sparta/swithme/service/RecordService.java

```java
public void saveRecord(StudyRecord studyRecord) {
    if (studyRecord.getDuration() <= 0) {
        throw new RecordTimeException("Study duration must be positive.");
    }
    recordRepository.save(studyRecord);
}
```

RecordTimeException 을 정의하고, 사용자가 잘못된 데이터를 입력했을 때, 이를 즉시 예외처리하고 사용자에게 피드백을 제공하도록 설계


<br>
<br>
<br>

### 트러블슈팅

---

**Issue 1 ) 공부시간 기록이 이전 날짜로 표시되는 문제**

- 해결 : 시간 기록 시 오늘의 누적시간을 가져오던 로직을 페이지 조회 시에 추가로 구현하여 해결
- 결과 : 사용자는 매일 자신의 스톱워치 페이지에 접속 시 정확한 오늘의 누적 공부시간을 확인
- 코드 : getTodayAccumulatedTime() 메소드가 페이지 로딩 시에 호출되도록 조정하여 문제 해결

**Issue 2 ) 공부시간 미기록 시 조회 오류**

- 해결 : 조회할 때, DB에 저장된 오늘의 공부시간 날짜가 null일 경우 값을 0으로 설정하여 테이블을 미리 생성하여 오류 해결
- 결과 : 타 기능에서 해당 기능 사용 전적이 없어도 정확하고 안정적으로 조회
- 코드 : getTodayAccumulatedTime() 메소드 내에 날짜가 null일 경우, 누적 시간을 0으로 설정하는 로직을 추가하여 문제 해결

<br>
<br>
<br>

### 회고

---

이번 최종 프로젝트를 통해 MVC 패턴과 간단한 SpringSecurity,예외처리를 위한 커스텀 Exception, 간단한 JPA 를 통한 쿼리 최적화, 시간 파싱 로직을 다루며 데이터 무결성 유지 등
을 적용하는 방법을 익혔다.

또한, 앞으로 이 프로젝트를 다음 항목에 걸쳐서 개선하고자 함
1. 테스트코드 도입 - 1)Junit5 2) ArchUnit 3) Jacoco 코드 커버리지체크
2. 아키텍쳐 개선 - 1) 클린코드 2) DDD 3) clean arc
3. AWS EC2 수동배포 -> 자동배포 기술 도입
