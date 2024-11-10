# java-convenience-store-precourse

## 기능 요구사항

1. 입력
    - [X] 구매할 상품과 수량을 입력받는다.
        - [X] 상품명, 수량은 하이픈(-)으로 구분한다.
            - [X] 상품명이 공백이면 예외를 발생시킨다.
            - [X] 수량이 숫자가 아니면 예외를 발생시킨다.
            - [X] 수량이 0 이하면 예외를 발생시킨다.
            - [X] 상품이 존재하지 않으면 예외를 발생시킨다.
        - [X] 개별 상품은 대괄호([])로 묶는다.
            - [X] 개별 상품이 대괄호([])로 묶여있지 않으면 예외를 발생시킨다.
        - [X] 쉼표료 개별 상품을 구분한다.
    - [X] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다.
        - [X] 입력이 Y 또는 N이 아닌경우 예외를 발생시킨다.
    - [X] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
        - [X] 입력이 Y 또는 N이 아닌경우 예외를 발생시킨다.
    - [X] 멤버십 할인 적용 여부를 입력 받는다.
        - [X] 입력이 Y 또는 N이 아닌경우 예외를 발생시킨다.
    - [ ] 추가 구매 여부를 입력 받는다.
        - [ ] 입력이 Y 또는 N이 아닌경우 예외를 발생시킨다.
        - [ ] N을 입력받은 경우 프로그램을 종료한다
        - [ ] Y를 입력받은 경우 상품 목록을 다시 출력하고 구매를 진행한다
    - [ ] 사용자가 잘못된 값을 입력했을때 오류 메시지가 출력 된 후 그 부분부터 입력을 다시 받는다.

2. 재고
    - [X] 상품 정보 초기화
        - [X] src/main/resources/products.md 파일에서 상품 목록을 불러온다
        - [X] 파일이 존재하지 않을 경우 예외를 발생시킨다
    - [X] 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.
        - [X] 구매 수량이 재고 수량을 초과한 경우 예외를 발생시킨다.
        - [X] 존재하지 않는 상품을 입력한 경우 예외를 발생시킨다.
    - [X] 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감한다.

3. 프로모션 할인
    - [X] 프로모션 정보 초기화
        - [X] src/main/resources/promotions.md 파일에서 행사 목록을 불러온다
        - [X] 파일이 존재하지 않을 경우 예외를 발생시킨다
    - [X] 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용한다.
    - [X] 프로모션은 N개 구매시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다.
    - [X] 동일 상품에 여러 프로모션이 적용되지 않는다.
    - [X] 프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
        - [X] 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.
        - [ ] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내한다.
            - [ ] Y 를 입력 할 경우 정가로 결제한다.
            - [ ] N 을 입력 할 경우 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.
    - [ ] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
        - [ ] Y 를 입력한 경우 증정 받을 수 있는 상품을 추가한다.
        - [ ] N 을 입력한 경우 증정 받을 수 있는 상품을 추가하지 않는다.

4. 멤버십 할인
    - [ ] 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
    - [ ] 멤버십 할인의 최대 한도는 8,000원이다.

5. 출력
    - [ ] 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다. 만약 재고가 0개라면 재고 없음을 출력한다.
    - [ ] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 혜택에 대한 안내 메시지를 출력한다.
    - [ ] 멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.
    - [ ] 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다.
    - [ ] 추가 구매 여부를 확인하기 위해 안내 문구를 출력한다.
    - [ ] 사용자가 잘못된 값을 입력했을 때, "[ERROR]"로 시작하는 오류 메시지와 함께 상황에 맞는 안내를 출력한다.
