# scoup-backend
SCOUP : Schedule For Group

그룹의 스케줄 관리를 도와줍니다.

## 구현 예정 기능

- 스케줄 후보를 등록하고 투표를 하여 자동확정을 할 수 있습니다.
- 투표 마감일에 가장 많이 투표된 일정이 선택되도록 할 수 있습니다.
- 이벤트에 참여하는 그룹원의 일정을 고려하여 스케줄을 등록할 수 있습니다.
- 일정에 참여하기로 결정한 그룹원의 개인 스케줄에 해당 일정이 자동으로 등록됩니다.

## 공통 기획

- 회의록 https://goldenrod-string-7f8.notion.site/f32b24e0bfe342f5903a5205c5cede61
- 시나리오 https://docs.google.com/spreadsheets/d/1Ybww-QdL-P2zxyGYcWt0KAMxFF0z99ZPsv7ghPUSmM0/edit#gid=0
- 스토리보드(진행 중) https://docs.google.com/presentation/d/1yZeXxSYeO6yuCtz6WqnfKAGjhf9Notrq7G2qvNhI9dA/edit#slide=id.p

<!-- 추후 공통 레포로 이동 고려 -->

## 그라운드 룰

- 개발할 기능 목록에서 진행할 기능의 내용을 템플릿에 맞춰 이슈로 작성합니다.

- 인수테스트를 먼저 작성하고, 세부 구현을 하는 Outside-In방식으로 개발합니다.

- 필요하다고 생각하는 부분에서 단위 테스트를 진행하되, 도메인은 되도록이면 TDD로 개발합니다.

  - 해당 클래스만의 고유한 로직이 있을 경우 단위 테스트를 합니다.

    > e.g. 서비스에서 특정 조건에 따라 예외가 발생해야 하는 경우

  - 필요한 경우 Mockito를 활용합니다.

    > e.g. 서비스의 특정 로직만을 테스트하고 싶은 경우

- 기능을 완성하면 PR을 보내고 리뷰어와 상의하여 기능을 개선합니다.

- Approve와 테스트 성공 체크가 완료되면 PR을 머지합니다.

- 현재 개발중인 기능의 스코프를 벗어나는 부분이 필요한 경우 별도의 브랜치와 PR로 나누어 관리합니다.

- 사전에 정의한 용어와 컨벤션을 준수하여 작성합니다.

- 월수금 2시에 스크럼을 진행합니다. 필요시 회의를 이어서 진행합니다.

- 추가적인 아이디어나 논의사항이 있을 경우 Discussion을 생성합니다. 논의가 완료되면 이슈를 생성해서 해당 기능을 구현합니다.

## 관리 중인 문서

- API 유스케이스 https://docs.google.com/spreadsheets/d/1YqVxbVj4CtCC0iNIOA7JNs6kpJ4xCPY21wKqrXlSoAs/edit#gid=1572791990

- 개발 컨벤션 https://goldenrod-string-7f8.notion.site/Convention-1a98aa39c64b4a99a06e8fe217718a56

- 용어 정리 https://goldenrod-string-7f8.notion.site/181ecdb4ea684098a192b50e1d887347

- API 문서(컨셉 -> 초안 진행 중) https://goldenrod-string-7f8.notion.site/API-6f8d83dde50c4a0295472e6b9790ba44
