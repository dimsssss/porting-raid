# porting-raid

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/dimsssss/porting-raid/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/dimsssss/porting-raid/tree/main)

## 보완한 점

## 랭킹 조회

기존에 작업한 프로젝트에서는 ranking을 조회하는 방식은 아래와 같다.

1. raid 로그를 데이터베이스에 저장한다.
2. 저장된 로그 기록을 유저별로 합계를 낸다

이와 같은 방법은 로그 전체를 전부 확인해야하기 때문에 데이터가 많아질수록 연산하는 작업도 늘어난다.
이것을 해결하기 위해 redis의 sorted set을 이용하였다.

### sorted set

> A Redis sorted set is a collection of unique strings (members) ordered by an associated score. When more than one string has the same score, the strings are ordered lexicographically. Some use cases for sorted sets include: Leaderboards. For example, you can use sorted sets to easily maintain ordered lists of the highest scores in a massive online game.
Rate limiters. In particular, you can use a sorted set to build a sliding-window rate limiter to prevent excessive API requests.


가장 최근 점수에 score를 더한 값으로 저장하기 때문에 따로 합계를 구할 필요도 없으며 score를 기준으로 정렬되기 때문에 rank도 자동으로 구할 수 있다.

### 기대 효과
데이터베이스의 작업을 줄여줄 수 있다. disk를 사용하는 RDBMS 특성상 데이터가 많아지면 속도가 느려진다. raid를 진행한 모든 유저의 rank를 구해야하기 때문에 index를 사용할 수도 없다. redis는 in-memory 저장소라서 rdbms보다 속도가 빠르고 sorted set 대부분 연산은 O(lgn)으로 빠르다.

이전 프로젝트에서는 rank를 구하는 작업을 RDBMS에서 처리했기 때문에 조회 쿼리가 복잡해졌고 Model쪽의 코드가 복잡해졌다. redis를 이용할 때 제공해주는 API로 구현 가능하기 때문에 코드가 훨씬 간결하다. 

### 단점
in-memory의 특성상 시스템에 장애가 생기면 저장한 모든 데이터가 사라지게 된다. 이에 대한 대비책으로는 두 가지 방법이 있다.

- redis의 persistance 기능을 사용한다. 이 기능에도 4가지 모드가 있지만 근본적으로 disk를 이용한다
- 기획을 변경한다. 예를 들어 회원 전체의 rank가 필요하지 않을 수 있다. 사용자 화면의 한페이지 정도의 rank 정보만 필요하다면 redis 뿐만 아니라 RDBMS도 사용 가능한 옵션이 된다.

### 결론

in-memory와 RDBMS 모두 장단점이 있어서 어떤 것을 선택해야할지는 사용하는 환경을 분석해야한다.

## Mock

이전 프로젝트에서 Mock을 활용하지 않았다. Mock이 꼭 필요할까? 라는 생각이 들었고 통합테스트, 단위테스트, e2e 테스트에 mock을 사용하지 않았다.
나중에 외부시스템의 API Key에 대한 이슈가 발생하면서 이번 프로젝트에서는 외부 시스템에 의존하는 코드는 Mock을 적극적으로 활용하였다

### 기대효과

외부 시스템에 대한 의존이 없어져서 코드를 변경하지 않아도 테스트 코드가 깨지는 상황을 막을 수 있다. 또한 테스트 코드가 많아질수록 지연 시간이 늘어나는데 Mock을 사용하면서 시간을 단축할 수 있다

### 단점

테스트하려는 대상과 관련된 의존성(외부시스템 제외)에도 Mock을 적용하면 세부 사항에 초점을 맞추게 된다.  다른 말로 하나가 변경되어 테스트 코드가 깨지면 기능이 실패하는 것으로 간주된다. 이것은 리팩터링을 하는데 부담이된다.

### 결론

장기적인 관점을 위해 외부시스템에 대한 코드만 Mock으로 처리하는 것이 바람직해보인다.

## Optional
예측 가능한 코드를 작성하기 위해, 코드가 정상 동작을 하지 않았을 때를 명시적으로 알리기 위해
null, Optional, 오류 반환이 있는데 java에서 지원하는 Optional을 적용

https://dimsss.notion.site/6-455428654030423d9fb48dd469f61e94

## Immutable
코드의 오용을 막기 위해서 의도치 않게 데이터가 변경되는 상황을 막기 위해서 불변 객체를 사용한다.
이와 대조적으로 변경 가능한 객체를 가변 객체라고 하는데 사용하던 객체가 의도치 않게 변경될 수 있다. 이런 동작을 가능하게 하는 요인 가운데 하나가 setter 메서드인데 기존에 작성한 setter 메서드를 지우고 불변 객체를 사용하도록 코드를 변경하였다

https://dimsss.notion.site/7-1-250beafd08c4486b859c2cc7dda6c61d

## 참고 자료

[sorted set](https://redis.io/docs/data-types/sorted-sets/)

[redis persistance](https://redis.io/docs/management/persistence/)

[단위 테스트](https://product.kyobobook.co.kr/detail/S000001805070)