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
