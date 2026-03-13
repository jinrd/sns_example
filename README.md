# sns_example

# 1. /api/v1/users/join 회원가입 기능만 구현

# 2. /api/v1/posts/write 게시글 작성

# 3. /api/v1/posts/list 저장되어 있는 게시글 조회

    - 문제점 존재했음
        api 요청에 대한 응답을 내려줄 PostResponseVo 에서
        this.username = post.getUser().getUsername();
        를 이용하여 username 을 세팅하니
        postRepository.findAll() 을 실행했을 때 게시글 조회하는 쿼리 1이 찍히고
        username 을 가져올 때 user 를 조회하는 쿼리 2가 찍혔다
        검색결과 이런 거를 N + 1 문제라고 한다.

        쿼리를 1번으로 줄이기 위해 JOIN FETCH 를 사용했다
        JPA 의 @Query 를 사용해서
        **"게시글을 가져올 때 유저 정보까지 한 번에 JOIN 해서 가져와라"**
        라고 명령할 수 있다고 한다.

# 4. /api/v1/posts/page/list?page={1}&size={2}

    - 많은 수의 게시글은 페이징으로 하여 가져올 수 있도록

# 5. JwtUtil.java 생성 후 토큰 제작

# 6. JwtTokenFilter 에 토큰 검증 추가

    - 디테일 설정이 필요한 이유
        디테일 설정의 목표는 보안감사와 추적을 위해 거의 필수로 넣는 스프링 시큐리티의 표준 관행이라고 한다.

        1) 어디서 접속했는지 기록하기 위해
            사용죠의 원격 IP 주소와 세션ID(세션 사용시)를 추출해 인증 객체의 details 공간에 넣어준다
        2) 해킹이나 이상 탐지를 위해
            평소의 IP 가 아닌 다른 국가의 특정 IP에서 접속시 해당 IP 를 차단할 수 있다.
        3) 슾링 시큐리티 프레임워크와의 일관성 유지
            원래 스프링 시큐리티가 기본으로 제공하는 로그인 필터(폼 로그인 등)들은 내부적으로 알아서 저 setDetails 과정을 거칩니다.

        요약
            authenticationToken.setDetails(...) 코드는 **"이 사용자가 아이디가 뭔지는 이미 확인했는데, 혹시 나중에 필요할지 모르니 사용자의 접속 IP 주소 같은 부가적인 HTTP 요청 정보도 인증 신분증(Authentication) 뒷면에 꼼꼼하게 적어두자"**라는 의미이다.

# 7. /api/v1/posts/write 게시글 작성 수정

    - 토큰 기반으로 인증/인가 후 게시글 작성할 수 있게 변경

# 8. 게시글 검색 기능 추가

    - keyword 를 통해서 검색 기능 추가 @Query 사용

# 9. 게시글 수정/삭제 및 권한 검증

# 10. 시간 자동화(실무 필수 기능 : JPA Auditing)

    - 기능 추가 목적 PostEntity 또는 UserEntity 를 보면 작성 및 수정 시간을 알 수 없다
    - SNS 시간 정렬이 필요하다
