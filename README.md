# java-convenience-store-precourse
# 편의점
## 구현 기능 목록


## 1. 파일 입출력 기능
- [ ] **상품 데이터 로드**: `src/main/resources/products.md` 파일을 읽어 상품 목록을 초기화
- [ ] **프로모션 데이터 로드**: `src/main/resources/promotions.md` 파일을 읽어 프로모션 정보 초기화

---

## 2. 상품 정보 출력 기능
- [ ] **환영 메시지 출력**: 결제 시스템 시작 시 환영 메시지와 함께 상품 목록 출력
- [ ] **상품 목록 표시**: 상품명, 가격, 재고, 프로모션 정보 포함하여 표시 (재고가 없을 시 "재고 없음" 표시)

---

## 3. 사용자 입력 기능
- [ ] **상품 선택 및 수량 입력**: `[상품명-수량]` 형식으로 구매할 상품과 수량 입력 받기
- [ ] **추가 여부 확인**: 프로모션 조건에 따른 증정 상품 추가 여부(Y/N) 입력 받기
- [ ] **정가 구매 여부 확인**: 프로모션 재고 부족 시 정가 구매 여부(Y/N) 입력 받기
- [ ] **멤버십 할인 적용 여부 확인**: 멤버십 할인을 받을지 여부(Y/N) 입력 받기
- [ ] **추가 구매 여부 확인**: 구매 완료 후 추가 구매 진행 여부(Y/N) 입력 받기

---

## 4. 입력 검증 및 예외 처리 기능
- [ ] **올바른 형식 확인**: `[상품명-수량]` 형식 확인 및 오류 메시지 출력 (`[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`)
- [ ] **상품 존재 여부 확인**: 입력한 상품명이 시스템에 존재하는지 확인 (`[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`)
- [ ] **재고 수량 확인**: 구매 수량이 상품 재고를 초과하는 경우 오류 메시지 출력 (`[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.`)
- [ ] **기타 잘못된 입력 처리**: 정의되지 않은 잘못된 입력 시 오류 메시지 출력 (`[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`)

---

## 5. 결제 처리 기능
- [ ] **상품별 결제 금액 계산**: 상품별 가격과 수량을 곱해 총 구매 금액 계산
- [ ] **프로모션 할인 적용**: 프로모션 조건(N+1)에 따라 상품 증정 및 증정 수량 기록
    - [ ] **프로모션 재고 우선 차감**: 프로모션 재고가 있을 경우 이를 우선 차감 후 부족 시 일반 재고 차감
    - [ ] **일부 프로모션 재고 부족 시 정가 결제 확인**: 프로모션 재고 부족 시 정가 결제 여부 반영
- [ ] **멤버십 할인 적용**: 멤버십 회원에 대해 프로모션 미적용 금액에 대해 최대 8,000원까지 30% 할인

---

## 6. 재고 관리 기능
- [ ] **재고 차감**: 결제 완료 후 구매한 수량만큼 해당 상품의 재고 차감
- [ ] **프로모션 재고 차감**: 프로모션 상품은 프로모션 재고에서 우선 차감하고, 부족할 경우 일반 재고 차감

---

## 7. 영수증 출력 기능
- [ ] **구매 내역 출력**: 사용자가 구매한 상품명, 수량, 금액을 영수증에 출력
- [ ] **증정 상품 내역 출력**: 프로모션으로 제공된 증정 상품 목록을 영수증에 출력
- [ ] **금액 정보 출력**: 총 구매액, 행사 할인, 멤버십 할인, 최종 결제 금액을 영수증에 보기 좋게 정렬하여 출력

---

## 8. 추가 구매 기능
- [ ] **재고 업데이트**: 구매 후 재고가 반영된 최신 상품 목록을 다시 표시
- [ ] **구매 종료**: 추가 구매가 불필요한 경우 시스템 종료

---

## 프로그래밍 요구사항
- [ ] build.gradle 파일은 변경할 수 없으며, 제공된 라이브러리 이외의 외부 라이브러리는 사용하지 않는다.
- [ ] 프로그램 종료 시 System.exit()를 호출하지 않는다.
- [ ] 프로그래밍 요구 사항에서 달리 명시하지 않는 한 파일, 패키지 등의 이름을 바꾸거나 이동하지 않는다.
- [ ] 자바 코드 컨벤션을 지키면서 프로그래밍한다.
- [ ] indent(인덴트, 들여쓰기) depth를 3이 넘지 않도록 구현한다. 2까지만 허용한다.
- [ ] 3항 연산자를 쓰지 않는다.
- [ ] 함수(또는 메서드)가 한 가지 일만 하도록 최대한 작게 만들어라.
- [ ] JUnit 5와 AssertJ를 이용하여 정리한 기능 목록이 정상적으로 작동하는지 테스트 코드로 확인한다.
- [ ] else 예약어를 쓰지 않는다.
- [ ] Java Enum을 적용하여 프로그램을 구현한다. 
- [ ] 구현한 기능에 대한 단위 테스트를 작성한다. 단, UI(System.out, System.in, Scanner) 로직은 제외한다.
- [ ] 함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현한다.
- [ ] 입출력을 담당하는 클래스를 별도로 구현한다.
- [ ] camp.nextstep.edu.missionutils에서 제공하는 DateTimes 및 Console API를 사용하여 구현해야 한다.